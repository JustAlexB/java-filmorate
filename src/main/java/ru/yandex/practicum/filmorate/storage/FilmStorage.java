package ru.yandex.practicum.filmorate.storage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public interface FilmStorage<Film> {
    HashMap<Integer, TreeSet<Integer>> filmLikes = new HashMap<>();

    Collection<Film> getAll();

    Film create(@Valid Film element);

    Film update(@Valid Film element);

    abstract boolean validation (Film element);
}
