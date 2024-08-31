package com.froggy.sebakwi.util.config;

import com.froggy.sebakwi.wheel.dto.WheelInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public List<WheelInfo> periodicAnomalyDataList() {
        return Collections.synchronizedList(new ArrayList<>());
    }
}