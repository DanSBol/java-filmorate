package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User get(int id) {
        log.info("Getting user (id = {})", id);
        return userStorage.get(id).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public User add(User user) {
        log.info("Creating user {}", user);
        validate(user);
        return userStorage.add(user).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public User update(User user) {
        log.info("Updating user {}", user);
        validate(user);
        return userStorage.update(user).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public void delete() {
        log.info("Deleting all users");
        userStorage.delete();
    }

    public Collection<User> getAll() {
        log.info("Getting all users");
        return userStorage.getAll();
    }

    public void addToFriends(int id, int friendId) {
        log.info("Adding to friends");
        userStorage.addToFriends(id, friendId);
    }

    public void deleteFromFriends(int id, int friendId) {
        log.info("Deleting from friends");
        userStorage.deleteFromFriends(id, friendId);
    }

    public Collection<User> getAllFriends(int id) {
        log.info("Getting all friends");
        return userStorage.getAllFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Getting common friends");
        return userStorage.getCommonFriends(id, otherId);
    }

    private void validate(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains(String.valueOf('@'))) {
            throw new ValidationException("User Email invalid.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(String.valueOf(' '))) {
            throw new ValidationException("User login invalid.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday invalid.");
        }
    }
}