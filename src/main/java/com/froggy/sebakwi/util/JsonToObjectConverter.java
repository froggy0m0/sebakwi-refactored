package com.froggy.sebakwi.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JsonToObjectConverter {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convert(String payload, Class<T> clazz) {
        T data = null;
        try {
            data = objectMapper.readValue(payload, clazz);
        } catch (Exception e) {
            log.error("Error handling MQTT message: {}", e.getMessage(), e);
        }
        return data;
    }

}
