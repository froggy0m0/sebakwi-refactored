package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = PRIVATE)
@ToString
public class CheckupRequest {

    private String ohtSerialNumber;
    @NotEmpty
    private String wheelSerialNumber;
    private int position;

    private String wheelImage;
    private double diameter;
    private boolean crack;
    private boolean stamp;
}
