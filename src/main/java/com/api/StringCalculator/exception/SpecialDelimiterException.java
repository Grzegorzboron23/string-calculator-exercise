package com.api.StringCalculator.exception;

public record SpecialDelimiterException(
        String expectedChar,
        String wrongChar,
        Integer index
) {}
