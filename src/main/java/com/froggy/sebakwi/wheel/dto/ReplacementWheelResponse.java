package com.froggy.sebakwi.wheel.dto;

import static lombok.AccessLevel.PROTECTED;

import com.froggy.sebakwi.wheel.domain.Wheel;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
public class ReplacementWheelResponse {

    private String wheelNumber;
    private LocalDate createdDate;

    public ReplacementWheelResponse(Wheel wheel) {
        this.wheelNumber = wheel.getSerialNumber();
        this.createdDate = wheel.getCreatedDate();
    }
}
