package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ElementNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractController <Film> {

    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм {}", film);
        super.create(film);
        film.setId(super.elementID);
        return film;
    }

    @Override
    public Film update(@Valid @RequestBody Film film) {
        Integer currentFilmID = film.getId();
        if (super.elements.containsKey(currentFilmID)) {
            log.info("Обновлен фильм {}", film);
            super.elements.put(film.getId(), film);
        } else {
            log.info("Фильм {} не найден", film);
            throw new ElementNotFoundException("Фильм " + film.toString() + " не найден", film);
        }
        return film;
    }

}
