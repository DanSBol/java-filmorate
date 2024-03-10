package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenre(int id) {
        log.info("Getting genre (id = {})", id);
        return genreStorage.getGenre(id).orElseThrow(() -> new NotFoundException("Genre not found."));
    }

    public Collection<Genre> getAllGenres() {
        log.info("Getting all genres");
        return genreStorage.getAllGenres();
    }
}