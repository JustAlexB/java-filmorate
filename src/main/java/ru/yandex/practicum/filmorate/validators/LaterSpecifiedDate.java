package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = LaterSpecifiedDateValidator.class)
@Documented
public @interface LaterSpecifiedDate {
    String message() default "Дата релиза не должна быть ранее 28.12.1895 года";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
