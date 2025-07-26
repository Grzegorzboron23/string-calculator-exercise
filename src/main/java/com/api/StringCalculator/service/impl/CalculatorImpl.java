package com.api.StringCalculator.service.impl;

import com.api.StringCalculator.service.Calculator;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CalculatorImpl implements Calculator {

    @Override
    public Integer add(String input) {
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }

        String[] tokens = input.split("[,\n]");

        return Arrays.stream(tokens)
                .map(String::trim)
                .mapToInt(token -> {
                    try {
                        int number = Integer.parseInt(token);
                        if (number < 0) {
                            throw new IllegalArgumentException("Negative numbers not allowed: " + number);
                        }
                        return number > 1000 ? 0 : number;
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid number: " + token);
                    }
                })
                .sum();
    }
}

