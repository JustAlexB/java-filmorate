package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Mpa;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@Component
public class MpaStorage {
    private final Logger log = LoggerFactory.getLogger(MpaStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public MpaStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    public Optional<Mpa> getMpaByID(Integer mpaID){
        if(mpaID == null)
            throw new IncorrectParameterException("Передан пустой mpaID");
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM MPA WHERE mpaID = ?", mpaID);
        if (mpaRows.first()) {
            log.info("Найден mpa с ID = {}", mpaID);
            Mpa mpa = new Mpa(mpaRows.getInt("mpaID"),
                    mpaRows.getString("name"),
                    mpaRows.getString("description"));
            return Optional.of(mpa);
        } else {
            log.info("Не найден mpa с ID = {}", mpaID);
            return Optional.empty();
        }
    }

    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Mpa(
                rs.getInt("mpaID"),
                rs.getString("name"),
                rs.getString("description"))
        );
    }
}
