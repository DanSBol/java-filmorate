package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        user.setId(++id);
        if (user.getName() == null || user.getName().isEmpty()) {
            int tempId = id;
            user = new User(user.getLogin(), user.getLogin(), user.getEmail(), user.getBirthday());
            user.setId(tempId);
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be BAD REQUEST (CODE 400)\n");
    }

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains(String.valueOf('@'))) {
            throw new ValidationException(("User Email invalid"));
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(String.valueOf(' '))) {
            throw new ValidationException("User login invalid");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("User birthday invalid");
        }
    }
}