package com.froggy.sebakwi.oht.repository;

import com.froggy.sebakwi.oht.domain.Oht;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OhtRepository extends JpaRepository<Oht, Long> {

    Long countByMaintenanceTrue();
}
