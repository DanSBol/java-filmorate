package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Genre> getAllGenres() {
        log.info("Getting all genres");
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(int id) {
        log.info("Getting genre (id = {})", id);
        return filmStorage.getGenre(id)
                .orElseThrow(() -> new NotFoundException("Genre not found."));
    }

    public Collection<Rating> getAllRatings() {
        log.info("Getting all ratings");
        return filmStorage.getAllRatings();
    }

    public Rating getRating(int id) {
        log.info("Getting rating (id = {})", id);
        return filmStorage.getRating(id)
                .orElseThrow(() -> new NotFoundException("Rating not found."));
    }

    public Film get(int id) {
        log.info("Getting film (id = {})", id);
        return filmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Film not found."));

    }

    public Film add(Film film) {
        log.info("Creating film {}", film);
        validate(film);
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("Updating film {}", film);
        validate(film);
        return filmStorage.update(film).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public void delete() {
        log.info("Deleting all films");
        filmStorage.delete();
    }

    public Collection<Film> getAll() {
        log.info("Getting all films");
        return filmStorage.getAll();
    }

    public Film addLike(int id, int userId) {
        log.info("Adding film like");
        return filmStorage.addLike(id, userId).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public Film deleteLike(int id, int userId) {
        log.info("Deleting film like");
        return filmStorage.deleteLike(id, userId).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public Collection<Film> getPopular(int count) {
        log.info("Getting popular films");
        return filmStorage.getPopular(count);
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Film name invalid.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Film description invalid.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Film release date invalid.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration invalid.");
        }
    }
}