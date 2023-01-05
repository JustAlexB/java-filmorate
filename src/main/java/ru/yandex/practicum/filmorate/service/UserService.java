package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmNUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

@Service
@Slf4j
public class UserService {
    protected final InMemoryFilmNUserStorage<User> filmNUserStorage;

    @Autowired
    public UserService(InMemoryFilmNUserStorage<User> filmNUserStorage) {
        this.filmNUserStorage = filmNUserStorage;
    }

    public Collection<User> getAll(){
        return filmNUserStorage.getAll();
    }

    public User getUserByID(Integer userID) {
        return filmNUserStorage.elements.get(userID);
    }

    public void create(User user){
        filmNUserStorage.create(user);
        user.setId(filmNUserStorage.elementID);
    }

    public boolean update(User user) {
        filmNUserStorage.update(user);
        Integer currentUserID = user.getId();
        if (filmNUserStorage.elements.containsKey(currentUserID)) {
            filmNUserStorage.elements.put(user.getId(), user);
            return true;
        } else
            return false;
    }

    public void addAsFriend(Integer userID, Integer friendID) {
        User user = filmNUserStorage.getElement(userID);
        User friend = filmNUserStorage.getElement(friendID);
        filmNUserStorage.addFriend(user, friend);
        filmNUserStorage.addFriend(friend, user);
    }

    public void removeFromFriends(Integer userID, Integer friendID) {
        User user = filmNUserStorage.getElement(userID);
        User friend = filmNUserStorage.getElement(friendID);
        filmNUserStorage.removeFriend(user, friend);
        filmNUserStorage.removeFriend(friend, user);
    }

    public Collection<User> getUserFriends(Integer userID){
        TreeSet<Integer> friendsOfUser = filmNUserStorage.userFriends.get(userID);
        return getCollectionOfUsers(friendsOfUser);
    }

    public Collection <User> getCommonFriends(Integer userID, Integer otherUserID){
        TreeSet <Integer> friendsOfUser = filmNUserStorage.userFriends.get(userID);
        TreeSet <Integer> friendsOfOther = filmNUserStorage.userFriends.get(otherUserID);
        if (friendsOfUser == null || friendsOfOther == null)
            return getCollectionOfUsers(null);
        TreeSet <Integer> friendsOfUserFinal = new TreeSet<>();
        friendsOfUserFinal.addAll(friendsOfUser);
        friendsOfUserFinal.retainAll(friendsOfOther);
        return getCollectionOfUsers(friendsOfUserFinal);
    }

    private Collection<User> getCollectionOfUsers(TreeSet<Integer> usersID){
        Collection<User> friends = new ArrayList<>();
        if (usersID != null)
            for (Integer value : usersID) {
                friends.add(getUserByID(value));
            }
        return friends;
    }

    public void checkUserName(User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя {} инициализировано логином {} ", user, user.getLogin());
        }
    }
}
