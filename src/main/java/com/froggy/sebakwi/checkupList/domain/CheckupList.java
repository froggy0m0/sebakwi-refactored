package com.froggy.sebakwi.checkupList.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.froggy.sebakwi.wheel.domain.Wheel;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class CheckupList {

    @Id @GeneratedValue
    @Column(name= "checkup_list_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "wheel_id")
    private Wheel wheel;

    private LocalDateTime checkedDate;

    private String wheelImage;

    @Enumerated(EnumType.STRING)
    private WheelStatus status;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "2.0", inclusive = true)
    private Double diameter;

    @NotNull
    private Boolean crack;

    @NotNull
    private Boolean stamp;

    @NotNull
    private Boolean abrasion;
}
