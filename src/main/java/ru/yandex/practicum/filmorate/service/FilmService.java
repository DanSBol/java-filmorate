package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public Film get(int id) {
        log.info("Getting film (id = {})", id);
        return inMemoryFilmStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Film not found."));

    }

    public Film add(Film film) {
        log.info("Creating film {}", film);
        validate(film);
        return inMemoryFilmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("Updating film {}", film);
        validate(film);
        Optional<Film> optionalFilm = inMemoryFilmStorage.update(film);
        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Film not found.");
        }
        return optionalFilm.get();
    }

    public void delete() {
        log.info("Deleting all films");
        inMemoryFilmStorage.delete();
    }

    public Collection<Film> getAll() {
        log.info("Getting all films");
        return inMemoryFilmStorage.getAll();
    }

    public Film addLike(int id, int userId) {
        log.info("Adding film like");
        return inMemoryFilmStorage.addLike(id, userId);
    }

    public Film deleteLike(int id, int userId) {
        log.info("Deleting film like");
        return inMemoryFilmStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopular(int count) {
        log.info("Getting popular films");
        return inMemoryFilmStorage.getPopular(count);
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