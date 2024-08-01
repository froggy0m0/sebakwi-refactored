package com.froggy.sebakwi.checkupList.repository;

import static com.froggy.sebakwi.checkupList.domain.QCheckupList.checkupList;
import static com.froggy.sebakwi.wheel.domain.WheelStatus.ABNORMAL;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.domain.QCheckupList;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
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

    private BooleanExpression dateBetween(LocalDate startDateTime, LocalDate endDateTime) {
        if (startDateTime == null && endDateTime == null) {
            return null;
        }

        LocalDate start = (startDateTime != null) ? startDateTime : LocalDate.MIN;
        LocalDate end = (endDateTime != null) ? endDateTime : LocalDate.MAX;

        return checkupList.checkedDate.between(start, end);
    }

    private OrderSpecifier<?> getOrderSpecifier(Boolean desc) {
        if (desc) {
            return checkupList.id.desc();
        }

        return checkupList.id.asc();
    }
}
