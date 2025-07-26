package com.api.StringCalculator.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiExceptionResponse(
        String message,
        String details,
        HttpStatus httpStatus,
        ZonedDateTime timestamp) {
}
