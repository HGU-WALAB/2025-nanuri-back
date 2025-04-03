package com.walab.nanuri.commons.entity;

import lombok.Getter;

@Getter
public enum ShareStatus {
    NONE("None"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String label;

    ShareStatus(String label) {
        this.label = label;
    }
}
