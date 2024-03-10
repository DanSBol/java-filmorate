package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> getUser(int id);

    User addUser(User user);

    Optional<User> updateUser(User user);

    void deleteAllUsers();

    Collection<User> getAllUsers();

    void addToFriends(int id, int friendId);

    void deleteFromFriends(int id, int friendId);

    Collection<User> getAllFriends(int id);

    Collection<User> getCommonFriends(int id, int otherId);
}