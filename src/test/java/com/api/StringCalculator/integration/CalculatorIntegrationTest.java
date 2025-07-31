package com.api.StringCalculator.integration;


import com.api.StringCalculator.StringCalculatorApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = StringCalculatorApplication.class)
@AutoConfigureMockMvc
public class CalculatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnCorrectSum_whenValidInputProvided() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("input", "1,2,3"))
                .andExpect(status().isOk())
                .andExpect(content().string("6"));
    }

    @Test
    void shouldReturnBadRequest_whenNegativeNumberProvided() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("input", "1,-2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.details").value("Negative numbers are not allowed: -2"));
    }

    @Test
    void shouldReturnZero_whenInputIsEmpty() throws Exception {
        mockMvc.perform(get("/api/calculate")
                        .param("input", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    void shouldReturnError_whenParamMissing() throws Exception {
        mockMvc.perform(get("/api/calculate"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }
}