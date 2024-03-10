package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User getUser(int id) {
        log.info("Getting user (id = {})", id);
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public User addUser(User user) {
        log.info("Creating user {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Updating user {}", user);
        return userStorage.updateUser(user).orElseThrow(() -> new NotFoundException("User not found."));
    }

    public void deleteAllUsers() {
        log.info("Deleting all users");
        userStorage.deleteAllUsers();
    }

    public Collection<User> getAllUsers() {
        log.info("Getting all users");
        return userStorage.getAllUsers();
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
}