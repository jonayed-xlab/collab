package com.jbtech.collab.utils;

public enum StatusEnum {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    ON_HOLD("On Hold"),
    SCHEDULED_TODO("Scheduled To Do"),
    DEVELOPED("Developed"),
    PR_REVIEW("PR Review"),
    READY_FOR_TESTING("Ready for Testing"),
    READY_FOR_UAT("Ready for UAT"),
    IN_UAT("In UAT"),
    TESTED("Tested"),
    READY_FOR_DEPLOYMENT("Ready for Deployment"),
    CLOSED("Closed"),
    REJECTED("Rejected"),;

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
