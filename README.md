# 🧮 String Calculator

A simple Spring Boot REST API implementing a string-based calculator as described in the **SmartBear** recruitment task.

---

## ✅ Implemented Requirements

- [x] Supports up to 2 or more numbers separated by commas
- [x] Returns `0` for empty input (`""`)
- [x] Handles an unknown number of inputs
- [x] Accepts newline characters as delimiters (`1\n2,3` → `6`)
- [x] Custom delimiters using the `//[delimiter]\n[numbers]` syntax
  - Examples: `//;\n1;2`, `//|\n1|2|3`
- [x] Throws an error if input ends with a delimiter (e.g. `"1,2,"`)
- [x] Throws an error for invalid delimiters (`//|\n1|2,3`)
  - Returns message: `"'|’ expected but ‘,’ found at position 3."`
- [x] Validates negative numbers
  - Example: `"1,-2"` → `"Negative number(s) not allowed: -2"`
  - Multiple negatives → `"Negative number(s) not allowed: -4, -9"`
- [x] Supports multiple errors in a single response
- [x] Ignores numbers greater than 1000 (e.g. `"2,1001"` → `2`)
- [x] Logs all exceptions (with stack traces) to file `logs/errors.log`
- [x] Integrated Swagger UI for easy API testing

---

## ▶️ How to Run

```bash
mvn spring-boot:run
```

App runs on port `8080` by default.

#### ➕ Calculator Endpoint

```
GET http://localhost:8080/api/calculate?input=1,2,3
```
---

## 🧪 Tests

Includes full test coverage:

- `CalculatorImplTest` – unit tests for calculator logic
- `CalculatorControllerTest` – unit tests for REST controller
- `GlobalExceptionHandlerTest` – tests for exception handling
- `CalculatorIntegrationTest` – integration tests over HTTP

In addition to automated tests, the API was manually tested using **Postman** to verify behavior with various input scenarios and edge cases.

To run tests:

```bash
mvn test
```

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 3.5.4
- JUnit 5, Mockito
- SLF4J + Logback

---

> 🔍 Note: This project was created as part of the **SmartBear** recruitment process.
