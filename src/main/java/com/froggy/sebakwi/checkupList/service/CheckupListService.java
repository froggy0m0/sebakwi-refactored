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

    private final WheelService wheelService;

    private final ApplicationEventPublisher eventPublisher;

    private static final double MINIMUM_DIAMETER_FOR_ABRASION = 1.0;

    // 차트용 이상 데이터 저장소 (이상데이터 발생 시 추가)
    private final List<WheelInfo> periodicAnomalyDataList;

    public Page<CheckupList> searchCheckupList(CheckupListSearchCriteria criteria) {
        return checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
    }

    @Transactional
    public void processCheckupData(CheckupRequest data) {

        WheelStatus wheelStatus = WheelStatus.NORMAL;
        if ( data.isCrack() || data.isStamp() || data.getDiameter() >= 1) {
            wheelStatus = WheelStatus.ABNORMAL;
        }

        CheckupList checkupList = createCheckupList(data, wheelStatus);

        checkupListRepository.save(checkupList);
        log.info("검진 데이터가 저장되었습니다.");

        if (checkupList.getStatus() == WheelStatus.ABNORMAL) {
            eventPublisher.publishEvent(new AnomalyDetectedEvent(this));

            periodicAnomalyDataList.add(WheelInfo.fromEntity(checkupList));
            wheelService.appendData(periodicAnomalyDataList);

            periodicAnomalyDataList.clear();
        }
    }


    private CheckupList createCheckupList(CheckupRequest data, WheelStatus wheelStatus) {
        return CheckupList.builder()
            .wheel(wheelRepository.findBySerialNumber(data.getWheelSerialNumber())
                .orElseThrow(() -> new NoSuchElementException("바퀴를 찾을 수 없습니다.")))
            .checkedDate(LocalDateTime.now())
            .wheelImage(data.getWheelImage())
            .status(wheelStatus)
            .diameter(data.getDiameter())
            .crack(data.isCrack())
            .stamp(data.isStamp())
            .abrasion(data.getDiameter() >= MINIMUM_DIAMETER_FOR_ABRASION)
            .build();
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
