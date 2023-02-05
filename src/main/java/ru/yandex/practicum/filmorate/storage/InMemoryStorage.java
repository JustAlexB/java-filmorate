package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;


import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeSet;

public class InMemoryStorage <T> implements Storage <T> {
    public HashMap<Integer, T> elements = new HashMap<>();
    public Integer elementID = 0;
    public HashMap<Integer, TreeSet<Integer>> sympathy = new HashMap<>();

    @Override
    public Collection<T> getAll(){
        return elements.values();
    }

    @Override
    public T create(@Valid T element) {
        validation(element);
        elements.put(++elementID, element);
        return element;
    }

    @Override
    public T update(@Valid @RequestBody T element){
        validation(element);
        return element;
    }

    @Override
    public Optional<T> getByID(Integer elementID) {
        return Optional.of(elements.get(elementID));
    }

    @Override
    public boolean validation (T element) {
       return false;
    }
}
