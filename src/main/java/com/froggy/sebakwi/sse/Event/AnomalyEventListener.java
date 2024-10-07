package com.froggy.sebakwi.sse.Event;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.sse.controller.SseUtils;
import com.froggy.sebakwi.wheel.dto.WheelInfo;
import com.froggy.sebakwi.wheel.service.WheelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnomalyEventListener {

    private final WheelService wheelService;
    private final List<WheelInfo> periodicAnomalyDataList;

    private final SseUtils sseUtils;

    /**
     * 이상 데이터가 감지되면 이를 처리하고
     * 최신 월간 이상 상태 데이터를 SSE 를 통해 연결된 클라이언트에 전송하는 함수
     */
    @Async
    @EventListener
    public void onAnomalyDetected(AnomalyDetectedEvent event) {
        periodicAnomalyDataList.add(WheelInfo.fromEntity(event.getCheckupList()));
        wheelService.appendData(periodicAnomalyDataList);
        
        sseUtils.sendAll(
            wheelService.findMonthlyAnomalyStatus()
        );
    }
}


