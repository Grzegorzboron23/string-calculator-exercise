package com.api.StringCalculator.exception;

import com.api.StringCalculator.constant.ErrorType;

public record CalculatorError(
        ErrorType type,
        String value) {
}
