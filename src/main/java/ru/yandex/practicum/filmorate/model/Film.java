package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.MinimumDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Film {
    @NotBlank(message = "Film name invalid")
    private final String name;
    @Size(max = 200, message = "Film description invalid")
    private final String description;
    @MinimumDate (message = "Film release date invalid")
    private final LocalDate releaseDate;
    @Positive(message = "Film duration invalid")
    private final int duration;
    @NotNull
    private int id;
}