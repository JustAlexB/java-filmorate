package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage extends InMemoryStorage <Film> {
    @Override
    public Film create(@Valid Film film) {
        super.create(film);
        film.setId(elementID);
        log.info("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public Film update(@Valid Film film) {
        super.update(film);
        Integer currentFilmID = film.getId();
        if (elements.containsKey(currentFilmID)) {
            log.info("Обновлен фильм {}", film);
            elements.put(film.getId(), film);
        } else {
            log.info("Фильм {} не найден", film);
            throw new NotFoundException("Фильм " + film.toString() + " не найден");
        }
        return film;
    }

    public Optional<Film> getFilmByID(Integer filmID) {
        return getByID(filmID);
    }

    public void addLike(Integer filmID, Integer userID) {
        TreeSet<Integer> likes = sympathy.get(filmID);
        if (likes == null)
            likes = new TreeSet<>();
        likes.add(userID);
        sympathy.put(filmID, likes);
    }

    public void removeLike(Integer filmID, Integer userID) {
        TreeSet <Integer> likes = sympathy.get(filmID);
        likes.remove(userID);
        if(likes.size() == 0)
            sympathy.remove(filmID);
    }

    public Collection<Film> getRateFilms(Integer count) {
//        HashMap<Film, Integer> transitional = new HashMap<>();
//        for (Map.Entry<Integer, TreeSet<Integer>> entry : sympathy.entrySet()) {
//            Film key = getFilmByID(entry.getKey());
//            Integer value = entry.getValue().size();
//            transitional.put(key, value);
//        }
//
//        LinkedHashMap<Film, Integer> collectFilms = transitional.entrySet().stream()
//                .sorted(Map.Entry.<Film, Integer>comparingByValue().reversed())
//                .limit(count)
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
//
//        Collection<Film> filmRate = (collectFilms.isEmpty() ? elements.values() : new ArrayList<>(collectFilms.keySet()));

        return null;//filmRate;
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
