package com.sheen.joe.bankingsystem.annotation;

import com.sheen.joe.bankingsystem.annotation.validation.PasswordCriteriaValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = PasswordCriteriaValidator.class)
public @interface ValidPassword {

    String message() default "com.sheen.joe.bankingsystem.annotation.ValidPassword.message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
