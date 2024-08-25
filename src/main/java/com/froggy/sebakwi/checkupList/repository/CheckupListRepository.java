package com.froggy.sebakwi.checkupList.repository;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CheckupListRepository extends JpaRepository<CheckupList, Long> {

    @Query("SELECT cl.wheel.oht.id FROM CheckupList cl WHERE cl.id = :checkupListId")
    Optional<Long> findOhtIdByCheckupListId(@Param("checkupListId") Long checkupListId);


    @Query(
            "SELECT cl1 " +
            "FROM CheckupList cl1 " +
            "JOIN FETCH cl1.wheel w " +
            "JOIN FETCH w.oht oht " +
            "WHERE cl1.id IN ( " +
            "   SELECT MAX(cl2.id) " +
            "   FROM CheckupList cl2 " +
            "   WHERE cl2.checkedDate >= :date " +
            "     AND cl2.status = :abnormal " +
            "   GROUP BY cl2.wheel.id " +
            ") " +
            "ORDER BY w.serialNumber"
    )
    List<CheckupList> findAbnormalCheckListByMonth(LocalDateTime date, WheelStatus abnormal);
}
