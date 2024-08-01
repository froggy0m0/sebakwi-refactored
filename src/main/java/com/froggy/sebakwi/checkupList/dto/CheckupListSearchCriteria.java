package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDate;
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
    private LocalDate startDateTime;
    private LocalDate endDateTime;

    private Boolean onlyAbnormal;

    private Integer position;

    private String ohtSerialNumber;
    private String wheelSerialNumber;

    private int page;

    private Boolean sortByCheck;
    private Boolean desc = true;
}

/**
 * 기본타입 -> 생략으로 요청하면?? 객체타입 -> 생략으로 요청하면??
 * <p>
 * get요청에대한 파라매터들을 요청으로받을건데 이거 받아지나 확인좀
 */