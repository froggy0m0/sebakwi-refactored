package com.froggy.sebakwi.oht.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Oht {

    @Id @GeneratedValue
    @Column(name= "oht_id")
    private Long id;

    @Column(length = 20)
    private String serialNumber;

    private boolean maintenance;
}
