package com.froggy.sebakwi.sse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/wheels/")
@RequiredArgsConstructor
@Slf4j
public class SseController {

    private final SseUtils SseUtils;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 5;
    private static final String CONNECTION_MESSAGE = "연결되었습니다 \uD83D\uDE80";

    @GetMapping(value = "/monthly/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter();
        SseUtils.add(emitter);

        SseUtils.send(emitter, CONNECTION_MESSAGE);
        return emitter;
    }
}
