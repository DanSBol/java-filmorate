package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User get(@PathVariable(value = "id") Integer id) {
        return userService.get(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping
    public void delete() {
        userService.delete();
    }

    @GetMapping
    public Collection<User> getAll() {
        return userService.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendId") Integer friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendId") Integer friendId) {
        userService.deleteFromFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable(value = "id") Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public Collection<User> getCommonFriends(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "friendId") Integer friendId) {
        return userService.getCommonFriends(id, friendId);
    }
}