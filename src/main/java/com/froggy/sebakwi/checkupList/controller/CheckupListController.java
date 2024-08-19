package com.froggy.sebakwi.checkupList.controller;

import com.froggy.sebakwi.checkupList.dto.ApiResponse;
import com.froggy.sebakwi.checkupList.dto.CheckupListDTO;
import com.froggy.sebakwi.checkupList.dto.CheckupListSearchCriteria;
import com.froggy.sebakwi.checkupList.dto.CheckupResponse;
import com.froggy.sebakwi.checkupList.dto.ZeroToNullIntegerEditor;
import com.froggy.sebakwi.checkupList.service.CheckupListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkup_list")
@RequiredArgsConstructor
@Slf4j
public class CheckupListController {

    private final CheckupListService checkupListService;

    @InitBinder("checkupListSearchCriteria")
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
        binder.registerCustomEditor(Integer.class, new ZeroToNullIntegerEditor());
    }

    @GetMapping
    public ApiResponse<CheckupListDTO> getCheckupList(
        @ModelAttribute CheckupListSearchCriteria criteria) {

        return ApiResponse.createResponse(
            checkupListService.searchCheckupList(criteria),
            CheckupListDTO::fromEntity
        );
    }

    @GetMapping("{checkupListId}")
    public List<CheckupResponse> getCheckup(@PathVariable Long checkupListId) {

        return checkupListService.findCheckupListModal(checkupListId);
    }
}
