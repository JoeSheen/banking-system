package com.sheen.joe.bankingsystem.annotation.validation;

import com.sheen.joe.bankingsystem.annotation.MinAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {

    private static final long MIN_AGE = 16;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = false;
        if (value != null) {
            return ChronoUnit.YEARS.between(value, LocalDate.now(ZoneId.of("UTC"))) >= MIN_AGE;
        }
        return valid;
    }
}
