package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.naming.NameNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends AbstractController <User> {
    @Override
    public User create(@Valid @RequestBody User user) {
        checkUserName(user);
        validation(user);
        super.create(user);
        user.setId(super.elementID);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(@Valid @RequestBody User user) {
        checkUserName(user);
        validation(user);
        Integer currentUserID = user.getId();
        if (super.elements.containsKey(currentUserID)) {
            super.elements.put(user.getId(), user);
            log.info("Обновлен пользователь {}", user);
        } else {
            log.info("Пользователь {} не найден", user);
            throw new ValidationException("Пользователь " + user.toString() + " не найден", user, new NameNotFoundException());
        }
        return user;
    }

    private void checkUserName(User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя {} инициализировано логином {} ", user, user.getLogin());
        }
    }

    @Override
    public boolean validation (User user) {
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            log.debug("Дата рождения пользователя {} находится в будущем", user);
            throw new ValidationException("Дата рождения пользователя " + user.toString() + " находится в будущем", user);
        }
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Почта пользователя {} задана неверно", user);
            throw new ValidationException("Почта пользователя " + user.toString() + " задана неверно", user);
        }
        return true;
    }
}
