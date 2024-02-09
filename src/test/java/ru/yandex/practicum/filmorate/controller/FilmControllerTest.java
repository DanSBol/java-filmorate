package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    static final FilmController fc = new FilmController();

    @Test
    void validateFilmOk() throws ValidationException {
        final Film validFilm = new Film("Film name", "Film description",
                LocalDate.of(1895,12,28), 100);
        fc.validate(validFilm);
    }

    @Test
    void validateFilmFail() {
        Film film = new Film(null, "Film description",
                LocalDate.of(1985,12,28), 100);
        Film film1 = film;
        Exception exception = assertThrows(ValidationException.class, () -> fc.validate(film1));
        assertEquals("Film name invalid", exception.getMessage());

        film = new Film("", "Film description",
                LocalDate.of(1985,12,28), 100);
        Film film2 = film;
        exception = assertThrows(ValidationException.class, () -> fc.validate(film2));
        assertEquals("Film name invalid", exception.getMessage());

        film = new Film("Film name", "f".repeat(201),
                LocalDate.of(1985,12,28), 100);
        Film film3 = film;
        exception = assertThrows(ValidationException.class, () -> fc.validate(film3));
        assertEquals("Film description invalid", exception.getMessage());

        film = new Film("Film name", "Film description",
                LocalDate.of(1895,12,27), 100);
        Film film4 = film;
        exception = assertThrows(ValidationException.class, () -> fc.validate(film4));
        assertEquals("Film release date invalid", exception.getMessage());

        film = new Film("Film name", "Film description",
                LocalDate.of(1985,12,28), 0);
        Film film5 = film;
        exception = assertThrows(ValidationException.class, () -> fc.validate(film5));
        assertEquals("Film duration invalid", exception.getMessage());
    }
}