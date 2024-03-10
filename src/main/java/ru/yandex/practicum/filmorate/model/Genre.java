package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NotNull
    private int id;
    @NotBlank(message = "Genre name invalid.")
    private String name;
}