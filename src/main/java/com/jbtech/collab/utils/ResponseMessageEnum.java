package com.jbtech.collab.utils;

public enum ResponseMessageEnum {
    SUCCESS("success"),
    ERROR("error"),
    WARNING("warning"),
    INFO("info");

    private final String message;

    ResponseMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
