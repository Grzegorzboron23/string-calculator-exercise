package com.api.StringCalculator.controller;

import com.api.StringCalculator.service.Calculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@WebMvcTest(CalculatorController.class)
public class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private Calculator calculator;

    @Test
    void shouldReturnSumWhenInputIsValid() throws Exception {
        // given
        Mockito.when(calculator.add("1,2,3")).thenReturn(6);

        // when + then
        mockMvc.perform(get("/api/calculate")
                        .param("input", "1,2,3"))
                .andExpect(status().isOk())
                .andExpect(content().string("6"));
    }

    @Test
    void shouldReturnBadRequestOnMissingInput() throws Exception {
        mockMvc.perform(get("/api/calculate"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnErrorFromService() throws Exception {
        Mockito.when(calculator.add("1,-2")).thenThrow(new RuntimeException("Boom"));

        mockMvc.perform(get("/api/calculate").param("input", "1,-2"))
                .andExpect(status().isInternalServerError());
    }
}
