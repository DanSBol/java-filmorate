package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static final UserController uc = new UserController();

    @Test
    void validateUserOk() throws ValidationException {
        final User validUser = new User("UserLogin", "Dan", "dan.s.boltachev@yandex.ru",
                LocalDate.of(1982, 4,21));
        uc.validate(validUser);
    }

    @Test
    void validateUserFail() {
        User user = new User("My login", "Dan", "dan.s.boltachev@yandex.ru",
                LocalDate.of(1982,4,21));
        User user1 = user;
        Exception exception = assertThrows(ValidationException.class, () -> uc.validate(user1));
        assertEquals("User login invalid", exception.getMessage());

        user = new User(null, "Dan", "dan.s.boltachev@yandex.ru",
                LocalDate.of(1982,4,21));
        User user2 = user;
        exception = assertThrows(ValidationException.class, () -> uc.validate(user2));
        assertEquals("User login invalid", exception.getMessage());

        user = new User("UserLogin", "Dan", "",
                LocalDate.of(1982,4,21));
        User user3 = user;
        exception = assertThrows(ValidationException.class, () -> uc.validate(user3));
        assertEquals("User Email invalid", exception.getMessage());

        user = new User("UserLogin", "Dan", "dan.s.boltachev.yandex.ru",
                LocalDate.of(1982,4,21));
        User user4 = user;
        exception = assertThrows(ValidationException.class, () -> uc.validate(user4));
        assertEquals("User Email invalid", exception.getMessage());

        user = new User("UserLogin", "Dan", "dan.s.boltachev@yandex.ru",
                LocalDate.of(2982,4,21));
        User user5 = user;
        exception = assertThrows(ValidationException.class, () -> uc.validate(user5));
        assertEquals("User birthday invalid", exception.getMessage());
    }
}