package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> getAllGenres() {
        log.info("Запрошены все жанры");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreByID(@PathVariable("id") Integer genreID) {
        log.info("Запрошен жанр c ID = {}", genreID);
        Optional<Genre> genre = genreService.getGenreByID(genreID);
        if(genre.isEmpty())
            throw new NotFoundException("Жанр c ID: " + genreID + " не найден");
        return genre;
    }

}
