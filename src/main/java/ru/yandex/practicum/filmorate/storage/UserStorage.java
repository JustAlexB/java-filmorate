package ru.yandex.practicum.filmorate.storage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public interface UserStorage<User> {
    HashMap<Integer, TreeSet<Integer>> userFriends = new HashMap<>();

    Collection<User> getAll();

    User create(@Valid User element);

    User update(@Valid User element);

    boolean validation (User element);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);
}
