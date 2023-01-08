package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final InMemoryFilmStorage filmStorage;
    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAll(){
        return filmStorage.getAll();
    }

    public Film create(Film film){
        filmStorage.create(film);
        return film;
    }

    public boolean update(Film film) {
        filmStorage.update(film);
        Integer currentFilmID = film.getId();
        if (filmStorage.elements.containsKey(currentFilmID)) {
            filmStorage.elements.put(film.getId(), film);
            return true;
        } else
            return false;
    }

    public Film getFilmByID(Integer filmID) {
        return filmStorage.elements.get(filmID);
    }

    public void addLike(Integer filmID, Integer userID) {
        TreeSet<Integer> likes = filmStorage.filmLikes.get(filmID);
        if (likes == null)
            likes = new TreeSet<>();
        likes.add(userID);
        filmStorage.filmLikes.put(filmID, likes);
    }

    public void removeLike(Integer filmID, Integer userID) {
        TreeSet <Integer> likes = filmStorage.filmLikes.get(filmID);
        likes.remove(userID);
        if(likes.size() == 0)
            filmStorage.filmLikes.remove(filmID);
    }

    public Collection <Film> getRateFilms(Integer count) {
        HashMap<Film, Integer> transitional = new HashMap<>();
        for (Map.Entry<Integer, TreeSet<Integer>> entry : filmStorage.filmLikes.entrySet()) {
            Film key = getFilmByID(entry.getKey());
            Integer value = entry.getValue().size();
            transitional.put(key, value);
        }

        LinkedHashMap<Film, Integer> collectFilms = transitional.entrySet().stream()
                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
                .limit(count)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Collection<Film> filmRate = (collectFilms.isEmpty() ? filmStorage.elements.values() : new ArrayList<>(collectFilms.keySet()));

        return filmRate;
    }

}
