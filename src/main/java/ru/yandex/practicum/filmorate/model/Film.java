package ru.yandex.practicum.filmorate.model;

import lombok.Data;

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
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма должна быть положительной")
    private Integer duration;

    public Film() {
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
