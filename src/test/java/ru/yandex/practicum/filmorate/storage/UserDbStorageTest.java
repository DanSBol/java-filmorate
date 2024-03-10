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
        User user = new User.UserBuilder()
                .login("userLogin")
                .name("user Name")
                .email("user@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        user.setId((userStorage.addUser(user).getId()));
        // вызываем тестируемый метод
        Optional<User> optSavedUser = userStorage.getUser(user.getId());
        assertTrue(optSavedUser.isPresent());
        User savedUser = optSavedUser.get();
        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void update() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = new User.UserBuilder()
                .login("userLogin")
                .name("user Name")
                .email("user@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        user.setId((userStorage.addUser(user).getId()));
        // вызываем тестируемый метод
        user.setName("another Name");
        Optional<User> optSavedUser = userStorage.updateUser(user);
        assertTrue(optSavedUser.isPresent());
        User savedUser = optSavedUser.get();
        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void delete() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User user = new User.UserBuilder()
                .login("userLogin")
                .name("user Name")
                .email("user@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        user.setId((userStorage.addUser(user).getId()));
        // вызываем тестируемый метод
        userStorage.deleteAllUsers();
        // проверяем утверждения
        assertTrue(userStorage.getAllUsers().isEmpty());
    }

    @Test
    void getAll() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User userOne = new User.UserBuilder()
                .login("userLoginOne")
                .name("user Name One")
                .email("userOne@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userOne.setId((userStorage.addUser(userOne).getId()));
        User userTwo = new User.UserBuilder()
                .login("userLoginTwo")
                .name("user Name Two")
                .email("userTwo@yandex.ru")
                .birthday(LocalDate.of(1990, 2, 2))
                .build();
        userTwo.setId((userStorage.addUser(userTwo).getId()));
        Collection<User> users = new ArrayList<>();
        users.add(userOne);
        users.add(userTwo);
        // вызываем тестируемый метод
        Collection<User> savedUsers = userStorage.getAllUsers();
        // проверяем утверждения
        assertThat(savedUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @Test
    void addToFriends() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User userOne = new User.UserBuilder()
                .login("userLoginOne")
                .name("user Name One")
                .email("userOne@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userOne.setId((userStorage.addUser(userOne).getId()));
        User userTwo = new User.UserBuilder()
                .login("userLoginTwo")
                .name("user Name Two")
                .email("userTwo@yandex.ru")
                .birthday(LocalDate.of(1990, 2, 2))
                .build();
        userTwo.setId((userStorage.addUser(userTwo).getId()));
        Collection<User> friends = new ArrayList<>();
        friends.add(userTwo);
        // вызываем тестируемый метод
        userStorage.addToFriends(userOne.getId(), userTwo.getId());
        userStorage.addToFriends(userTwo.getId(), userOne.getId());
        // проверяем утверждения
        Collection<User> savedFriends = userStorage.getAllFriends(userOne.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(friends);

        // меняем сценарий теста (отзыв заявки в друзья)
        friends.remove(userTwo);
        // вызываем тестируемый метод
        userStorage.deleteFromFriends(userOne.getId(), userTwo.getId());
        // проверяем утверждения
        savedFriends = userStorage.getAllFriends(userOne.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(friends);
    }

    @Test
    void getCommonFriends() {
        // Подготавливаем данные для теста
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User userOne = new User.UserBuilder()
                .login("userLoginOne")
                .name("user Name One")
                .email("userOne@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userOne.setId((userStorage.addUser(userOne).getId()));
        User userTwo = new User.UserBuilder()
                .login("userLoginTwo")
                .name("user Name Two")
                .email("userTwo@yandex.ru")
                .birthday(LocalDate.of(1990, 2, 2))
                .build();
        userTwo.setId((userStorage.addUser(userTwo).getId()));
        User userThree = new User.UserBuilder()
                .login("userLoginThree")
                .name("user Name Three")
                .email("userThree@yandex.ru")
                .birthday(LocalDate.of(1990, 3, 3))
                .build();
        userThree.setId((userStorage.addUser(userThree).getId()));
        Collection<User> friends = new ArrayList<>();
        friends.add(userThree);
        // вызываем тестируемый метод
        userStorage.addToFriends(userOne.getId(), userThree.getId());
        userStorage.addToFriends(userThree.getId(), userOne.getId());
        userStorage.addToFriends(userTwo.getId(), userThree.getId());
        userStorage.addToFriends(userThree.getId(), userTwo.getId());
        // проверяем утверждения
        Collection<User> savedFriends = userStorage.getCommonFriends(userOne.getId(), userTwo.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(friends);

        // меняем сценарий теста (отзыв заявки в друзья)
        friends.remove(userThree);
        // вызываем тестируемый метод
        userStorage.deleteFromFriends(userOne.getId(), userThree.getId());
        // проверяем утверждения
        savedFriends = userStorage.getCommonFriends(userOne.getId(), userTwo.getId());
        assertThat(savedFriends)
                .isNotNull() //
                .usingRecursiveComparison()
                .isEqualTo(friends);
    }
}