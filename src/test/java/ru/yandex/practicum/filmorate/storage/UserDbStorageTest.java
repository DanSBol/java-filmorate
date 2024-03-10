package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    void get() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User("vanya123", "Ivan Petrov", "user@email.ru",
                LocalDate.of(1990, 1, 1));

        newUser.setId(userStorage.add(newUser).orElseThrow().getId());

        // вызываем тестируемый метод
        Optional<User> optSavedUser = userStorage.get(newUser.getId());
        assertTrue(optSavedUser.isPresent());
        User savedUser = optSavedUser.get();
        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void update() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User("vanya123", "Ivan Petrov", "user@email.ru",
                LocalDate.of(1990, 1, 1));
        newUser.setId(userStorage.add(newUser).orElseThrow().getId());

        // вызываем тестируемый метод
        newUser.setName("Petr Ivanov");
        Optional<User> optSavedUser = userStorage.update(newUser);
        assertTrue(optSavedUser.isPresent());
        User savedUser = optSavedUser.get();
        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    void delete() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUser = new User("vanya123", "Ivan Petrov", "user@email.ru",
                LocalDate.of(1990, 1, 1));
        newUser.setId(userStorage.add(newUser).orElseThrow().getId());
        // вызываем тестируемый метод
        userStorage.delete();
        // проверяем утверждения
        assertTrue(userStorage.getAll().isEmpty());
    }

    @Test
    void getAll() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUserOne = new User("vanya123", "Ivan Petrov", "userOne@email.ru",
                LocalDate.of(1990, 1, 1));
        newUserOne.setId(userStorage.add(newUserOne).orElseThrow().getId());
        User newUserTwo = new User("vanya456", "Petr Ivanov", "userTwo@email.ru",
                LocalDate.of(1992, 2, 2));
        newUserTwo.setId(userStorage.add(newUserTwo).orElseThrow().getId());
        Collection<User> newUsers = new ArrayList<>();
        newUsers.add(newUserOne);
        newUsers.add(newUserTwo);
        // вызываем тестируемый метод
        Collection<User> savedUsers = userStorage.getAll();
        // проверяем утверждения
        assertThat(savedUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUsers);
    }

    @Test
    void addToFriends() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUserOne = new User("vanya123", "Ivan Petrov", "userOne@email.ru",
                LocalDate.of(1990, 1, 1));
        newUserOne.setId(userStorage.add(newUserOne).orElseThrow().getId());
        User newUserTwo = new User("vanya456", "Petr Ivanov", "userTwo@email.ru",
                LocalDate.of(1992, 2, 2));
        newUserTwo.setId(userStorage.add(newUserTwo).orElseThrow().getId());
        Collection<User> newFriends = new ArrayList<>();
        newFriends.add(newUserTwo);
        // вызываем тестируемый метод
        userStorage.addToFriends(newUserOne.getId(), newUserTwo.getId());
        userStorage.addToFriends(newUserTwo.getId(), newUserOne.getId());
        // проверяем утверждения
        Collection<User> savedFriends = userStorage.getAllFriends(newUserOne.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(newFriends);

        // меняем сценарий теста (отзыв заявки в друзья)
        newFriends.remove(newUserTwo);
        // вызываем тестируемый метод
        userStorage.deleteFromFriends(newUserOne.getId(), newUserTwo.getId());
        // проверяем утверждения
        savedFriends = userStorage.getAllFriends(newUserOne.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(newFriends);
    }

    @Test
    void getCommonFriends() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUserOne = new User("vanya123", "Ivan Petrov", "userOne@email.ru",
                LocalDate.of(1990, 1, 1));
        newUserOne.setId(userStorage.add(newUserOne).orElseThrow().getId());
        User newUserTwo = new User("vanya456", "Petr Ivanov", "userTwo@email.ru",
                LocalDate.of(1992, 2, 2));
        newUserTwo.setId(userStorage.add(newUserTwo).orElseThrow().getId());
        User newUserThree = new User("chuvak789", "Ilya Ilukhov", "userThree@email.ru",
                LocalDate.of(1993, 3, 3));
        newUserThree.setId(userStorage.add(newUserThree).orElseThrow().getId());

        Collection<User> newFriends = new ArrayList<>();
        newFriends.add(newUserThree);
        // вызываем тестируемый метод
        userStorage.addToFriends(newUserOne.getId(), newUserThree.getId());
        userStorage.addToFriends(newUserThree.getId(), newUserOne.getId());
        userStorage.addToFriends(newUserTwo.getId(), newUserThree.getId());
        userStorage.addToFriends(newUserThree.getId(), newUserTwo.getId());
        // проверяем утверждения
        Collection<User> savedFriends = userStorage.getCommonFriends(newUserOne.getId(), newUserTwo.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(newFriends);

        // меняем сценарий теста (отзыв заявки в друзья)
        newFriends.remove(newUserThree);
        // вызываем тестируемый метод
        userStorage.deleteFromFriends(newUserOne.getId(), newUserThree.getId());
        // проверяем утверждения
        savedFriends = userStorage.getCommonFriends(newUserOne.getId(), newUserTwo.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(newFriends);
    }
}