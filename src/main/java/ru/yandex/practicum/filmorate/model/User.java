package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class User {
    @NotNull(message = "User login invalid.")
    @Pattern(regexp = "^\\S+$", message = "User login invalid.")
    private final String login;
    private String name;
    @Email(message = "User Email invalid.")
    private final String email;
    @PastOrPresent(message = "User birthday invalid.")
    private final LocalDate birthday;
    @NotNull
    private int id;
    private Set<Integer> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.id = 0;
        this.friends = new HashSet<>();
    }

    @Override
    public String toString() {
        String result = "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthday=" + birthday +
                ", friends=" + friends;
        if (id == 0) {
            result += "}";
        } else {
            result += ", id=" + id + '}';
        }
        return result;
    }
}