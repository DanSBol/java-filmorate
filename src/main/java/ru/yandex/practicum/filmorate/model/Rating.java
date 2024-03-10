package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Rating {
    private final int id;
    @Enumerated(EnumType.STRING)
    private final String name;
}