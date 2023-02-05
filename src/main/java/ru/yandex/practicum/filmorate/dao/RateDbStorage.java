package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Component
public class RateDbStorage {
    private final Logger log = LoggerFactory.getLogger(RateDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public RateDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public void addLike(Integer filmID, Integer userID) {
        String sqlQuery = "INSERT INTO likes (IDfilm, IDuser) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
        log.info("Пользователь ID = {} лайкнул фильм ID = {}", userID, filmID);
    }

    public void removeLike(Integer filmID, Integer userID) {
        String sqlQuery = "DELETE FROM likes WHERE IDfilm = ? AND IDuser = ?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
        log.info("Пользователь ID = {} удалил лайк фильму ID = {}", userID, filmID);
    }

    public Collection<Film> getRateFilms(Integer count) {
        ArrayList<Film> films = new ArrayList<>();
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILM f LEFT JOIN LIKES l ON f.FILMID = l.IDFILM GROUP BY f.FILMID ORDER BY COUNT(l.IDUSER) DESC LIMIT ?", count);
        while(filmRows.next()) {
            Film film = new Film(filmRows.getInt("filmID"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getInt("duration"),
                    null,
                    genreStorage.getGenreByFilmID(filmRows.getInt("filmID")));
            Optional<Mpa> mpa = mpaStorage.getMpaByID(filmRows.getInt("IDmpa"));
            mpa.ifPresent(film::setMpa);
            log.info("Найден фильм: {} ", film);
            if (film != null)
                films.add(film);
        }
        return films;
    }

}
