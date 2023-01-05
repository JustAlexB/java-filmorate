package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;


public abstract class InMemoryFilmNUserStorage <T> implements FilmNUserStorage <T> {
    public HashMap<Integer, TreeSet<Integer>> userFriends = new HashMap<>();
    public HashMap<Integer, TreeSet<Integer>> filmLikes = new HashMap<>();
    public HashMap<Integer, T> elements = new HashMap<>();
    public Integer elementID = 0;

    public Collection<T> getAll(){
        return elements.values();
    }

    public T getElement(Integer iD){
        return elements.get(iD);
    }

    public T create(@Valid T element) {
        validation(element);
        elements.put(++elementID, element);
        return element;
    }

    public T update(@Valid T element){
        validation(element);
        return element;
    }

    public void addFriend(User user, User friend) {
        TreeSet <Integer> friends = userFriends.get(user.getId());
        if (friends == null)
            friends = new TreeSet<>();

        friends.add(friend.getId());
        userFriends.put(user.getId(), friends);
    }

    public void removeFriend(User user, User friend) {
        TreeSet <Integer> friends = userFriends.get(user.getId());
        friends.remove(friend.getId());
    }

    public abstract boolean validation (T element);
}
