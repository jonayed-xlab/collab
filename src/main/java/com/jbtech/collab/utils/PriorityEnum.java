package com.jbtech.collab.utils;

public enum PriorityEnum {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    URGENT("URGENT"),
    BLOCKER("BLOCKER");

    private final String value;

    PriorityEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
