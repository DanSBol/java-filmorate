package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class Rating {
    @NotNull
    private final int id;
    //@NotBlank(message = "Rating name invalid.")
    private final String name;
}