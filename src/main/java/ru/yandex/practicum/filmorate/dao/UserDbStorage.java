package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class UserDbStorage implements Storage<User> {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("userID"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }

    @Override
    public Collection<User> getAll(){
        return jdbcTemplate.query("SELECT * FROM users", UserDbStorage::makeUser);
    }

    @Override
    public User create(@Valid User user){
        validation(user);
        checkUserName(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("userID");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        log.info("Добавлен новый пользователь с ID = {}", user.getId());
        return user;
    }

    @Override
    public User update(@Valid User user) {
        validation(user);
        checkUserName(user);
        String sqlQuery = "UPDATE users SET " + "name = ?, birthday = ?, email = ?, login = ? WHERE userID = ?";
        int result = jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getBirthday(),
                user.getEmail(),
                user.getLogin(),
                user.getId());
        if (result != 1)
            throw new NotFoundException("Пользователь " + user.toString() + " не найден");
        return user;
    }

    @Override
    public Optional<User> getByID(Integer userID){
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE userID = ?", userID);
        User user = null;
        if (userRows.first()) {
            user = new User(userRows.getInt("userID"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getDate("birthday").toLocalDate());
        } else {
            throw new NotFoundException("Пользователь с ID = " + userID + " не найден.");
        }
        return Optional.of(user);
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
