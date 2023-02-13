package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.RateDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;

@Service
public class FilmService {
    private final Storage<Film> filmStorage;
    protected final RateDbStorage rateStorage;
    @Autowired
    public FilmService(@Qualifier("filmDbStorage")Storage<Film> filmStorage, RateDbStorage rateStorage) {
        this.filmStorage = filmStorage;
        this.rateStorage = rateStorage;
    }

    public Collection<Film> getAll(){
        return filmStorage.getAll();
    }

    public Film create(Film film){
        filmStorage.create(film);
        return film;
    }

    public boolean update(Film film) {
        if (filmStorage.update(film) != null) {
            return true;
        } else
            return false;
    }

    public Optional<Film> getFilmByID(Integer filmID) {
        return filmStorage.getByID(filmID);
    }

    public void addLike(Integer filmID, Integer userID) {
        rateStorage.addLike(filmID, userID);
    }

    public void removeLike(Integer filmID, Integer userID) {
        rateStorage.removeLike(filmID, userID);
    }

    public Collection <Film> getRateFilms(Integer count) {
        return rateStorage.getRateFilms(count);
    }

}
