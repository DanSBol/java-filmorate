package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> getFilm(int id);

    Film addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    void deleteAllFilms();

    Collection<Film> getAllFilms();

    Optional<Film> addLike(int id, int userId);

    Optional<Film> deleteLike(int id, int userId);

    Collection<Film> getPopularFilms(int count);
}
