package com.froggy.sebakwi.checkupList.repository;

import static com.froggy.sebakwi.wheel.domain.WheelStatus.ABNORMAL;
import static com.froggy.sebakwi.wheel.domain.WheelStatus.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.froggy.sebakwi.checkupList.service.CheckupListService;
import com.froggy.sebakwi.oht.repository.OhtRepository;
import com.froggy.sebakwi.wheel.domain.WheelStatus;
import com.froggy.sebakwi.wheel.repository.WheelRepository;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@Sql(scripts = "/dummy.sql")
class CheckupListQuerydslRepositoryTest {

    @Autowired
    CheckupListRepository checkupListRepository;
    @Autowired
    CheckupListQuerydslRepository checkupListQuerydslRepository;
    @Autowired
    CheckupListService checkupListService;
    @Autowired
    OhtRepository ohtRepository;
    @Autowired
    WheelRepository wheelRepository;


    static private final LocalDate TEST_START_DATE = LocalDate.of(2023, 1, 1);
    static private final LocalDate TEST_END_DATE = LocalDate.of(2023, 12, 31);
    static private final Boolean TEST_ONLY_ABNORMAL = true;
    static private final Boolean TEST_ABNORMAL_AND_NORMAL = false;
    static private final Integer TEST_POSITION = 1;
    static private final String TEST_OHT_SERIAL_NUMBER = "VM0003";
    static private final String TEST_WHEEL_SERIAL_NUMBER = "SM00016";

    static private final int EXPECTED_PERIOD_COUNT = 12;
    static private final int EXPECTED_ABNORMAL_COUNT = 3;
    static private final int EXPECTED_NORMAL_COUNT = 3;
    static private final int EXPECTED_POSITION_COUNT = 3;
    static private final int EXPECTED_WHEEL_SERIAL_COUNT = 3;
    static private final int EXPECTED_OHT_SERIAL_COUNT = 3;
    static private final String IMAGE_URL = "https://raw.githubusercontent.com/froggy0m0/froggy0m0.github.io/main/assets/img/avatar.png";

    @Test
    public void 조건검색_기간()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        // 더미데이터 추가
        createDummyDataForPeriodSearch();

        // 검색 조건 설정
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();

        criteria.setStartDateTime(TEST_START_DATE);
        criteria.setEndDateTime(TEST_END_DATE);

        // 데이터 검색
        Page<CheckupList> checkupListByCriteria =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);

        List<CheckupList> content = checkupListByCriteria.getContent();

        assertThat(content.size()).isEqualTo(EXPECTED_PERIOD_COUNT);
    }

    @Test
    public void 조건검색_상태()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 더미데이터 추가
        createDummyDataForStatusSearch();

        // 검색 조건 설정
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();
        criteria.setOnlyAbnormal(TEST_ONLY_ABNORMAL);

        // 비정상 데이터 검색
        Page<CheckupList> abnormalCheckList =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);

        List<CheckupList> abnormalContent = abnormalCheckList.getContent();

        assertThat(abnormalContent.size()).isEqualTo(EXPECTED_ABNORMAL_COUNT);

        abnormalContent.stream()
            .forEach(checkup -> assertThat(checkup.getStatus()).isEqualTo(WheelStatus.ABNORMAL));

        // 비정상 + 정상 데이터 검색
        criteria.setOnlyAbnormal(TEST_ABNORMAL_AND_NORMAL);
        Page<CheckupList> normalCheckList =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);

        List<CheckupList> normalContent = normalCheckList.getContent();
        assertThat(normalContent.size()).
            isEqualTo(EXPECTED_ABNORMAL_COUNT + EXPECTED_NORMAL_COUNT);
    }

    @Test
    public void 조건검색_바퀴_위치()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // 더미데이터 추가
        createDummyDataForWheelPositionSearch();

        // 검색 조건 설정
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();
        criteria.setPosition(TEST_POSITION);

        // 데이터 검색
        Page<CheckupList> checkupListByCriteria =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);

        List<CheckupList> content = checkupListByCriteria.getContent();

        assertThat(content.size()).isEqualTo(EXPECTED_POSITION_COUNT);
    }

    @Test
    public void 조건검색_OHT_시리얼_넘버()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        createDummyDataForOhtSerialNumberSearch();

        // 검색 조건 설정
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();
        criteria.setOhtSerialNumber(TEST_OHT_SERIAL_NUMBER);

        // 데이터 검색
        Page<CheckupList> checkupListByCriteria =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
        List<CheckupList> content = checkupListByCriteria.getContent();

        // 갯수 확인
        assertThat(content.size()).isEqualTo(EXPECTED_OHT_SERIAL_COUNT);

        // 시리얼 넘버 확인
        content.stream()
            .forEach(checkupList -> assertThat(
                checkupList.getWheel().getOht().getSerialNumber()).isEqualTo(
                TEST_OHT_SERIAL_NUMBER));
    }

    @Test
    public void 조건검색_바퀴_시리얼_넘버()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        createDummyDataForWheelSerialNumberSearch();

        // 검색 조건 설정
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();
        criteria.setWheelSerialNumber(TEST_WHEEL_SERIAL_NUMBER);

        // 데이터 검색
        Page<CheckupList> checkupListByCriteria =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
        List<CheckupList> content = checkupListByCriteria.getContent();

        // 갯수확인
        assertThat(content.size()).isEqualTo(EXPECTED_WHEEL_SERIAL_COUNT);

        // 시리얼 넘버 확인
        content.stream()
            .forEach(checkupList -> assertThat(checkupList.getWheel().getSerialNumber()).isEqualTo(
                TEST_WHEEL_SERIAL_NUMBER));
    }

    @Test
    public void 조건검색_모든_조건()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        createDummyDataForAllConditions();

        /**
         * 페이징 처리
         */
        CheckupListSearchCriteria criteria = getCheckupListSearchCriteria();
        Page<CheckupList> findResult =
            checkupListQuerydslRepository.findCheckupListByCriteria(criteria);

        System.out.println("Total Elements: " + findResult.getTotalElements());
        System.out.println("Total Pages: " + findResult.getTotalPages());
        System.out.println("Number of Elements: " + findResult.getNumberOfElements());
        System.out.println("Page Number: " + findResult.getNumber());
        System.out.println("Page Size: " + findResult.getSize());


        /**
         * 조건
         * 1. 기간
         * 2. 바퀴위치
         * 3. 비정상 상태
         */


        criteria.setStartDateTime(TEST_START_DATE);
        criteria.setEndDateTime(TEST_END_DATE);

        criteria.setPosition(TEST_POSITION);

        criteria.setOnlyAbnormal(TEST_ONLY_ABNORMAL);

        findResult = checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
        List<CheckupList> content = findResult.getContent();


        content.stream()
            .forEach(item -> {
                assertThat(isBetweenPeriod(item.getCheckedDate())).isEqualTo(true);
                assertThat(item.getWheel().getPosition()).isEqualTo(TEST_POSITION);
                assertThat(item.getStatus()).isEqualTo(ABNORMAL);
            });

        assertThat(content.size()).isEqualTo(EXPECTED_POSITION_COUNT);


        /**
         * 조건
         * 1. 기간
         * 2. OHT 시리얼 넘버
         * 3. 비정상 상태
         */
        criteria.setPosition(null);

        criteria.setOhtSerialNumber(TEST_OHT_SERIAL_NUMBER);

        criteria.setOnlyAbnormal(TEST_ONLY_ABNORMAL);

        findResult = checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
        content = findResult.getContent();

        assertThat(content.size()).isEqualTo(EXPECTED_OHT_SERIAL_COUNT);
        content.stream()
            .forEach(item -> {
                assertThat(isBetweenPeriod(item.getCheckedDate())).isEqualTo(true);
                assertThat(item.getWheel().getOht().getSerialNumber()).isEqualTo(TEST_OHT_SERIAL_NUMBER);
                assertThat(item.getStatus()).isEqualTo(ABNORMAL);
            });

        /**
         * 조건
         * 1. 기간
         * 2. Wheel 시리얼 넘버
         * 3. 비정상 상태
         */
        criteria.setPosition(null);
        criteria.setOhtSerialNumber(null);

        criteria.setWheelSerialNumber(TEST_WHEEL_SERIAL_NUMBER);

        criteria.setOnlyAbnormal(TEST_ONLY_ABNORMAL);

        findResult = checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
        content = findResult.getContent();

        assertThat(content.size()).isEqualTo(EXPECTED_WHEEL_SERIAL_COUNT);
        content.stream()
            .forEach(item -> {
                assertThat(isBetweenPeriod(item.getCheckedDate())).isEqualTo(true);
                assertThat(item.getWheel().getSerialNumber()).isEqualTo(TEST_WHEEL_SERIAL_NUMBER);
                assertThat(item.getStatus()).isEqualTo(ABNORMAL);
            });
    }


    private static CheckupListSearchCriteria getCheckupListSearchCriteria()
        throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<CheckupListSearchCriteria> constructor = CheckupListSearchCriteria.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        CheckupListSearchCriteria criteria = constructor.newInstance();

        criteria.setDesc(true);

        return criteria;
    }

    private void createDummyData(int count, WheelStatus status, LocalDate startDate) {
        IntStream.rangeClosed(1, count).forEach(i -> saveCheckupList((long) i, startDate.plusMonths(i - 1), status));
    }

    private void createDummyDataForPeriodSearch() {
        final int EXTRA_COUNT = 2;
        createDummyData(EXPECTED_PERIOD_COUNT + EXTRA_COUNT, NORMAL, TEST_START_DATE);
    }

    private void createDummyDataForStatusSearch() {
        IntStream.rangeClosed(1, EXPECTED_ABNORMAL_COUNT + EXPECTED_NORMAL_COUNT).forEach(i -> {
            WheelStatus status = (i % 2 == 0) ? WheelStatus.NORMAL : WheelStatus.ABNORMAL;
            saveCheckupList((long) i, LocalDate.now(), status);
        });
    }

    private void createDummyDataForWheelPositionSearch() {
        createDummyData(EXPECTED_POSITION_COUNT * 4, NORMAL, LocalDate.now());
    }

    private void createDummyDataForOhtSerialNumberSearch() {
        List<Long> wheelIds = List.of(10L, 11L, 12L, 5L, 6L, 7L);
        IntStream.range(0, wheelIds.size())
            .forEach(i -> saveCheckupList(wheelIds.get(i), LocalDate.now(), NORMAL));
    }

    private void createDummyDataForWheelSerialNumberSearch() {
        List<Long> wheelIds = List.of(16L, 16L, 16L, 5L, 6L, 7L);
        IntStream.range(0, wheelIds.size())
            .forEach(i -> saveCheckupList(wheelIds.get(i), LocalDate.now(), NORMAL));
    }



    private boolean isBetweenPeriod(LocalDate date) {
        return (date.isEqual(TEST_START_DATE) || date.isAfter(TEST_START_DATE))
            && (date.isEqual(TEST_END_DATE) || date.isBefore(TEST_END_DATE));
    }
    private void createDummyDataForAllConditions() {
        // 5개
        saveCheckupList(3L, LocalDate.of(2022, 5, 1), NORMAL);
        saveCheckupList(3L, LocalDate.of(2022, 6, 1), NORMAL);
        saveCheckupList(3L, LocalDate.of(2022, 7, 1), NORMAL);
        saveCheckupList(3L, LocalDate.of(2022, 8, 1), NORMAL);
        saveCheckupList(3L, LocalDate.of(2022, 9, 1), NORMAL);

        // 3개
        // EXPECTED_ABNORMAL_COUNT = 3
        saveCheckupList(3L, TEST_START_DATE.plusMonths(1), ABNORMAL);
        saveCheckupList(3L, TEST_START_DATE.plusMonths(2), ABNORMAL);
        saveCheckupList(3L, TEST_START_DATE.plusMonths(3), ABNORMAL);


        // 3개
        // EXPECTED_POSITION_COUNT = 3
        Queue<Long> positionQ = createQueue(13L, 17L, 49L);
        saveCheckupList(positionQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);
        saveCheckupList(positionQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);
        saveCheckupList(positionQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);


        // EXPECTED_OHT_SERIAL_COUNT = 3
        Queue<Long> ohtQ = createQueue(10L, 11L, 12L, 11L, 11L, 72L);
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.plusMonths(1), ABNORMAL);
        // 더미데이터
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.plusMonths(1), NORMAL);
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.minusMonths(1), NORMAL);
        saveCheckupList(ohtQ.poll(), TEST_START_DATE.minusMonths(2), ABNORMAL);

        // 3개
        Queue<Long> WheelQ = createQueue(16L, 16L, 16L, 16L, 16L, 7L);
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.plusMonths(2), ABNORMAL);
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.plusMonths(3), ABNORMAL);
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.plusMonths(3), ABNORMAL);
        // 더미데이터
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.plusMonths(1), NORMAL);
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.minusMonths(1), NORMAL);
        saveCheckupList(WheelQ.poll(), TEST_START_DATE.minusMonths(2), ABNORMAL);


    }

    private Queue<Long> createQueue(long... wheelIds) {
        Queue<Long> queue = new ArrayDeque<>();

        Arrays.stream(wheelIds)
            .forEach(queue::add);

        return queue;
    }

    private void saveCheckupList(Long wheelId, LocalDate checkedDate, WheelStatus status) {
        CheckupList checkupList = CheckupList.builder()
            .wheel(wheelRepository.findById(wheelId).get())
            .checkedDate(checkedDate)
            .wheelImage(IMAGE_URL)
            .status(status)
            .diameter(0.1)
            .crack(false)
            .stamp(false)
            .abrasion(false)
            .build();
        checkupListRepository.save(checkupList);
    }
}
