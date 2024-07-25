package com.froggy.sebakwi.oht.controller;

import com.froggy.sebakwi.oht.service.OhtService;
import com.froggy.sebakwi.oht.dto.OHTStatusCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oht")
@RequiredArgsConstructor
@Slf4j
public class OhtController {

    private final OhtService ohtService;

    @GetMapping("/replacement")
    public OHTStatusCount getOHTStatusCounts() {
        return ohtService.findOhtStatusCounts();
    }
}
