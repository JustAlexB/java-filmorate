package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.naming.NameNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController <Film> {

    @Override
    public Film create(@Valid @RequestBody Film film) {
        validation(film);
        log.info("Добавлен фильм {}", film);
        super.create(film);
        film.setId(super.elementID);
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        Integer currentFilmID = film.getId();
        validation(film);
        if (super.elements.containsKey(currentFilmID)) {
            log.info("Обновлен фильм {}", film);
            super.elements.put(film.getId(), film);
        } else {
            log.info("Фильм {} не найден", film);
            throw new ValidationException("Фильм " + film.toString() + " не найден", film, new NameNotFoundException());
        }
        return film;
    }

    @Override
    public boolean validation (Film film) {
        if (film.getDescription().length() > 200) {
            log.debug("Длина описания фильма {} превышает 200 символов", film);
            throw new ValidationException("Длина описания фильма " + film.toString() + " превышает 200 символов", film);
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза фильма {} ранее 28.12.1895", film);
            throw new ValidationException("Дата релиза фильма " + film.toString() + " ранее 28.12.1895", film);
        }
        return true;
    }

}
