package com.froggy.sebakwi.sse.Event;

import org.springframework.context.ApplicationEvent;

public class AnomalyDetectedEvent extends ApplicationEvent {

    public AnomalyDetectedEvent(Object source) {
        super(source);
    }
}