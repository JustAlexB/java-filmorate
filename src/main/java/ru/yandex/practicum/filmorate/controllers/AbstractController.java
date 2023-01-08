package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.List;

public abstract class AbstractController <T> {

    //HashMap <Integer, T> elements = new HashMap<>();
    //Integer elementID = 0;
//    protected final InMemoryFilmNUserStorage <T> filmNUserStorage;
//
//    @Autowired
//    public AbstractController(InMemoryFilmNUserStorage<T> filmNUserStorage) {
//        this.filmNUserStorage = filmNUserStorage;
//    }

    @GetMapping
    public List<T> getAll(){
        //List<T> elementList = new ArrayList<>(super.elements.values());
        return null; //elementList;
    }

    @PostMapping
    public T create(@Valid @RequestBody T element) {
        validation(element);
       // super.elements.put(++super.elementID, element);
        return element;
    }

    @PutMapping
    public T update(@Valid @RequestBody T element){
        validation(element);
        return element;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity handleException(ValidationException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        if (exception.getCause() != null)
            status = HttpStatus.NOT_FOUND;

        return new ResponseEntity(exception.getElement(), status);
    }
    public abstract boolean validation (T element);
}
