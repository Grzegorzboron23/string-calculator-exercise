package com.api.StringCalculator.service.impl;

import com.api.StringCalculator.constant.ErrorType;
import com.api.StringCalculator.exception.CalculatorError;
import com.api.StringCalculator.exception.SpecialDelimiterException;
import com.api.StringCalculator.exception.StringCalculatorException;
import com.api.StringCalculator.model.DelimiterData;
import com.api.StringCalculator.service.Calculator;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Service
public class CalculatorImpl implements Calculator {

    private static final String DEFAULT_SEPARATOR = ",";
    private static final String CUSTOM_SEPARATOR_PREFIX = "//";

    @Override
    public Integer add(String input) throws StringCalculatorException {
        if (StringUtils.isBlank(input)) {
            return 0;
        }

        List<CalculatorError> errors = new ArrayList<>();
        collectNegativeNumbers(input, errors);

        DelimiterData delimiter = getDelimiterData(input, errors);
        String numbers = extractNumbers(input);

        if (numbers.endsWith(delimiter.literal())) {
            errors.add(new CalculatorError(ErrorType.WRONG_SEPARATOR, delimiter.literal()));
        }

        String[] tokens = numbers.split(delimiter.regex());
        int sum = Arrays.stream(tokens)
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
        token = token.trim();

        if (token.isEmpty()) {
            errors.add(new CalculatorError(ErrorType.EMPTY_VALUE, ""));
            return Optional.empty();
        }

        try {
            int number = Integer.parseInt(token);
            if (number < 0) {
                return Optional.empty();
            }
            if (number > 1000) {
                return Optional.of(0);
            }
            return Optional.of(number);
        } catch (NumberFormatException e) {
            errors.add(new CalculatorError(ErrorType.INVALID_NUMBER, token));
            return Optional.empty();
        }
    }

    private DelimiterData getDelimiterData(String input, List<CalculatorError> errors) {
        if (!input.startsWith(CUSTOM_SEPARATOR_PREFIX)) {
            return new DelimiterData(",|\n", DEFAULT_SEPARATOR);
        }

        int commaIndex = input.indexOf(',');
        if (commaIndex > 0) {
            throw new StringCalculatorException(errors, new SpecialDelimiterException(input.substring(2, 3), ",", input.indexOf(',')));
        }

        String literal = input.substring(2, input.indexOf('\n'));
        return new DelimiterData(Pattern.quote(literal), literal);
    }

    private String extractNumbers(String input) {
        if (!input.startsWith(CUSTOM_SEPARATOR_PREFIX)) {
            return input;
        }
        int newlineIndex = input.indexOf('\n');
        return input.substring(newlineIndex + 1);
    }

    private void collectNegativeNumbers(String input, List<CalculatorError> errors) {
        Pattern negativePattern = Pattern.compile("-\\d+");
        negativePattern.matcher(input)
                .results()
                .map(MatchResult::group)
                .forEach(neg -> errors.add(new CalculatorError(ErrorType.NEGATIVE_NUMBER, neg)));
    }
}
