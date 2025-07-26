package com.api.StringCalculator.service.impl;

import com.api.StringCalculator.constant.ErrorType;
import com.api.StringCalculator.exception.CalculatorError;
import com.api.StringCalculator.exception.StringCalculatorException;
import com.api.StringCalculator.service.Calculator;
import com.api.StringCalculator.utils.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CalculatorImpl implements Calculator {

    @Override
    public Integer add(String input) {
        List<CalculatorError> errors = new ArrayList<>();

        if (ValidationUtils.isNullOrBlank(input)) {
            return 0;
        }

        String[] tokens = input.split("[,\n]");

        int sum = Arrays.stream(tokens)
                .map(String::trim)
                .map(token -> tryParseNumber(token, errors))
                .filter(Optional::isPresent)
                .mapToInt(Optional::get)
                .sum();

        if (!errors.isEmpty()) {
            throw new StringCalculatorException(errors);
        }

        return sum;
    }

    private Optional<Integer> tryParseNumber(String token, List<CalculatorError> errors) {
        if (token.isEmpty()) {
            errors.add(new CalculatorError(ErrorType.EMPTY_VALUE, "Empty value found"));
            return Optional.empty();
        }

        try {
            int number = Integer.parseInt(token);
            if (number < 0) {
                errors.add(new CalculatorError(ErrorType.NEGATIVE_NUMBER, token));
                return Optional.empty();
            }
            return number > 1000 ? Optional.of(0) : Optional.of(number);
        } catch (NumberFormatException e) {
            errors.add(new CalculatorError(ErrorType.INVALID_NUMBER, token));
            return Optional.empty();
        }
    }
}
