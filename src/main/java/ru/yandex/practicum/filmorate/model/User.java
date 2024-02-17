package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"id"})
public class User {
    private final String login;
    private String name;
    private final String email;
    private final LocalDate birthday;
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