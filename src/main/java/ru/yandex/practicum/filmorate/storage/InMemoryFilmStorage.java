package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.*;

//@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 0;

    @Override
    public Collection<Genre> getAllGenres() {
        return null;
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return null;
    }

    @Override
    public Optional<Rating> getRating(int id) {
        return Optional.empty();
    }

    @Override
    public Optional<Film> get(int id) {
        Film film = films.get(id);
        return film != null ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Film add(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Optional<Film> update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public void delete() {
        films.clear();
        id = 0;
    }

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Optional<Film> addLike(int id, int userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Film (id = %s) not found.", id));
        }
        Film film = films.get(id);
        Set<Integer> newSet = film.getLikes();
        if (!newSet.contains(userId)) {
            newSet.add(userId);
            film.setLikes(newSet);
        }
        return films.get(film.getId()) != null ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Optional<Film> deleteLike(int id, int userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Film (id = %s) not found.", id));
        }
        Film film = films.get(id);
        Set<Integer> newSet = film.getLikes();
        if (!newSet.contains(userId)) {
            throw new NotFoundException(String.format("User id %s not found among likes.", id));
        }
        newSet.remove(userId);
        film.setLikes(newSet);
        return films.get(film.getId()) != null ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return null;
    }
}