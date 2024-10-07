package com.froggy.sebakwi.checkupList.service;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.froggy.sebakwi.checkupList.dto.CheckupRequest;
import com.froggy.sebakwi.checkupList.dto.CheckupResponse;
import com.froggy.sebakwi.checkupList.repository.CheckupListQuerydslRepository;
import com.froggy.sebakwi.checkupList.repository.CheckupListRepository;
import com.froggy.sebakwi.sse.Event.AnomalyDetectedEvent;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import com.froggy.sebakwi.wheel.dto.WheelInfo;
import com.froggy.sebakwi.wheel.repository.WheelRepository;
import com.froggy.sebakwi.wheel.service.WheelService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CheckupListService {

    private final CheckupListRepository checkupListRepository;
    private final WheelRepository wheelRepository;
    private final CheckupListQuerydslRepository checkupListQuerydslRepository;

    private final ApplicationEventPublisher eventPublisher;

    private static final double MINIMUM_DIAMETER_FOR_ABRASION = 1.0;

    // 차트용 이상 데이터 저장소 (이상데이터 발생 시 추가)
    private final List<WheelInfo> periodicAnomalyDataList;

    public Page<CheckupList> searchCheckupList(CheckupListSearchCriteria criteria) {
        return checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
    }

    /**
     * 휠 검진 데이터 처리
     * 이상 검진시 Event 호출
     */
    @Transactional
    public void processCheckupData(CheckupRequest request) {

        // DTO 를 엔티티로 변환
        CheckupList checkupList = createCheckupList(request);

        // 검진 데이터 저장
        checkupListRepository.save(createCheckupList(request));

        // 이상 검진시 Event 호출
        if (checkupList.getStatus() == WheelStatus.ABNORMAL) {
            eventPublisher.publishEvent(new AnomalyDetectedEvent(this, checkupList));
        }
    }

    private CheckupList createCheckupList(CheckupRequest request) {
        return CheckupList.builder()
            .wheel(wheelRepository.findBySerialNumber(request.getWheelSerialNumber())
                .orElseThrow(() -> new NoSuchElementException("바퀴를 찾을 수 없습니다.")))
            .checkedDate(LocalDateTime.now())
            .wheelImage(request.getWheelImage())
            .status(determineWheelStatus(request))
            .diameter(request.getDiameter())
            .crack(request.isCrack())
            .stamp(request.isStamp())
            .abrasion(request.getDiameter() >= MINIMUM_DIAMETER_FOR_ABRASION)
            .build();
    }

    private WheelStatus determineWheelStatus(CheckupRequest request) {
        if (request.isCrack() || request.isStamp() || request.getDiameter() >= 1) {
            return WheelStatus.ABNORMAL;
        }
        return WheelStatus.NORMAL;
    }

    public List<CheckupResponse> findCheckupListModal(Long checkupListId) {
        Long ohtId = checkupListRepository
            .findOhtIdByCheckupListId(checkupListId)
            .orElseThrow(() -> new NoSuchElementException("검색 목록을 찾을 수 없습니다."));

        CheckupList checkupList = checkupListRepository.
            findById(checkupListId)
            .orElseThrow(() -> new NoSuchElementException("검색 목록을 찾을 수 없습니다."));

    return CheckupResponse.fromEntity(
        checkupListQuerydslRepository.findCheckupListModal(ohtId, checkupList.getCheckedDate()));
    }
}
