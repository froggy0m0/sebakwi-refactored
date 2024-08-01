package com.froggy.sebakwi.checkupList.repository;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckupListRepository extends JpaRepository<CheckupList, Long> {

}
