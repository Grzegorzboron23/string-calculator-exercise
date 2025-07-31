package com.api.StringCalculator.exception;

public record SpecialDelimeterException(
        String expectedChar,
        String wrongChar,
        Integer index
) {}
