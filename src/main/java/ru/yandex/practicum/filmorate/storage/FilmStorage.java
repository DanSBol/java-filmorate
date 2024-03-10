package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Genre> getAllGenres();

    Optional<Genre> getGenre(int id);

    Collection<Rating> getAllRatings();

    Optional<Rating> getRating(int id);

    Optional<Film> get(int id);

    Film add(Film film);

    Optional<Film> update(Film film);

    void delete();

    Collection<Film> getAll();

    Optional<Film> addLike(int id, int userId);

    Optional<Film> deleteLike(int id, int userId);

    Collection<Film> getPopular(int count);
}
