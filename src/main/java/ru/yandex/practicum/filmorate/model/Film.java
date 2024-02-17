package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Film {
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
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