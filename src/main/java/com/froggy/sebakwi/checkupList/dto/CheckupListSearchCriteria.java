package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Data
public class CheckupListSearchCriteria {

    private Boolean isCheckedDate;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private Boolean onlyAbnormal;

    private Integer position;

    private String ohtSerialNumber;
    private String wheelSerialNumber;

    private int page;

    private Boolean sortByCheck;
    private Boolean desc = true;
}