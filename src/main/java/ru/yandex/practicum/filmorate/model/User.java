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
@NoArgsConstructor
public class User {
    @NotNull(message = "User login invalid.")
    @Pattern(regexp = "^\\S+$", message = "User login invalid.")
    private String login;
    private String name;
    @Email(message = "User Email invalid.")
    private String email;
    @PastOrPresent(message = "User birthday invalid.")
    private LocalDate birthday;
    @NotNull
    private int id;
    private Set<Friendship> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.id = 0;
        this.friends = new HashSet<>();
    }

    public User(int id, String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.id = id;
        this.friends = new HashSet<>();
    }
}
