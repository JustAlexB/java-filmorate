package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorage genreStorage;
    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> getGenreByID(Integer genreID){
        return genreStorage.getGenreByID(genreID);
    }

    public Collection<Genre> getAllGenres(){
        return new ArrayList<>(genreStorage.getAllGenres());
    }
}
