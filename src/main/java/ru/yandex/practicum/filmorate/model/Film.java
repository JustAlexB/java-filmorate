package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();

    public Film() {
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa, Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("releaseDate", releaseDate);
        values.put("duration", duration);
        values.put("IDmpa", mpa.getId());
        return values;
    }
}
