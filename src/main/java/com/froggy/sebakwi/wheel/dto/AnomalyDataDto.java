package com.froggy.sebakwi.wheel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyDataDto {

    private String wheelNumber;
    private String ohtNumber;
    private int position;
    private double diameter;
    private boolean crack;
    private boolean stamp;
    private boolean abrasion;
}
