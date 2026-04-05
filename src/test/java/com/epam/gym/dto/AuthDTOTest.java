package com.epam.gym.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        AuthDTO dto = new AuthDTO("john", "password123");

        Set<ConstraintViolation<AuthDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void nullUsername_shouldFail() {
        AuthDTO dto = new AuthDTO(null, "password123");

        Set<ConstraintViolation<AuthDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals("Username cannot be empty or null",
                violations.iterator().next().getMessage());
    }

    @Test
    void blankUsername_shouldFail() {
        AuthDTO dto = new AuthDTO("   ", "password123");

        Set<ConstraintViolation<AuthDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }

    @Test
    void nullPassword_shouldFail() {
        AuthDTO dto = new AuthDTO("john", null);

        Set<ConstraintViolation<AuthDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertEquals("Password cannot be empty or null",
                violations.iterator().next().getMessage());
    }

    @Test
    void blankPassword_shouldFail() {
        AuthDTO dto = new AuthDTO("john", "   ");

        Set<ConstraintViolation<AuthDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
    }
}