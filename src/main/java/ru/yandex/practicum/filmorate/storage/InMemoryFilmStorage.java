package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

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
