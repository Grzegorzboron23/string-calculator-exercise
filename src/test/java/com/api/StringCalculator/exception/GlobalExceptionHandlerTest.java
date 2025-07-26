package com.api.StringCalculator.exception;

import com.api.StringCalculator.constant.ErrorType;
import com.api.StringCalculator.controller.CalculatorController;
import com.api.StringCalculator.service.Calculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CalculatorController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Calculator calculator;

    @Test
    void handleStringCalculatorException_shouldReturnBadRequest_whenCustomExceptionIsThrown() throws Exception {
        // given
        List<CalculatorError> errors = List.of(new CalculatorError(ErrorType.NEGATIVE_NUMBER, "-2"));
        Mockito.when(calculator.add(Mockito.anyString()))
                .thenThrow(new StringCalculatorException(errors));

        String expectedMessage = ErrorType.NEGATIVE_NUMBER.getDefaultMessage() + ": -2";

        // when & then
        mockMvc.perform(get("/api/calculate").param("input", "1,-2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.details").value(expectedMessage))
                .andExpect(jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError_whenGenericExceptionIsThrown() throws Exception {
        // given
        Mockito.when(calculator.add(Mockito.anyString()))
                .thenThrow(new NullPointerException("BOOM"));

        // when & then
        mockMvc.perform(get("/api/calculate").param("input", "$$%%^"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error occurred"))
                .andExpect(jsonPath("$.details").value("BOOM"))
                .andExpect(jsonPath("$.httpStatus").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}