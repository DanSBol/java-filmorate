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
@NoArgsConstructor
public class Film {
    private int id;
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

    public Film(int id, String name, String description, LocalDate releaseDate, int duration, Set<Genre> genres,
                 Rating mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
        this.likes = null;
    }

    public Film(String name, String description, LocalDate releaseDate, int duration, Set<Genre> genres,
                 Rating mpa) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
        this.likes = null;
    }
}
