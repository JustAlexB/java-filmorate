package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class GenreStorage {
    private final Logger log = LoggerFactory.getLogger(GenreStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public GenreStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    public Optional<Genre> getGenreByID(Integer genreID){
        if(genreID == null)
            throw new IncorrectParameterException("Передан пустой genreID");
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE WHERE genreID = ?", genreID);
        if (genreRows.first()) {
            log.info("Найден жанр с ID = {}", genreID);
            Genre genre = new Genre(genreRows.getInt("genreID"),
                    genreRows.getString("name"));
            return Optional.of(genre);
        } else {
            log.info("Не найден жанр с ID = {}", genreID);
            return Optional.empty();
        }
    }

    public Set<Genre> getGenreByFilmID(Integer filmID){
        if(filmID == null)
            throw new IncorrectParameterException("Передан пустой filmID");

        Set<Genre> genres = new HashSet<>();
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRE g INNER JOIN FILMGENRE f ON f.IDGENRE = g.GENREID WHERE f.IDfilm = ? ORDER BY g.GENREID", filmID);
        while (genreRows.next()) {
            log.info("Найден фильм ID {}: жанр ID {}", filmID, genreRows.getInt("IDGenre"));
            Genre genre = new Genre(genreRows.getInt("IDgenre"),
                    genreRows.getString("name"));
            genres.add(genre);
        }
        return genres;
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(
                rs.getInt("genreID"),
                rs.getString("name"))
        );
    }
}
