package com.froggy.sebakwi.wheel.controller;

import com.froggy.sebakwi.util.config.RedisConfig;
import com.froggy.sebakwi.wheel.dto.MonthlyAnomalyStatus;
import com.froggy.sebakwi.wheel.dto.ReplacementWheelResponse;
import com.froggy.sebakwi.wheel.dto.WheelChartResponse;
import com.froggy.sebakwi.wheel.repository.WheelRedisRepository;
import com.froggy.sebakwi.wheel.service.WheelService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wheels")
@RequiredArgsConstructor
@Slf4j
public class WheelController {

    private final WheelService wheelService;
    private final WheelRedisRepository wheelRedisRepository;

    @GetMapping("/replacement")
    public List<ReplacementWheelResponse> getReplacementWheels() {
        return wheelService.findReplacementWheels();
    }

    @GetMapping("/monthly")
    public MonthlyAnomalyStatus getMonthlyAnomalyStatus() {
        return wheelService.findMonthlyAnomalyStatus();
    }

    @GetMapping("/chart")
    public WheelChartResponse getWheelChartInfo() {
        return wheelService.findWheelChartInfo();
    }
}
