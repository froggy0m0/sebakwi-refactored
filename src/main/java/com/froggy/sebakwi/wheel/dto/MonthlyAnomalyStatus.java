package com.froggy.sebakwi.wheel.dto;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MonthlyAnomalyStatus {

    private AnomalyStatusCount count;
    private List<WheelInfo> wheelList;


    public static MonthlyAnomalyStatus fromEntity(List<CheckupList> dataList) {
        int crackCount = 0;
        int stampCount = 0;
        int abrasionCount = 0;

        List<WheelInfo> wheelList = new ArrayList<>();
        for (CheckupList data : dataList) {
            WheelInfo wheelInfo = WheelInfo.fromEntity(data);

            if (wheelInfo.isCrack() || wheelInfo.isStamp() || wheelInfo.isAbrasion()) {
                if (wheelInfo.isCrack()) crackCount++;
                if (wheelInfo.isStamp()) stampCount++;
                if (wheelInfo.isAbrasion()) abrasionCount++;
            }

            wheelList.add(wheelInfo);
        }

        AnomalyStatusCount count = AnomalyStatusCount.
            createAnomalyStatusCount(crackCount, stampCount, abrasionCount);

        return MonthlyAnomalyStatus.builder()
            .count(count)
            .wheelList(wheelList)
            .build();
    }
}
