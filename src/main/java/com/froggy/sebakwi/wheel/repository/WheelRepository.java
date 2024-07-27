package com.froggy.sebakwi.wheel.repository;

import com.froggy.sebakwi.wheel.domain.Wheel;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WheelRepository extends JpaRepository<Wheel, Long> {

    List<Wheel> findReplacementWheelByCreatedDateBefore(LocalDate replacementCycleDate);
}
