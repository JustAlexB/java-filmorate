package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
//public class FilmController extends AbstractController<Film> {
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll(){
        return filmService.getAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (filmService.update(film))
            log.info("Обновлен фильм {}", film);
        else {
            log.info("Фильм {} не найден", film);
            throw new NotFoundException("Фильм " + film.toString() + " не найден");
        }
        return film;
    }

    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmID, @PathVariable("userId") Integer userID){
        filmService.addLike(filmID, userID);
        log.info("Пользователь {} поставил лайк фильму {}", userID, filmService.getFilmByID(filmID));
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike(@PathVariable("id") Integer filmID, @PathVariable("userId") Integer userID){
        if(filmID != null && userID != null && filmID >= 0 && userID >=0) {
            filmService.removeLike(filmID, userID);
            log.info("Пользователь {} забрал лайк у фильма {}", userID, filmService.getFilmByID(filmID));
        } else {
            throw new NotFoundException("Переданы некорректные ID");
        }
    }

    @GetMapping("/popular")
    public Collection<Film> getRateFilms(@RequestParam(defaultValue = "10", required = false) Integer count){
        return filmService.getRateFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer filmID){
        log.info("Запрос фильма по ID: {}", filmID);
        Film fondFilm = filmService.getFilmByID(filmID);
        if (fondFilm == null) {
            log.info("Фильм c ID {} не найден", filmID);
            throw new NotFoundException("Фильм c ID: " + filmID + " не найден");
        }
        return fondFilm;
    }

}
