package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.internal.constraintvalidators.bv.time.pastorpresent.PastOrPresentValidatorForDate;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
public class Film {
    private Integer id;
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    public Film() {
    }
}
