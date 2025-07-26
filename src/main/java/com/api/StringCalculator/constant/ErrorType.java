package com.api.StringCalculator.constant;


public enum ErrorType {

    EMPTY_VALUE("Value cannot be empty"),
    INVALID_NUMBER("Invalid number"),
    NEGATIVE_NUMBER("Negative numbers are not allowed");

    private final String defaultMessage;

    ErrorType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
