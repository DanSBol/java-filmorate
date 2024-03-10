package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

//@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 0;

    @Override
    public Optional<Film> getFilm(int id) {
        Film film = films.get(id);
        return film != null ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return Optional.of(film);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        id = 0;
    }

    @Override
    public Collection<Film> getAllFilms() {
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
    public Collection<Film> getPopularFilms(int count) {
        return null;
    }
}