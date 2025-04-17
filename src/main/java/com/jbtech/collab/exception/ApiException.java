package com.jbtech.collab.exception;

import lombok.Data;

@Data
public class ApiException extends RuntimeException {

    private final String statusCode;

    public ApiException(String statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}