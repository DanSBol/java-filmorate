package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> get(int id);

    User add(User user);

    Optional<User> update(User user);

    void delete();

    Collection<User> getAll();

    User addToFriends(int id, int friendId);

    User deleteFromFriends(int id, int friendId);

    Collection<User> getAllFriends(int id);

    Collection<User> getCommonFriends(int id, int otherId);
}