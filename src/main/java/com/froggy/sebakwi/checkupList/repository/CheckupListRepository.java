package com.froggy.sebakwi.checkupList.repository;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckupListRepository extends JpaRepository<CheckupList, Long> {

    @Query("SELECT cl.wheel.oht.id FROM CheckupList cl WHERE cl.id = :checkupListId")
    Optional<Long> findOhtIdByCheckupListId(@Param("checkupListId") Long checkupListId);
}
