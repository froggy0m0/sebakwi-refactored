package com.froggy.sebakwi.checkupList.repository;

import static com.froggy.sebakwi.checkupList.domain.QCheckupList.checkupList;
import static com.froggy.sebakwi.wheel.domain.WheelStatus.ABNORMAL;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.domain.QCheckupList;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CheckupListQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    static private final int DEFAULT_LIMIT = 15;

    public Page<CheckupList> findCheckupListByCriteria(CheckupListSearchCriteria criteria) {
        QCheckupList checkupList = QCheckupList.checkupList; // Q 클래스 인스턴스 생성

        Pageable pageable = PageRequest.of(criteria.getPage(), DEFAULT_LIMIT, Sort.unsorted());

        int size = queryFactory
            .select(checkupList.id)
            .where(
                wheelSerialNumberEq(criteria.getWheelSerialNumber()),
                ohtSerialNumberEq(criteria.getOhtSerialNumber()),
                positionEq(criteria.getPosition()),
                onlyAbnormal(criteria.getOnlyAbnormal()),
                dateBetween(criteria.getStartDateTime(), criteria.getEndDateTime())
            )
            .from(checkupList)
            .fetch().size();

        List<CheckupList> checkupLists = queryFactory
            .selectFrom(checkupList)
            .where(
                wheelSerialNumberEq(criteria.getWheelSerialNumber()),
                ohtSerialNumberEq(criteria.getOhtSerialNumber()),
                positionEq(criteria.getPosition()),
                onlyAbnormal(criteria.getOnlyAbnormal()),
                dateBetween(criteria.getStartDateTime(), criteria.getEndDateTime())
            )
            .from(checkupList)
            .orderBy(getOrderSpecifier(criteria.getDesc()))
            .offset(pageable.getOffset())
            .limit(DEFAULT_LIMIT)
            .fetch();

        return new PageImpl<>(checkupLists, pageable, size);
    }

    public List<CheckupList> findCheckupListModal(Long ohtId, LocalDateTime baseTime) {
        QCheckupList checkupList = QCheckupList.checkupList; // 메인 쿼리용 Q 클래스 인스턴스

        LocalDateTime startTime = baseTime.minusSeconds(2); // 기준 시간 -2초
        LocalDateTime endTime = baseTime.plusSeconds(2);    // 기준 시간 +2초

        // OHT에 연결된 각 4개의 휠에 대한 CheckupList 데이터를 조회
        return queryFactory
            .selectFrom(checkupList)
            .where(checkupList.wheel.oht.id.eq(ohtId)
                .and(checkupList.checkedDate.between(startTime, endTime))
            )
            .orderBy(checkupList.wheel.id.asc())
            .fetch();
    }

    private BooleanExpression wheelSerialNumberEq(String serialNumber) {
        return serialNumber != null ? checkupList.wheel.serialNumber.eq(serialNumber) : null;
    }

    private BooleanExpression ohtSerialNumberEq(String serialNumber) {
        return serialNumber != null ? checkupList.wheel.oht.serialNumber.eq(serialNumber) : null;
    }

    private BooleanExpression positionEq(Integer position) {
        return position != null ? checkupList.wheel.position.eq(position) : null;
    }

    private BooleanExpression onlyAbnormal(Boolean onlyAbnormal) {
        if (onlyAbnormal == null || !onlyAbnormal) {
            return null;
        }

        return checkupList.status.eq(ABNORMAL);
    }

    private BooleanExpression dateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null && endDateTime == null) {
            return null;
        }

        LocalDateTime start = (startDateTime != null) ? startDateTime : LocalDateTime.MIN;
        LocalDateTime end = (endDateTime != null) ? endDateTime : LocalDateTime.MAX;

        return checkupList.checkedDate.between(start, end);
    }

    private OrderSpecifier<?> getOrderSpecifier(Boolean desc) {
        if (desc) {
            return checkupList.id.desc();
        }

        return checkupList.id.asc();
    }
}
