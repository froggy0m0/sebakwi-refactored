package com.froggy.sebakwi.wheel.service;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.repository.CheckupListRepository;
import com.froggy.sebakwi.util.DateFormatterUtil;
import com.froggy.sebakwi.util.config.RedisConfig;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import com.froggy.sebakwi.wheel.dto.MonthlyAnomalyStatus;
import com.froggy.sebakwi.wheel.dto.ReplacementWheelResponse;
import com.froggy.sebakwi.wheel.dto.WheelChartResponse;
import com.froggy.sebakwi.wheel.dto.WheelInfo;
import com.froggy.sebakwi.wheel.repository.WheelRedisRepository;
import com.froggy.sebakwi.wheel.repository.WheelRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WheelService {

    private final WheelRepository wheelRepository;
    private final WheelRedisRepository wheelRedisRepository;
    private final CheckupListRepository checkupListRepository;

    public List<ReplacementWheelResponse> findReplacementWheels() {
        LocalDate replacementCycleDate = LocalDate.now().minusYears(2);

        return wheelRepository.findReplacementWheelByCreatedDateBefore(replacementCycleDate)
            .stream()
            .map(ReplacementWheelResponse::new)
            .collect(Collectors.toList());
    }

    public MonthlyAnomalyStatus findMonthlyAnomalyStatus() {

        List<CheckupList> abnormalWheelInfo = checkupListRepository.findAbnormalCheckListByMonth(
            getCurrentMonth(),
            WheelStatus.ABNORMAL
        );

        return MonthlyAnomalyStatus.fromEntity(abnormalWheelInfo);
    }

    private static LocalDateTime getCurrentMonth() {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay();
    }

    public void appendData(List<WheelInfo> wheeInfoList) {
        String key = RedisConfig.getChartKey();
        WheelChartResponse wheelChartResponse = wheelRedisRepository.getData(key);
        log.info("wheel={}", wheelChartResponse);

        if (wheelChartResponse == null) {
            wheelChartResponse = WheelChartResponse.createWheelChartResponse();
        }

        wheelChartResponse.getXData()
            .add(DateFormatterUtil.defaultDateFormat(LocalDateTime.now()));

        wheelChartResponse.getYData()
            .add(wheelChartResponse.getYData().isEmpty() ?
                0 : wheelChartResponse.getYData().peek() + wheeInfoList.size()
            );

        wheelChartResponse.getToolTips()
            .add(wheeInfoList);

        wheelRedisRepository.saveData(key, wheelChartResponse);
    }

    public WheelChartResponse findWheelChartInfo() {
        return wheelRedisRepository.getData(RedisConfig.getChartKey());
    }
}
