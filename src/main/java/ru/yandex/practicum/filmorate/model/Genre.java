package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Genre {
    private int id;
    @NotBlank(message = "Genre name invalid.")
    private String name;
}