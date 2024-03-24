package com.sheen.joe.bankingsystem.annotation;

import com.sheen.joe.bankingsystem.annotation.validation.MinAgeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
public @interface MinAge {
    String message() default "com.sheen.joe.bankingsystem.annotation.MinAge.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
