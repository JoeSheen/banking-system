package com.sheen.joe.bankingsystem.annotation;

import com.sheen.joe.bankingsystem.annotation.validation.MinAgeValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MinAgeTest {

    @Mock
    private MinAge minAge;

    @Mock
    private ConstraintValidatorContext context;

    private MinAgeValidator minAgeValidator;

    @BeforeEach
    void setUp() {
        minAgeValidator = new MinAgeValidator();
    }

    @Test
    void testIsValidReturnsTrue() {
        LocalDate date = LocalDate.now().minus(Period.ofYears(16));

        minAgeValidator.initialize(minAge);

        boolean result = minAgeValidator.isValid(date, context);
        assertTrue(result);
    }

    @Test
    void testIsValidReturnsFalse() {
        LocalDate date = LocalDate.now();

        minAgeValidator.initialize(minAge);

        boolean result = minAgeValidator.isValid(date, context);
        assertFalse(result);
    }

}
