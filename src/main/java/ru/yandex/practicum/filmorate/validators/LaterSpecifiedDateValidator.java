package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LaterSpecifiedDateValidator implements ConstraintValidator <LaterSpecifiedDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        return releaseDate.isAfter(LocalDate.of(1895,12,28));
    }
}
