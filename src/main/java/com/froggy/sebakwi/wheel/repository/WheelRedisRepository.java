package com.froggy.sebakwi.wheel.repository;

import com.froggy.sebakwi.wheel.dto.WheelChartResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WheelRedisRepository {

    private final RedisTemplate<String, WheelChartResponse> redisTemplate;

    public void saveData(String key, WheelChartResponse data) {
        redisTemplate.opsForValue().set(key, data);
    }

    public WheelChartResponse getData(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
            .orElse(WheelChartResponse.createWheelChartResponse());
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}