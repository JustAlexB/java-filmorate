package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();
    Integer userID = 0;

    @GetMapping
    public List<User> getAll() {
        log.info("Список пользователей содержит {} записей", users.size());
        List<User> usersList =new ArrayList<>(users.values());
        return usersList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        user.setId(++userID);
        if (validationUser(user)) {
            log.info("Добавлен пользователь {}", user);
            users.put(user.getId(), user);
            return user;
        }
        throw new ValidationException("Проблема с данными");
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
        if (validationUser(user)) {
            Integer currentUserID = user.getId();
            if (users.containsKey(currentUserID)) {
                log.info("Обновлен пользователь {}", user);
                users.put(user.getId(), user);
                return user;
            } else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        throw new ValidationException("Проблема с данными");
    }

    private boolean validationUser (User user) {
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.debug("Логин пользователя {} задан неверно", user);
            return false;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Имя пользователя {} переопределено на логин", user);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            log.debug("Дата рождения пользователя {} находится в будущем", user);
            return false;
        }
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Почта пользователя {} задана неверно", user);
            return false;
        }
        return true;
    }
}
