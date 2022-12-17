package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ElementNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController <User> {
    @Override
    public User create(@Valid @RequestBody User user) {
        checkUserName(user);
        super.create(user);
        user.setId(super.elementID);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        checkUserName(user);
        Integer currentUserID = user.getId();
        if (super.elements.containsKey(currentUserID)) {
            super.elements.put(user.getId(), user);
            log.info("Обновлен пользователь {}", user);
        } else {
            log.info("Пользователь {} не найден", user);
            throw new ElementNotFoundException("Пользователь " + user.toString() + " не найден", user);
        }
        return user;
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity handleException(ElementNotFoundException exception) {
        return new ResponseEntity(exception.getElement(),HttpStatus.NOT_FOUND);
    }

    private void checkUserName(User user) {
        if(user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }
}
