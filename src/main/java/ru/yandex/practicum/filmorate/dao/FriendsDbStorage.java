package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class FriendsDbStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    public void addFriend(Integer userID, Integer friendID, boolean isFriends) {
        String sqlQuery = "";
        if(isFriends) {
            //подтвержденная
            sqlQuery = "UPDATE friends SET IDuser = ? AND IDfriend = ? AND status = ? WHERE IDuser = ? AND IDfriend = ?";
            jdbcTemplate.update(sqlQuery, userID, friendID, isFriends);
        } else {
            //неподтвержденная
            sqlQuery = "INSERT INTO friends (IDuser, IDfriend, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userID, friendID, isFriends);
        }
    }

    public void removeFriend(Integer userID, Integer friendID) {
        String sqlQuery = "DELETE FROM friends WHERE IDuser = ? AND IDfriend = ?";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }

    public Collection<User> getUserFriends(Integer userID){
        List<User> friendsOfUser = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS u INNER JOIN FRIENDS f ON f.IDFRIEND = u.USERID WHERE f.IDUSER = ?", userID);
        while(userRows.next()) {
            log.info("Найден пользователь: {} {}", userRows.getString("userID"), userRows.getString("name"));
            User user = new User(userRows.getInt("userID"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
            if (user != null)
                friendsOfUser.add(user);
        }
        return friendsOfUser;
    }

    public Collection<User> getCommonFriends(Integer userID, Integer otherUserID) {
        List<User> friendsOfUser = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS u WHERE u.USERID IN (SELECT f.IDFRIEND  FROM FRIENDS f WHERE f.IDuser = ? AND f.IDFRIEND IN (SELECT IDFriend FROM FRIENDS f WHERE IDUSER = ?))", userID, otherUserID);
        while(userRows.next()) {
            log.info("Найден общий друг пользоватлей: {} и {} -> {}", userID, otherUserID, userRows.getString("userID"));
            User user = new User(userRows.getInt("userID"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
            if (user != null)
                friendsOfUser.add(user);
        }
        return friendsOfUser;
    }
}
