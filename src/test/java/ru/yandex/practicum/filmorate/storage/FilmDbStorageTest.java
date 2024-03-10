package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    void getFilm() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);

        Film newFilm = new Film.FilmBuilder()
                .name("film name 1")
                .description("film description 1")
                .releaseDate(LocalDate.of(2010, 1,1))
                .duration(60)
                .genres(genres)
                .mpa(mpa)
                .build();
        newFilm.setId(filmStorage.addFilm(newFilm).getId());
        // вызываем тестируемый метод
        Optional<Film> optSavedFilm = filmStorage.getFilm(newFilm.getId());
        assertTrue(optSavedFilm.isPresent());
        Film savedFilm = optSavedFilm.get();
        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    void update() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);
        Film film = new Film.FilmBuilder()
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(2010, 1,1))
                .duration(60)
                .genres(genres)
                .mpa(mpa)
                .build();
        film.setId(filmStorage.addFilm(film).getId());
        // вызываем тестируемый метод
        Film anotherFilm = new Film.FilmBuilder()
                .id(film.getId())
                .name("film another name")
                .description("film another description")
                .releaseDate(LocalDate.of(2010, 2,2))
                .duration(120)
                .genres(genres)
                .mpa(mpa)
                .build();
        Optional<Film> optSavedFilm = filmStorage.updateFilm(anotherFilm);
        assertTrue(optSavedFilm.isPresent());
        Film savedFilm = optSavedFilm.get();
        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(anotherFilm);
    }

    @Test
    void delete() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);
        Film film = new Film.FilmBuilder()
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(2010, 1,1))
                .duration(60)
                .genres(genres)
                .mpa(mpa)
                .build();
        film.setId(filmStorage.addFilm(film).getId());
        Collection<Film> films = new ArrayList<>();
        // вызываем тестируемый метод
        filmStorage.deleteAllFilms();
        Collection<Film> savedFilms = filmStorage.getAllFilms();
        // проверяем утверждения
        assertThat(savedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @Test
    void getAll() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);
        Film filmOne = new Film.FilmBuilder()
                .name("film name 1")
                .description("film description 1")
                .releaseDate(LocalDate.of(2010, 1,1))
                .duration(60)
                .genres(genres)
                .mpa(mpa)
                .build();
        filmOne.setId(filmStorage.addFilm(filmOne).getId());
        Film filmTwo = new Film.FilmBuilder()
                .name("film name 2")
                .description("film description 2")
                .releaseDate(LocalDate.of(2010, 2,2))
                .duration(120)
                .genres(genres)
                .mpa(mpa)
                .build();
        filmTwo.setId(filmStorage.addFilm(filmTwo).getId());
        Collection<Film> films = new ArrayList<>();
        films.add(filmOne);
        films.add(filmTwo);
        // вызываем тестируемый метод
        Collection<Film> savedFilms = filmStorage.getAllFilms();
        // проверяем утверждения
        assertThat(savedFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @Test
    void addLike() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);
        Film filmOne = new Film.FilmBuilder()
                .name("film name 1")
                .description("film description 1")
                .releaseDate(LocalDate.of(2010, 1,1))
                .duration(60)
                .genres(genres)
                .mpa(mpa)
                .build();
        filmOne.setId(filmStorage.addFilm(filmOne).getId());
        Film filmTwo = new Film.FilmBuilder()
                .name("film name 2")
                .description("film description 2")
                .releaseDate(LocalDate.of(2010, 2,2))
                .duration(120)
                .genres(genres)
                .mpa(mpa)
                .build();
        filmTwo.setId(filmStorage.addFilm(filmTwo).getId());
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User userOne = new User.UserBuilder()
                .login("userLoginOne")
                .name("user Name One")
                .email("userOne@yandex.ru")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        userOne.setId((userStorage.addUser(userOne).getId()));
        User userTwo = new User.UserBuilder()
                .login("userLoginTwo")
                .name("user Name Two")
                .email("userTwo@yandex.ru")
                .birthday(LocalDate.of(1990, 2, 2))
                .build();
        userTwo.setId((userStorage.addUser(userTwo).getId()));
        Collection<Film> films = new ArrayList<>();
        films.add(filmTwo);
        // вызываем тестируемый метод
        filmStorage.addLike(filmOne.getId(), userOne.getId());
        filmStorage.addLike(filmTwo.getId(), userTwo.getId());
        filmStorage.addLike(filmTwo.getId(), userOne.getId());
        Collection<Film> popularFilms = filmStorage.getPopularFilms(1);
        // проверяем утверждения
        assertThat(popularFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
        // меняем условие - удаляем все лайки фильма newFilmTwo
        films.remove(filmTwo);
        films.add(filmOne);
        // вызываем тестируемый метод
        filmStorage.deleteLike(filmTwo.getId(), userTwo.getId());
        filmStorage.deleteLike(filmTwo.getId(), userOne.getId());
        popularFilms = filmStorage.getPopularFilms(1);
        // проверяем утверждения
        assertThat(popularFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }
}
