package com.froggy.sebakwi.chart;

import com.froggy.sebakwi.wheel.dto.WheelInfo;
import com.froggy.sebakwi.wheel.service.WheelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class AnomalyDataScheduler {

    private final WheelService wheelService;
    private final List<WheelInfo> periodicAnomalyDataList;

    @Scheduled(cron = "*/10 * * * * *")
    public void storeWheelChartResponse() {
        wheelService.appendData(periodicAnomalyDataList);

        periodicAnomalyDataList.clear();
    }

}
