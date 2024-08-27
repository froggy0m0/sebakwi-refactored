package com.froggy.sebakwi.sse.Event;

import com.froggy.sebakwi.sse.controller.SseUtils;
import com.froggy.sebakwi.wheel.service.WheelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnomalyEventListener {

    private final WheelService wheelService;

    private final SseUtils sseUtils;

    @Async
    @EventListener
    public void onAnomalyDetected(AnomalyDetectedEvent event) {
        sseUtils.sendAll(
            wheelService.findMonthlyAnomalyStatus()
        );
    }
}