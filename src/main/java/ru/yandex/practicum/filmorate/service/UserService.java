package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDbStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    protected final Storage<User> userStorage;
    protected final FriendsDbStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") Storage<User> userStorage, FriendsDbStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }

    public Collection<User> getAll(){
        return userStorage.getAll();
    }

    public Optional<User> getUserByID(Integer userID) {
        return userStorage.getByID(userID);
    }

    public User create(User user){
        userStorage.create(user);
        return user;
    }

    public boolean update(User user) {
        if (userStorage.update(user) != null) {
            return true;
        } else
            return false;
    }

    public void addAsFriend(Integer userID, Integer friendID) {
        User user = userStorage.getByID(userID).get();
        User friend = userStorage.getByID(friendID).get();
        if(user !=null && friend !=null && !user.equals(friend)) {
            Collection<User> userFriends = friendsStorage.getUserFriends(friendID);
            boolean isFriends = false;
            if(!userFriends.isEmpty()){
                isFriends = userFriends.contains(user);
            }
            friendsStorage.addFriend(userID, friendID, isFriends);
        }
    }

    public void removeFromFriends(Integer userID, Integer friendID) {
        friendsStorage.removeFriend(userID, friendID);
    }

    public Collection<User> getUserFriends(Integer userID){
        if (userStorage.getByID(userID) != null)
            return friendsStorage.getUserFriends(userID);
        else
            return null;
    }

    public Collection <User> getCommonFriends(Integer userID, Integer otherUserID){
        return friendsStorage.getCommonFriends(userID, otherUserID);
    }


    public void checkUserName(User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя {} инициализировано логином {} ", user, user.getLogin());
        }
    }
}
