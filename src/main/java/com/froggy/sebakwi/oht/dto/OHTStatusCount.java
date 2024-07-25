package com.froggy.sebakwi.oht.dto;

import static lombok.AccessLevel.PROTECTED;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = PROTECTED)
public class OHTStatusCount {

    private Long totalOht;
    private Long maintenance;
}
