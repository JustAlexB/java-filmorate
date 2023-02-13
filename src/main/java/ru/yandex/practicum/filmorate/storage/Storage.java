package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

public interface Storage <T> {
    Collection<T> getAll();

    T create(@Valid T element);

    T update(@Valid T element);

    Optional<T> getByID(Integer elementID);

    abstract boolean validation (T element);
}
