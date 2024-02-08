package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.annotations.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class User {
    @NotNull (message = "User login invalid")
    @ExceptedSymbols (message = "User login invalid")
    private final String login;
    private final String name;
    @Email (message = "User Email invalid")
    private final String email;
    @PastOrPresent (message = "User birthday invalid")
    private final LocalDate birthday;
    @NotNull
    private int id;
}