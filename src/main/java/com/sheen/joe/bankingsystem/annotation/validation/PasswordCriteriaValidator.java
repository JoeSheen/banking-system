package com.sheen.joe.bankingsystem.annotation.validation;

import com.sheen.joe.bankingsystem.annotation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.passay.*;

public class PasswordCriteriaValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator passwordValidator = new PasswordValidator(
                new LengthRule(8, 50),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule(),
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, true),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, true),
                new IllegalSequenceRule(EnglishSequenceData.USQwerty, 3, true)
        );
        RuleResult ruleResult = passwordValidator.validate(new PasswordData(value));
        if (!ruleResult.isValid()) {
            String validatorMessage = String.join(",", passwordValidator.getMessages(ruleResult));
            context.buildConstraintViolationWithTemplate(validatorMessage)
                    .addConstraintViolation().disableDefaultConstraintViolation();
            return false;
        }
        return true;
    }
}
