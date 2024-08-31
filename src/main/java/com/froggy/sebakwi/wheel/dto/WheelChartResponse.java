package com.froggy.sebakwi.wheel.dto;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class WheelChartResponse {

    // 시간 데이터
    @Builder.Default
    private List<String> xData = new ArrayList<>();

    // 이상 휠 누적 갯수
    @Builder.Default
    private Stack<Integer> yData = new Stack<>();

    @Builder.Default
    private Queue<List<WheelInfo>> toolTips = new ArrayDeque<>();

    public static WheelChartResponse createWheelChartResponse() {
        return WheelChartResponse.builder()
            .build();
    }
}