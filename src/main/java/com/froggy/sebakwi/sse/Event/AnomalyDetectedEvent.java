package com.froggy.sebakwi.sse.Event;

import com.froggy.sebakwi.checkupList.domain.CheckupList;
import org.springframework.context.ApplicationEvent;

public class AnomalyDetectedEvent extends ApplicationEvent {
    private final CheckupList checkupList;

    public AnomalyDetectedEvent(Object source, CheckupList checkupList) {
        super(source);
        this.checkupList = checkupList;
    }

    public CheckupList getCheckupList() {
        return checkupList;
    }
}