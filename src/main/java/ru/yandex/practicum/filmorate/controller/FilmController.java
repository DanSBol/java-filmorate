package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return film;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "HTTP Status will be BAD REQUEST (CODE 400)\n");
    }

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException(("Film name invalid"));
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Film description invalid");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895,12,
                28))) {
            throw new ValidationException("Film release date invalid");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration invalid");
        }
    }
}