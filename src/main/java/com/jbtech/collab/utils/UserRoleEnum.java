package com.jbtech.collab.utils;

public enum UserRoleEnum {
    DEVELOPER("Developer"),
    QUALITY_ASSURANCE("Quality Assurance"),
    PROJECT_MANAGER("Project Manager"),
    ADMIN("Admin");
    private final String value;

    UserRoleEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
