package ru.yandex.practicum.filmorate.storage;

import javax.validation.Valid;
import java.util.Collection;

public interface Storage <T> {
    Collection<T> getAll();

    T create(@Valid T element);

    T update(@Valid T element);

    abstract boolean validation (T element);
}
