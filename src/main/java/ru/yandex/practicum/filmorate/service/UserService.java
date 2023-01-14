package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

@Service
@Slf4j
public class UserService {
    protected final InMemoryUserStorage UserStorage;

    @Autowired
    public UserService(InMemoryUserStorage UserStorage) {
        this.UserStorage = UserStorage;
    }

    public Collection<User> getAll(){
        return UserStorage.getAll();
    }

    public User getUserByID(Integer userID) {
        return UserStorage.elements.get(userID);
    }

    public User create(User user){
        UserStorage.create(user);
        return user;
    }

    public boolean update(User user) {
        UserStorage.update(user);
        Integer currentUserID = user.getId();
        if (UserStorage.elements.containsKey(currentUserID)) {
            UserStorage.elements.put(user.getId(), user);
            return true;
        } else
            return false;
    }

    public void addAsFriend(Integer userID, Integer friendID) {
        User user = UserStorage.elements.get(userID);
        User friend = UserStorage.elements.get(friendID);
        UserStorage.addFriend(user, friend);
        UserStorage.addFriend(friend, user);
    }

    public void removeFromFriends(Integer userID, Integer friendID) {
        User user = UserStorage.elements.get(userID);
        User friend = UserStorage.elements.get(friendID);
        UserStorage.removeFriend(user, friend);
        UserStorage.removeFriend(friend, user);
    }

    public Collection<User> getUserFriends(Integer userID){
        TreeSet<Integer> friendsOfUser = UserStorage.sympathy.get(userID);
        return getCollectionOfUsers(friendsOfUser);
    }

    public Collection <User> getCommonFriends(Integer userID, Integer otherUserID){
        TreeSet <Integer> friendsOfUser = UserStorage.sympathy.get(userID);
        TreeSet <Integer> friendsOfOther = UserStorage.sympathy.get(otherUserID);
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
