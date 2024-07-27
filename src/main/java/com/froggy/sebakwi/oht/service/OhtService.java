package com.froggy.sebakwi.oht.service;

import com.froggy.sebakwi.oht.dto.OHTStatusCount;
import com.froggy.sebakwi.oht.repository.OhtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OhtService {

    private final OhtRepository ohtRepository;

    public OHTStatusCount findOhtStatusCounts() {
        return OHTStatusCount.builder()
            .totalOht(ohtRepository.count())
            .maintenance(ohtRepository.countByMaintenanceTrue())
            .build();
    }
}