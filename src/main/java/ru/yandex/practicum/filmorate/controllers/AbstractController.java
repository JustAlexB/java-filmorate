package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.naming.NameNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class AbstractController <T> {
    HashMap <Integer, T> elements = new HashMap<>();
    Integer elementID = 0;

    @GetMapping
    public List<T> getAll(){
        List<T> elementList =new ArrayList<>(elements.values());
        return elementList;
    }

    @PostMapping
    public T create(@Valid @RequestBody T element) {
        elements.put(++elementID, element);
        return element;
    };

    @PutMapping
    public abstract T update(@Valid @RequestBody T element);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleException(ValidationException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (exception.getCause() != null)
            status = HttpStatus.NOT_FOUND;

        return new ResponseEntity(exception.getElement(), status);
    }
    public abstract boolean validation (T element);
}
