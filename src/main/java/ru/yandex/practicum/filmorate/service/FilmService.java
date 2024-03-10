package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;

    public Film getFilm(int id) {
        log.info("Getting film (id = {})", id);
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new NotFoundException("Film not found."));

    }

    public Film addFilm(Film film) {
        log.info("Creating film {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Updating film {}", film);
        return filmStorage.updateFilm(film).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public void deleteAllFilms() {
        log.info("Deleting all films");
        filmStorage.deleteAllFilms();
    }

    public Collection<Film> getAllFilms() {
        log.info("Getting all films");
        return filmStorage.getAllFilms();
    }

    public Film addLike(int id, int userId) {
        log.info("Adding film like");
        return filmStorage.addLike(id, userId).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public Film deleteLike(int id, int userId) {
        log.info("Deleting film like");
        return filmStorage.deleteLike(id, userId).orElseThrow(() -> new NotFoundException("Film not found."));
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Getting popular films");
        return filmStorage.getPopularFilms(count);
    }
}