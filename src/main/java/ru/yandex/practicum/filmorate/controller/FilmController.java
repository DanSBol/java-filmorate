package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/genres")
    public Collection<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(
        @PathVariable(value = "id") Integer id) {
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public Collection<Rating> getAllRatings() {
        return filmService.getAllRatings();
    }

    @GetMapping("/mpa/{id}")
    public Rating getRating(
            @PathVariable(value = "id") Integer id) {
        return filmService.getRating(id);
    }

    @GetMapping("/films/{id}")
    public Film get(@PathVariable(value = "id") Integer id) {
        return filmService.get(id);
    }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("/films")
    public void delete() {
        filmService.delete();
    }

    @GetMapping("/films")
    public Collection<Film> getAll() {
        return filmService.getAll();
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "userId") Integer userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(
            @PathVariable(value = "id") Integer id,
            @PathVariable(value = "userId") Integer userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopular(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopular(count);
    }
}