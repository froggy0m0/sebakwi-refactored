package com.froggy.sebakwi.wheel.domain;

import static lombok.AccessLevel.PROTECTED;

import com.froggy.sebakwi.oht.domain.Oht;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Wheel {

    @Id @GeneratedValue
    @Column(name = "wheel_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oht_id")
    private Oht oht;

    private String serialNumber;

    private LocalDate createdDate;

    private Integer position;
}
