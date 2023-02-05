package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;

	@Test
	public void shouldCreateUser() {
		User user = userStorage.create(new User(1, "mail@yandex.ru", "Alex", "", LocalDate.of(1991,2,20)));
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

//	@Test
//	public void shouldUpdateUser() {
//		User user = userStorage.update(new User(1, "updatemail@yandex.ru", "Alex", "", LocalDate.of(1891,2,20)));
//		assertNotNull(user);
//	}
//
//	@Test
//	public void shouldGetAll() {
//		Collection<User> user = userStorage.getAll();
//		assertTrue(user.size() == 1);
//	}

}
