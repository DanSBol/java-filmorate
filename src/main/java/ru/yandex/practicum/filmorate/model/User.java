package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
@Builder(builderClassName = "UserBuilder")
public class User {
    @NotNull
    private int id;
    @NotNull(message = "User login invalid.")
    @Pattern(regexp = "^\\S+$", message = "User login invalid.")
    private String login;
    private String name;
    @Email(message = "User Email invalid.")
    private String email;
    @PastOrPresent(message = "User birthday invalid.")
    private LocalDate birthday;
    private Set<Friendship> friends;

    public static class UserBuilder {
        public UserBuilder() {
        }
    }
}