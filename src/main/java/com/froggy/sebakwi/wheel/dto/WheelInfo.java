package com.froggy.sebakwi.wheel.dto;

import static lombok.AccessLevel.PRIVATE;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Builder
public class WheelInfo {

    private String wheelNumber;
    private String ohtNumber;
    private int position;
    private double diameter;
    private boolean crack;
    private boolean stamp;
    private boolean abrasion;

    public static WheelInfo fromEntity(CheckupList cl) {
        return WheelInfo.builder()
            .wheelNumber(cl.getWheel().getSerialNumber())
            .ohtNumber(cl.getWheel().getOht().getSerialNumber())
            .position(cl.getWheel().getPosition())
            .diameter(cl.getDiameter())
            .crack(cl.getCrack())
            .stamp(cl.getStamp())
            .abrasion(cl.getAbrasion())
            .build();
    }
}
