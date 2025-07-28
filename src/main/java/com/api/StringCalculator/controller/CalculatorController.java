package com.api.StringCalculator.controller;

import com.api.StringCalculator.service.Calculator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {

    private final Calculator calculator;

    public CalculatorController(Calculator calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/api/calculate")
    public Integer calculate(@RequestParam(required = false) String input){
        return calculator.add(input);
    }
}
