package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.LaterSpecifiedDate;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
public class Film {
    private Integer id;
    @NotNull(message = "Название фильма не может быть пустым")
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание фильма не может превышать 200 символов")
    private String description;
    @NotNull
    @LaterSpecifiedDate
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма должна быть положительной")
    private Integer duration;

    public Film() {
    }
}
