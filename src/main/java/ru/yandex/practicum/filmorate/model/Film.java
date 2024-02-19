package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Film {
    @NotBlank(message = "Film name invalid.")
    private final String name;
    @Size(max = 200, message = "Film description invalid.")
    private final String description;
    @MinimumDate(message = "Film release date invalid.")
    private final LocalDate releaseDate;
    @Positive(message = "Film duration invalid.")
    private final int duration;
    @NotNull
    private int id;
    private Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = 0;
        this.likes = new HashSet<>();
    }

    @Override
    public String toString() {
        String result = "Film{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", likes=" + likes;
        if (id == 0) {
                result += "}";
        } else {
                result += ", id=" + id + '}';
        }
        return result;
    }

    public int getCountLikes() {
        return this.likes.size();
    }
}