package com.example.validation.validator;

import com.example.validation.annotatoin.ValidAge;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    private int minAge;

    @Override
    public void initialize(ValidAge constraintAnnotation) {
        this.minAge = constraintAnnotation.minAge();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext context) {

        if (birthDate == null) {
            return false;
        }

        return Period.between(birthDate, LocalDate.now()).getYears() >= minAge;
    }
}
