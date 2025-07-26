package com.api.StringCalculator.service.impl;

import com.api.StringCalculator.constant.ErrorType;
import com.api.StringCalculator.exception.CalculatorError;
import com.api.StringCalculator.exception.StringCalculatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalculatorImplTest {

    private final CalculatorImpl calculator = new CalculatorImpl();

    @Test
    public void shouldReturnZeroForEmptyString() throws StringCalculatorException {
        assertEquals(0, calculator.add(""));
    }

    @Test
    public void shouldReturnZeroForNull() throws StringCalculatorException {
        assertEquals(0, calculator.add(null));
    }

    @Test
    public void shouldReturnSumForCommaSeparatedValues() throws StringCalculatorException {
        assertEquals(6, calculator.add("1,2,3"));
    }

    @Test
    public void shouldHandleNewlineAsSeparator() throws StringCalculatorException {
        assertEquals(6, calculator.add("1\n2,3"));
    }

    @Test
    public void shouldSupportCustomDelimiter() throws StringCalculatorException {
        assertEquals(3, calculator.add("//;\n1;2"));
    }

    @Test
    public void shouldThrowErrorForNegativeNumbers() {
        String input = "1,-2,3,-5";
        StringCalculatorException ex = assertThrows(StringCalculatorException.class, () -> calculator.add(input));
        List<CalculatorError> errors = ex.getErrors();
        assertEquals(2, errors.size());
        assertTrue(errors.stream().allMatch(e -> e.type() == ErrorType.NEGATIVE_NUMBER));
    }

    @Test
    public void shouldIgnoreNumbersGreaterThan1000() throws StringCalculatorException {
        assertEquals(2, calculator.add("2,1001"));
        assertEquals(1002, calculator.add("2,1000"));
    }

    @Test
    public void shouldThrowErrorIfInputEndsWithSeparator() {
        String input = "//;\n1;2;";
        StringCalculatorException ex = assertThrows(StringCalculatorException.class, () -> calculator.add(input));
        assertEquals(1, ex.getErrors().size());
        assertEquals(ErrorType.WRONG_SEPARATOR, ex.getErrors().getFirst().type());
    }

    @Test
    public void shouldThrowErrorOnEmptyValue() {
        String input = "1,,2";
        StringCalculatorException ex = assertThrows(StringCalculatorException.class, () -> calculator.add(input));
        assertEquals(1, ex.getErrors().size());
        assertEquals(ErrorType.EMPTY_VALUE, ex.getErrors().getFirst().type());
    }

    @Test
    public void shouldThrowErrorOnInvalidNumber() {
        String input = "1,a,3";
        StringCalculatorException ex = assertThrows(StringCalculatorException.class, () -> calculator.add(input));
        assertEquals(1, ex.getErrors().size());
        assertEquals(ErrorType.INVALID_NUMBER, ex.getErrors().getFirst().type());
    }

    @Test
    void shouldHandleLargeNumberOfValues() throws StringCalculatorException {
        String input = String.join(",", Collections.nCopies(1000, "1"));
        assertEquals(1000, calculator.add(input));
    }

    @Test
    public void shouldSupportMultiDigitNumbers() throws StringCalculatorException {
        assertEquals(123 + 456 + 789, calculator.add("123,456,789"));
    }

    @Test
    public void shouldThrowErrorIfMissingNewlineAfterCustomDelimiter() {
        String input = "//;\n";
        StringCalculatorException ex = assertThrows(StringCalculatorException.class, () -> calculator.add(input + "1;2;"));
        assertEquals(1, ex.getErrors().size());
        assertEquals(ErrorType.WRONG_SEPARATOR, ex.getErrors().getFirst().type());
    }

    @Test
    public void shouldSupportSpaceAroundNumbers() throws StringCalculatorException {
        assertEquals(6, calculator.add(" 1 , 2 , 3 "));
    }

    @Test
    public void shouldSupportSingleNumber() throws StringCalculatorException {
        assertEquals(7, calculator.add("7"));
    }

    @Test
    public void shouldSupportCustomDelimiterWithSpecialRegexChars() throws StringCalculatorException {
        assertEquals(10, calculator.add("//.\n4.6"));
        assertEquals(11, calculator.add("//|\n5|6"));
    }

    @ParameterizedTest
    @CsvSource({
            "'', 0",
            "'1', 1",
            "'1,2,3', 6",
            "'1\n2,3', 6",
            "'//;\n1;2', 3",
            "'2,1001', 2",
            "'2,1000', 1002",
            "' 1 , 2 , 3 ', 6",
            "'123,456,789', 1368"
    })
    void shouldReturnExpectedSum(String input, int expected) throws StringCalculatorException {
        assertEquals(expected, calculator.add(input));
    }
}

