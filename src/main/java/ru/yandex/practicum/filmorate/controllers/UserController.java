package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll(){
        Collection<User> elementList = userService.getAll();
        return elementList;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userService.checkUserName(user);
        userService.create(user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        userService.checkUserName(user);
        if (userService.update(user))
            log.info("Обновлен пользователь {}", user);
        else {
            log.info("Пользователь {} не найден", user);
            throw new NotFoundException("Пользователь " + user.toString() + " не найден");
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer userID, @PathVariable("friendId") Integer friendID) {
        if (userID != null && userID >=0 && friendID != null && friendID >=0) {
            userService.addAsFriend(userID, friendID);
            log.info("Пользователь {} и {} добавлены в друзья", userService.getUserByID(userID), userService.getUserByID(friendID));
        } else {
            log.info("Пользователь {} и {} не станут друзьями, ID некорректны", userID, friendID);
            throw new NotFoundException("Некорректный ID пользователя");
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable("id") Integer userID, @PathVariable("friendId") Integer friendID) {
        userService.removeFromFriends(userID, friendID);
        log.info("Пользователь {} и {} больше не друзья", userService.getUserByID(userID), userService.getUserByID(friendID));
    }

    @GetMapping(value = {"{id}/friends", "{id}/friends/common/{otherId}"})
    public Collection<User> getUserFriends(@PathVariable("id") Integer userID, @PathVariable Optional<Integer> otherId) {
        log.info("Запрошены друзья пользователя {}", userService.getUserByID(userID));
        if (otherId.isPresent())
            return userService.getCommonFriends(userID, otherId.get());
        else
            return userService.getUserFriends(userID);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Integer userID){
        log.info("Запрос пользователя по ID: {}", userID);
        User fondUser = userService.getUserByID(userID);
        if (fondUser == null) {
            log.info("Пользователь c ID {} не найден", userID);
            throw new NotFoundException("Пользователь c ID: " + userID + " не найден");
        }
        return fondUser;
    }


}
