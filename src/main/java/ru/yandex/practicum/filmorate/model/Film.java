package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
@Builder(builderClassName = "FilmBuilder")
public class Film {
    int id;
    @NotBlank(message = "Film name invalid.")
    private String name;
    @Size(max = 200, message = "Film description invalid.")
    private String description;
    @MinimumDate(message = "Film release date invalid.")
    private LocalDate releaseDate;
    @Positive(message = "Film duration invalid.")
    private int duration;
    private Set<Genre> genres;
    private Rating mpa;
    private Set<Integer> likes;

    public static class FilmBuilder {
        public FilmBuilder() {
        }
    }
}