package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Component
@Slf4j
public class InMemoryUserStorage extends InMemoryFilmNUserStorage <User> {
    @Override
    public User create(@Valid User user) {
        checkUserName(user);
        super.create(user);
        user.setId(super.elementID);
        return user;
    }

    @Override
    public User update(@Valid User user) {
        checkUserName(user);
        Integer currentUserID = user.getId();
        if (super.elements.containsKey(currentUserID)) {
            super.elements.put(user.getId(), user);
        } else {
            throw new NotFoundException("Пользователь " + user.toString() + " не найден");
        }
        return user;
    }

    private void checkUserName(User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
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
