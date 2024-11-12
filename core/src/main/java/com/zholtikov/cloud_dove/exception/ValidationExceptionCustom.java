package com.zholtikov.cloud_dove.exception;

public class ValidationExceptionCustom extends RuntimeException {
    public ValidationExceptionCustom(String message) {
        super(message);
    }
}
