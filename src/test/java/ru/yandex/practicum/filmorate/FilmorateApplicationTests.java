package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final UserService userService;
	private final FilmDbStorage filmStorage;
	private final FilmService filmService;

	private User user0;
	private User user1;
	private User user2;
	private Film film0;
	private Film film1;
	private Film film2;

	@BeforeEach
	public void beforeEach() {
		user0 = User.builder()
				.name("Alex")
				.login("Just")
				.email("fdhjdhfj@gmail.com")
				.birthday(LocalDate.of(1991, 07, 15))
				.build();

		user1 = User.builder()
				.name("Antony")
				.login("J")
				.email("djjjdh@gmail.com")
				.birthday(LocalDate.of(1991, 02, 15))
				.build();

		user2 = User.builder()
				.name("NoIdeas")
				.login("NoIdeas")
				.email("ddjjhjhd@gmail.com")
				.birthday(LocalDate.of(1991, 10, 15))
				.build();

		film0 = Film.builder()
				.name("Больше чем секс")
				.description("Легко, забавно и романтично - так в трех словах можно сказать о комедии и мелодраме" +
						", которую мы предлагаем посмотреть онлайн.")
				.releaseDate(LocalDate.of(2010, 01, 01))
				.duration(170)
				.rate(8)
				.build();
		film0.setMpa(new Mpa(4, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
		film0.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),	new Genre(1, "Комедия"))));

		film1 = Film.builder()
				.name("Плохой Санта")
				.description("Отвратительный Санта по имени Уилли и его маленький друг Маркус")
				.releaseDate(LocalDate.of(2015, 01, 10))
				.duration(170)
				.rate(3)
				.build();
		film1.setMpa(new Mpa(4, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
		film1.setGenres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"))));
	}


	@Test
	public void shouldCreateUser() {
		User user = userStorage.create(user1);
		assertNotNull(user);
	}

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = userStorage.getByID(1);
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void shouldUpdateUser() {
		User user = userStorage.create(user1);
		User userUpDate = User.builder()
				.id(user.getId())
				.name("Alex")
				.login("Just")
				.email("JustAlex@gmail.com")
				.birthday(LocalDate.of(1991, 07, 15))
				.build();

		assertThat(userStorage.update(userUpDate))
				.hasFieldOrPropertyWithValue("email", "JustAlex@gmail.com");
	}

	@Test
	public void shouldGetAll() {
		User userT = userStorage.create(user1);
		User userTT = userStorage.create(user2);

		Collection<User> userList = userStorage.getAll();
		assertThat(userList).asList().contains(userT);
		assertThat(userList).asList().contains(userTT);
	}

	@Test
	public void shoulAddandRemoveFriends() {
		User userT = userStorage.create(user1);
		User userTT = userStorage.create(user2);

		userService.addAsFriend(userT.getId(), userTT.getId());
		Collection<User> userList = userService.getUserFriends(userT.getId());
		assertThat(userList).asList().contains(userTT);

		userService.removeFromFriends(userT.getId(),userTT.getId());
		userList = userService.getUserFriends(userT.getId());
		assertThat(userList).asList().hasSize(0);
	}

	@Test
	public void shoulGetComonFriends() {
		User userT = userStorage.create(user0);
		User userTT = userStorage.create(user1);
		User userTTT = userStorage.create(user2);

		userService.addAsFriend(userT.getId(), userTTT.getId());
		userService.addAsFriend(userTT.getId(), userTTT.getId());
		Collection<User> userList = userService.getCommonFriends(userT.getId(),userTT.getId());
		assertThat(userList).asList().contains(userTTT);

	}

	@Test
	public void shouldCreateFilm() {
		Film film = filmStorage.create(film1);
		assertNotNull(film);
	}

	@Test
	public void testGetFilmById() {
		Optional<Film> filmOptional = filmStorage.getByID(1);
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void shouldUpdateFilm() {
		Film film = filmStorage.create(film0);
		Film filmUpDate = Film.builder()
				.id(film.getId())
				.name("Плохой Санта")
				.description("Обновленное описание")
				.releaseDate(LocalDate.of(2015, 01, 10))
				.duration(150)
				.rate(2)
				.build();
		filmUpDate.setMpa(new Mpa(4, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
		filmUpDate.setGenres(new HashSet<>(Arrays.asList(new Genre(1, "Комедия"))));

		assertThat(filmStorage.update(filmUpDate))
				.hasFieldOrPropertyWithValue("description", "Обновленное описание")
				.hasFieldOrPropertyWithValue("duration", 150);
	}

	@Test
	public void shouldGetAllFilms() {
		Film filmT = filmStorage.create(film0);
		Film filmTT = filmStorage.create(film1);

		Collection<Film> filmList = filmStorage.getAll();
		assertThat(filmList).asList().contains(filmT);
		assertThat(filmList).asList().contains(filmTT);
	}

	@Test
	public void shouldAddLike() {
		User userT = userStorage.create(user0);

		Film filmT = filmStorage.create(film0);
		filmService.addLike(filmT.getId(), userT.getId());

		Optional<Film> filmOptional = filmStorage.getByID(filmT.getId());
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("rate", 9)
				);
	}

	@Test
	public void shouldDelLike() {
		User userT = userStorage.create(user0);

		Film filmT = filmStorage.create(film0);
		filmService.removeLike(filmT.getId(), userT.getId());

		Optional<Film> filmOptional = filmStorage.getByID(filmT.getId());
		assertThat(filmOptional)
				.isPresent()
				.hasValueSatisfying(film ->
						assertThat(film).hasFieldOrPropertyWithValue("rate", 7)
				);
	}


	@Test
	public void shouldGetFilmRate() {
		Film film = filmStorage.create(film0);
		User userT = userStorage.create(user0);
		User userTT = userStorage.create(user1);
		User userTTT = userStorage.create(user2);

		filmService.addLike(film.getId(), userT.getId());
		filmService.addLike(film.getId(), userTT.getId());
		filmService.addLike(film.getId(), userTTT.getId());

		Collection<Film> filmList = filmService.getRateFilms(3);
		assertThat(filmList).asList().first().hasFieldOrPropertyWithValue("rate", 11);

	}



}
