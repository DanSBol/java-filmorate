package ru.yandex.practicum.filmorate.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExceptedSymbolsValidator implements ConstraintValidator<ExceptedSymbols, String> {

    private String exceptedSymbol;

    @Override
    public void initialize(ExceptedSymbols constraintAnnotation) {
        exceptedSymbol = String.valueOf(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !context.toString().contains(exceptedSymbol);
    }
}