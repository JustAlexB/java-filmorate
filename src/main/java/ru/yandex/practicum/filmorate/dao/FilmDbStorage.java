package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements Storage<Film> {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaStorage, GenreStorage genreStorage){
        this.jdbcTemplate=jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("filmID"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("releaseDate").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("IDmpa"), "", ""),
                null,
                rs.getInt("rate"));
    }

    @Override
    public Collection<Film> getAll(){

        List<Film> films = jdbcTemplate.query("SELECT * FROM film", FilmDbStorage::makeFilm);

        for (Film film : films) {
            Optional<Mpa> mpa = mpaStorage.getMpaByID(film.getMpa().getId());
            mpa.ifPresent(film::setMpa);
            film.setGenres(genreStorage.getGenreByFilmID(film.getId()));
        }

        return films;
    }

    @Override
    public Film create(@Valid Film film) {
        validation(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("film")
                .usingGeneratedKeyColumns("filmID");
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());

        Optional<Mpa> mpa = mpaStorage.getMpaByID(film.getMpa().getId());
        mpa.ifPresent(film::setMpa);

        String sqlQuery = "INSERT INTO filmGenre (IDfilm, IDgenre) values (?, ?)";
        if(!film.getGenres().isEmpty()) {
            for (Genre value : film.getGenres()) {
                jdbcTemplate.update(sqlQuery, film.getId(), value.getId());
            }
        } else
            jdbcTemplate.update(sqlQuery, film.getId(), null);
        return film;
    }

    @Override
    public Film update(@Valid Film film) {
        validation(film);
        String sqlQuery = "UPDATE film SET name = ?, description = ?, releaseDate = ?, duration = ?, IDmpa = ? WHERE filmID = ?";
        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (result != 1)
            throw new NotFoundException("Фильм " + film.toString() + " не найден");

        sqlQuery = "DELETE FROM FILMGENRE WHERE IDFILM = ?";
        jdbcTemplate.update(sqlQuery, film.getId());

        sqlQuery = "INSERT INTO filmGenre (IDfilm, IDgenre) values (?, ?)";
        if(!film.getGenres().isEmpty()) {
            for (Genre value : film.getGenres()) {
                result = jdbcTemplate.update(sqlQuery, film.getId(), value.getId());
                if (result < 1)
                    throw new NotFoundException("Жанры фильма " + film.toString() + " не обновлены");
            }
        } else
            jdbcTemplate.update(sqlQuery, film.getId(), null);

        List<Genre> genresSort = film.getGenres().stream()
                .sorted(Comparator.comparing(Genre::getId))
                .collect(Collectors.toList());
        film.setGenres(new LinkedHashSet<>(genresSort));

        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @Override
    public Optional<Film> getByID(Integer filmID){
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE filmID = ?", filmID);
        if (filmRows.first()) {
            Film film = new Film(filmRows.getInt("filmID"),
                    filmRows.getString("name"),
                    filmRows.getString("description"),
                    filmRows.getDate("releaseDate").toLocalDate(),
                    filmRows.getInt("duration"),
                    null,
                    genreStorage.getGenreByFilmID(filmRows.getInt("filmID")),
                    filmRows.getInt("rate"));

            Optional<Mpa> mpa = mpaStorage.getMpaByID(filmRows.getInt("IDmpa"));
            mpa.ifPresent(film::setMpa);
            return Optional.of(film);
        } else {
            throw new NotFoundException("Фильм с ID = " + filmID + " не найден.");
        }
    }

    @Override
    public boolean validation (Film film) {
        if (film.getDescription().length() > 200) {
            log.debug("Длина описания фильма {} превышает 200 символов", film);
            throw new ValidationException("Длина описания фильма " + film.toString() + " превышает 200 символов", film);
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза фильма {} ранее 28.12.1895", film);
            throw new ValidationException("Дата релиза фильма " + film.toString() + " ранее 28.12.1895", film);
        }
        return true;
    }

}
