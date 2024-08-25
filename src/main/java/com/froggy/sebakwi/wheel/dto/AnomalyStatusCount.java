package com.froggy.sebakwi.wheel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnomalyStatusCount {

    private int crack;
    private int stamp;
    private int abrasion;
    private int total;

    public static AnomalyStatusCount createAnomalyStatusCount(int crack, int stamp, int abrasion) {
        return AnomalyStatusCount.builder()
            .crack(crack)
            .stamp(stamp)
            .abrasion(abrasion)
            .total(crack + stamp + abrasion)
            .build();
    }
}
