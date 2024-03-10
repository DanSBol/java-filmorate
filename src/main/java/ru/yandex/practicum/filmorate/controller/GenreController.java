package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable(value = "id") Integer id) {
        return genreService.getGenre(id);
    }

    @GetMapping
    public Collection<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }
}