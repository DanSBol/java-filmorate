package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 0;

    @Override
    public Optional<Film> get(int id) {
        Film film = films.get(id);
        return film != null ? Optional.of(film) : Optional.empty();
    }

    @Override
    public Film add(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
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
    public Film addLike(int id, int userId) {
        if (!films.containsKey(id)) {
            throw new NotFoundException(String.format("Film (id = %s) not found.", id));
        }
        Film film = films.get(id);
        Set<Integer> newSet = film.getLikes();
        if (!newSet.contains(userId)) {
            newSet.add(userId);
            film.setLikes(newSet);
        }
        return film;
    }

    @Override
    public Film deleteLike(int id, int userId) {
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
        return film;
    }

    @Override
    public Collection<Film> getPopular(int count) {
        Collection<Film> populars = new ArrayList<>();

        List<Film> filmsByLikes = new ArrayList<>(films.values());
        filmsByLikes.sort(Comparator.comparing(Film::getCountLikes).reversed());
        return filmsByLikes.stream().limit(count).collect(Collectors.toList());
    }
}