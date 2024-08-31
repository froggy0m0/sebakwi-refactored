package com.froggy.sebakwi.sse.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseUtils {

    private final ObjectMapper objectMapper;

    private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter add(SseEmitter emitter) {
        emitters.add(emitter);

        emitter.onCompletion(() -> {
            emitters.remove(emitter);
        });

        emitter.onTimeout(() -> {
            emitter.complete();
        });

        return emitter;
    }

    public void send(SseEmitter emitter, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .name("sse")
                .data(translateJson(data)));
        } catch (IOException e) {
            emitters.remove(emitter);
        }
    }

    public void sendAll(Object data) {
        log.info("현재 SSE 연결 {}개", emitters.size());
        emitters.forEach(e -> send(e, data));
    }

    public String translateJson(Object data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
