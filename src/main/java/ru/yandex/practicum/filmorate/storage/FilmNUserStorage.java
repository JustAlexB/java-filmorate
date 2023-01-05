package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public interface FilmNUserStorage <T> {

    public Collection<T> getAll();

    public T create(@Valid T element);

    public T update(@Valid T element);

    public abstract boolean validation (T element);
}
