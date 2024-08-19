package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PRIVATE;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.util.DateFormatterUtil;
import com.froggy.sebakwi.wheel.domain.Wheel;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder(access = PRIVATE)
@Getter
@ToString
public class CheckupResponse {

    private Long checkupListId;
    private String wheelNumber;
    private Integer position;
    private String ohtNumber;
    private String checkedDate;
    private String wheelImage;
    private Double diameter;
    private Boolean crack;
    private Boolean stamp;
    private Boolean abrasion;
    private WheelStatus status;
    private LocalDate createdDate;

    public static List<CheckupResponse> fromEntity(List<CheckupList> cList) {
        if (cList.get(0) == null) {
            return new ArrayList<>();
        }

        Wheel w = cList.get(0).getWheel();
        String ohtNumber = w.getOht().getSerialNumber();
        LocalDate createdDate = w.getCreatedDate();

        return cList.stream()
            .map(c -> CheckupResponse.builder()
                .checkupListId(c.getId())
                .wheelNumber(c.getWheel().getSerialNumber())
                .position(c.getWheel().getPosition())
                .ohtNumber(ohtNumber)
                .checkedDate(DateFormatterUtil.formatCheckedDate(c.getCheckedDate()))
                .wheelImage(c.getWheelImage())
                .diameter(c.getDiameter())
                .crack(c.getCrack())
                .stamp(c.getStamp())
                .abrasion(c.getAbrasion())
                .status(c.getStatus())
                .createdDate(createdDate)
                .build()
            ).collect(Collectors.toList());
    }
}
