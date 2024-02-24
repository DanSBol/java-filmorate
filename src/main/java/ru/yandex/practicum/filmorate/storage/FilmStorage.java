package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> get(int id);

    Film add(Film film);

    Optional<Film> update(Film film);

    void delete();

    Collection<Film> getAll();

    Film addLike(int id, int userId);

    Film deleteLike(int id, int userId);

    Collection<Film> getPopular(int count);
}
