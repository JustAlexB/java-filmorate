package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final static UserStorage userStorage = new InMemoryUserStorage();

    @Test
    public void shouldValidateUser() {
        User user = new User(1, "mail@yandex.ru", "Alex", "", LocalDate.of(1991,2,20));
        assertTrue(userStorage.validation(user));
    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateUserEmail() {
        User user = new User(1, "mail.yandex.ru", "Alex", "", LocalDate.of(1991,2,20));
        userStorage.validation(user);

    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateUserBirthday() {
        User user = new User(1, "mail@yandex.ru", "Alex", "", LocalDate.now().plusDays(1));
        userStorage.validation(user);

    }
}
