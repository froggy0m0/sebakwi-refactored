package com.froggy.sebakwi.wheel.service;

import com.froggy.sebakwi.wheel.dto.ReplacementWheelResponse;
import com.froggy.sebakwi.wheel.repository.WheelRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class WheelService {

    private final WheelRepository wheelRepository;

    public List<ReplacementWheelResponse> findReplacementWheels() {
        LocalDate replacementCycleDate  = LocalDate.now().minusYears(2);

        return wheelRepository.findReplacementWheelByCreatedDateBefore(replacementCycleDate)
            .stream()
            .map(ReplacementWheelResponse::new)
            .collect(Collectors.toList());
    }
}
