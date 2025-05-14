package com.jbtech.collab.utils;

public enum ProjectStatusEnum {
    NEW("New"),
    ON_TRACK("On Track"),
    COMPLETED("Completed"),
    AT_RISK("At Risk"),
    OFF_TRACK("Off Track");

    private final String value;

    ProjectStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
