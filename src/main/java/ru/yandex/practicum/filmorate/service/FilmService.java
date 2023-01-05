package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmNUserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final InMemoryFilmNUserStorage<Film> filmNUserStorage;
    @Autowired
    public FilmService(InMemoryFilmNUserStorage<Film> filmNUserStorage) {
        this.filmNUserStorage = filmNUserStorage;
    }

    public Collection<Film> getAll(){
        return filmNUserStorage.getAll();
    }

    public void create(Film film){
        filmNUserStorage.create(film);
        film.setId(filmNUserStorage.elementID);
    }

    public boolean update(Film film) {
        filmNUserStorage.update(film);
        Integer currentFilmID = film.getId();
        if (filmNUserStorage.elements.containsKey(currentFilmID)) {
            filmNUserStorage.elements.put(film.getId(), film);
            return true;
        } else
            return false;
    }

    public Film getFilmByID(Integer filmID) {
        return filmNUserStorage.elements.get(filmID);
    }

    public void addLike(Integer filmID, Integer userID) {
        TreeSet<Integer> likes = filmNUserStorage.filmLikes.get(filmID);
        if (likes == null)
            likes = new TreeSet<>();
        likes.add(userID);
        filmNUserStorage.filmLikes.put(filmID, likes);
    }

    public void removeLike(Integer filmID, Integer userID) {
        TreeSet <Integer> likes = filmNUserStorage.filmLikes.get(filmID);
        likes.remove(userID);
        if(likes.size() == 0)
            filmNUserStorage.filmLikes.remove(filmID);
    }

    public Collection <Film> getRateFilms(Integer count) {
        HashMap<Film, Integer> transitional = new HashMap<>();
        for (Map.Entry<Integer, TreeSet<Integer>> entry : filmNUserStorage.filmLikes.entrySet()) {
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

        Collection<Film> filmRate = (collectFilms.isEmpty() ? filmNUserStorage.elements.values() : new ArrayList<>(collectFilms.keySet()));

        return filmRate;
    }

}
