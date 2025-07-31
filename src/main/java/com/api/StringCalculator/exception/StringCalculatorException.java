package com.api.StringCalculator.exception;

import com.api.StringCalculator.constant.ErrorType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
public class StringCalculatorException extends RuntimeException {

    private final List<CalculatorError> errors;
    private SpecialDelimiterException specialError;


    public StringCalculatorException(List<CalculatorError> errors) {
        this.errors = errors;
    }

    public StringCalculatorException(List<CalculatorError> errors, SpecialDelimiterException specialError) {
        this.errors = errors;
        this.specialError = specialError;
    }


    public String format() {
        Map<ErrorType, List<String>> grouped = new LinkedHashMap<>();
        for (CalculatorError error : errors) {
            grouped.computeIfAbsent(error.type(), k -> new ArrayList<>()).add(error.value());
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<ErrorType, List<String>> entry : grouped.entrySet()) {
            sb.append(entry.getKey().getDefaultMessage())
                    .append(": ")
                    .append(String.join(", ", entry.getValue()))
                    .append("\n");
        }

        sb.append(specialError != null ? buildSpecialMessage(specialError) : "");
        return sb.toString().trim();
    }

    private String buildSpecialMessage(SpecialDelimiterException error) {
        return error.expectedChar() + "  expected but " + " " + error.wrongChar() + " found at position " + error.index();
    }
}