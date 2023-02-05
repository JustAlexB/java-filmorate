package ru.yandex.practicum.filmorate;
import org.junit.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest  {
    private final static InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();

    @Test
    public void shouldValidateFilm() {
//        Film film = new Film(1, "name", "horror", LocalDate.now(), 20);
//        assertTrue(filmStorage.validation(film));
    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateFilmDescription() {
//        Film film = new Film(1, "name", "horrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorror" +
//                "horrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorror" +
//                "horrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorrorhorror"
//                , LocalDate.of(1654,1,03), 20);
//        filmStorage.validation(film);
    }

    @Test(expected = ValidationException.class)
    public void shouldNotValidateFilmReleaseDate() {
//        Film film = new Film(1, "name", "horror", LocalDate.of(1654,1,3), 20);
//        filmStorage.validation(film);
    }

}
