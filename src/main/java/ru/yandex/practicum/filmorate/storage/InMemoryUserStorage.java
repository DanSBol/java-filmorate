package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Getter
    @Setter
    private int id;

    public InMemoryUserStorage() {
        this.id = 0;
    }

    @Override
    public Optional<User> get(int id) {
        if (users.containsKey(id)) {
            users.get(id);
            return Optional.of(users.get(id));
        }
        return Optional.empty();
    }

    @Override
    public User add(User user) {
        user.setId(++id);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public void delete() {
        users.clear();
        id = 0;
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User addToFriends(int id, int friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        Set<Integer> userSet = user.getFriends();
        Set<Integer> friendSet = friend.getFriends();
        if (!userSet.contains(friendId)) {
            userSet.add(friendId);
            user.setFriends(userSet);
            users.replace(id, user);
        }
        if (!friendSet.contains(id)) {
            friendSet.add(id);
            friend.setFriends(friendSet);
            users.replace(friendId, friend);
        }
        return user;
    }

    @Override
    public User deleteFromFriends(int id, int friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        Set<Integer> userSet = user.getFriends();
        Set<Integer> friendSet = friend.getFriends();
        if (userSet.contains(friendId)) {
            userSet.remove(friendId);
            user.setFriends(userSet);
            users.replace(id, user);
        }
        if (friendSet.contains(id)) {
            friendSet.remove(id);
            friend.setFriends(friendSet);
            users.replace(friendId, friend);
        }
        return user;
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        Collection<User> friends = new ArrayList<>();
        for (Integer item : users.get(id).getFriends()) {
            friends.add(users.get(item));
        }
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        if (!users.containsKey(otherId)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        Collection<User> friends = new ArrayList<>();
        for (Integer item : users.get(id).getFriends()) {
            if (users.get(otherId).getFriends().contains(item)) {
                friends.add(users.get(item));
            }
        }
        return friends;
    }
}