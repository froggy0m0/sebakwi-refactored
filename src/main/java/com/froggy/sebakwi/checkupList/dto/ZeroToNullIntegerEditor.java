package com.froggy.sebakwi.checkupList.dto;

import java.beans.PropertyEditorSupport;

public class ZeroToNullIntegerEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
        } else {
            try {
                Integer value = Integer.parseInt(text);
                setValue(value == 0 ? null : value);
            } catch (NumberFormatException e) {
                setValue(null);
            }
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        return (value != null ? value.toString() : "");
    }
}
