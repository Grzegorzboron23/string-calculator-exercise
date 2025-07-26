package com.api.StringCalculator.utils;

public class ValidationUtils {

    public static boolean isNullOrBlank(String input) {
        return input == null || input.trim().isEmpty();
    }
}
