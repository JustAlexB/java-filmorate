package ru.yandex.practicum.filmorate;

import org.junit.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final static UserController userController = null;//new UserController();

    @Test
    public void shouldValidateUser() {
        User user = new User(1, "mail@yandex.ru", "Alex", "", LocalDate.of(1991,2,20));
        assertTrue(userController.validation(user));
    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateUserEmail() {
        User user = new User(1, "mail.yandex.ru", "Alex", "", LocalDate.of(1991,2,20));
        userController.validation(user);

    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateUserBirthday() {
        User user = new User(1, "mail@yandex.ru", "Alex", "", LocalDate.now().plusDays(1));
        userController.validation(user);

    }
}
