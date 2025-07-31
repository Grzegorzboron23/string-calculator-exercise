package com.api.StringCalculator.constant;


import lombok.Getter;

@Getter
public enum ErrorType {

    EMPTY_VALUE("Value cannot be empty"),
    INVALID_NUMBER("Invalid number"),
    NEGATIVE_NUMBER("Negative number(s) not allowed"),
    WRONG_SEPARATOR("Wrong separator");

    private final String defaultMessage;

    ErrorType(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
