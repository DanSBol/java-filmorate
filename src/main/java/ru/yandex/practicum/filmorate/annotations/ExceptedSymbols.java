package ru.yandex.practicum.filmorate.annotations;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ExceptedSymbolsValidator.class)
public @interface ExceptedSymbols {
    String message() default "Login should be without spaces";

    Class<?>[] groups() default {};
    Class<?>[] payload() default {};

    String value() default "^\\c";
}