package com.froggy.sebakwi.checkupList.service;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.froggy.sebakwi.checkupList.repository.CheckupListQuerydslRepository;
import com.froggy.sebakwi.checkupList.repository.CheckupListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CheckupListService {

    private final CheckupListRepository checkupListRepository;
    private final CheckupListQuerydslRepository checkupListQuerydslRepository;

    public Page<CheckupList> searchCheckupList(CheckupListSearchCriteria criteria) {
        return checkupListQuerydslRepository.findCheckupListByCriteria(criteria);
    }
}
