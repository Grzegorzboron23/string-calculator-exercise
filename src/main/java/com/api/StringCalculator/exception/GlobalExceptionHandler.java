package com.api.StringCalculator.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StringCalculatorException.class)
    public ResponseEntity<ApiExceptionResponse> handleStringCalculatorException(StringCalculatorException ex) {
        log.warn("Handled StringCalculatorException: {}", ex.format(), ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                "Validation error",
                ex.format(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        ApiExceptionResponse response = new ApiExceptionResponse(
                "Unexpected error occurred",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

