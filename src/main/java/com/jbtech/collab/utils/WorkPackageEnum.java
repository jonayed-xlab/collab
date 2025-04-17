package com.jbtech.collab.utils;

public enum WorkPackageEnum {
    EPIC("Epic"),
    STORY("Story"),
    TASK("Task"),
    BUG("Bug"),
    FEATURE("Feature"),
    MILLSTONE("Milestone");

    private final String value;

    WorkPackageEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
