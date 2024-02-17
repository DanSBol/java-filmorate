package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User get(int id) {
        log.info("Getting user (id = {})", id);
        Optional<User> optionalUser = inMemoryUserStorage.get(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found.");
        }
        return optionalUser.get();
    }

    public User add(User user) {
        log.info("Creating user {}", user);
        validate(user);
        return inMemoryUserStorage.add(user);
    }

    public User update(User user) {
        log.info("Updating user {}", user);
        validate(user);
        Optional<User> optionalUser = inMemoryUserStorage.update(user);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User not found.");
        }
        return optionalUser.get();
    }

    public void delete() {
        log.info("Deleting all users");
        inMemoryUserStorage.delete();
    }

    public Collection<User> getAll() {
        log.info("Getting all users");
        return inMemoryUserStorage.getAll();
    }

    public User addToFriends(int id, int friendId) {
        log.info("Adding to friends");
        return inMemoryUserStorage.addToFriends(id, friendId);
    }

    public User deleteFromFriends(int id, int friendId) {
        log.info("Deleting from friends");
        return inMemoryUserStorage.deleteFromFriends(id, friendId);
    }

    public Collection<User> getAllFriends(int id) {
        log.info("Getting all friends");
        return inMemoryUserStorage.getAllFriends(id);
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Getting common friends");
        return inMemoryUserStorage.getCommonFriends(id, otherId);
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