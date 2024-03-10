package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

//@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    private int id = 0;

    @Override
    public Optional<User> get(int id) {
        User user = users.get(id);
        return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> add(User user) {
        user.setId(++id);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return users.get(user.getId()) != null ? Optional.of(user) : Optional.empty();
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
    public void addToFriends(int id, int friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        Friendship fsForUser = new Friendship(friendId, false);
        Friendship fsForFriend = new Friendship(id, false);
        Set<Friendship> userSet = user.getFriends();
        Set<Friendship> friendSet = friend.getFriends();
        if (!userSet.contains(fsForUser)) {
            userSet.add(fsForUser);
            user.setFriends(userSet);
            users.replace(id, user);
        }
        if (!friendSet.contains(fsForFriend)) {
            friendSet.add(fsForFriend);
            friend.setFriends(friendSet);
            users.replace(friendId, friend);
        }
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        if (!users.containsKey(friendId)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        User user = users.get(id);
        User friend = users.get(friendId);
        Friendship fsForUser = new Friendship(friendId, false);
        Friendship fsForFriend = new Friendship(id, false);
        Set<Friendship> userSet = user.getFriends();
        Set<Friendship> friendSet = friend.getFriends();
        if (userSet.contains(fsForUser)) {
            userSet.remove(fsForUser);
            user.setFriends(userSet);
            users.replace(id, user);
        }
        if (friendSet.contains(fsForFriend)) {
            friendSet.remove(fsForFriend);
            friend.setFriends(friendSet);
            users.replace(friendId, friend);
        }
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(String.format("User (id = %s) not found.", id));
        }
        Collection<User> friends = new ArrayList<>();
        for (Friendship item : users.get(id).getFriends()) {
            friends.add(users.get(item.getUserId()));
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
        for (Friendship item : users.get(id).getFriends()) {
            if (users.get(otherId).getFriends().contains(item)) {
                friends.add(users.get(item.getUserId()));
            }
        }
        return friends;
    }
}