package com.froggy.sebakwi.checkupList.dto;

import static lombok.AccessLevel.PRIVATE;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.util.DateFormatterUtil;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = PRIVATE)
public class CheckupListDTO {

    private Long checkupListId;
    private String wheelNumber;
    private int position;
    private String ohtNumber;
    private String checkedDate;
    private WheelStatus status;
    private LocalDate createdDate;

    public static CheckupListDTO fromEntity(CheckupList cl) {
        return CheckupListDTO.builder()
            .checkupListId(cl.getId())
            .wheelNumber(cl.getWheel().getSerialNumber())
            .position(cl.getWheel().getPosition())
            .ohtNumber(cl.getWheel().getOht().getSerialNumber())
            .checkedDate(DateFormatterUtil.defaultDateFormat(cl.getCheckedDate()))
            .status(cl.getStatus())
            .createdDate(cl.getWheel().getCreatedDate())
            .build();
    }
}
