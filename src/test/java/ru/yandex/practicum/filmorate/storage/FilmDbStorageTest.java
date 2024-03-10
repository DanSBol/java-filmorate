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
    void get() {
        // Подготавливаем данные для теста
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        Rating mpa = new Rating(1, "G");
        Genre genreOne = new Genre(1, "Комедия");
        Genre genreTwo = new Genre(2, "Драма");
        Set<Genre> genres = new HashSet<>();
        genres.add(genreOne);
        genres.add(genreTwo);
        Film newFilm = new Film("film name 1", "film description 1",
                LocalDate.of(2010, 1,1), 60, genres, mpa);
        newFilm.setId(filmStorage.add(newFilm).getId());
        // вызываем тестируемый метод
        Optional<Film> optSavedFilm = filmStorage.get(newFilm.getId());
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
        Film newFilm = new Film("film name 1", "film description 1",
                LocalDate.of(2010, 1,1), 60, genres, mpa);
        newFilm.setId(filmStorage.add(newFilm).getId());
        // вызываем тестируемый метод
        Film anotherFilm = new Film(newFilm.getId(), "film another name", "film another description",
                newFilm.getReleaseDate(), newFilm.getDuration(), genres, mpa);
        Optional<Film> optSavedFilm = filmStorage.update(anotherFilm);
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
        Film newFilm = new Film("film name 1", "film description 1",
                LocalDate.of(2010, 1,1), 60, genres, mpa);
        newFilm.setId(filmStorage.add(newFilm).getId());
        Collection<Film> films = new ArrayList<>();
        // вызываем тестируемый метод
        filmStorage.delete();
        Collection<Film> savedFilms = filmStorage.getAll();
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
        Film newFilmOne = new Film("film name 1", "film description 1",
                LocalDate.of(2010, 1,1), 60, genres, mpa);
        newFilmOne.setId(filmStorage.add(newFilmOne).getId());
        Film newFilmTwo = new Film("film name 2", "film description 2",
                LocalDate.of(2010, 2,2), 120, genres, mpa);
        newFilmTwo.setId(filmStorage.add(newFilmTwo).getId());
        Collection<Film> films = new ArrayList<>();
        films.add(newFilmOne);
        films.add(newFilmTwo);
        // вызываем тестируемый метод
        Collection<Film> savedFilms = filmStorage.getAll();
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
        Film newFilmOne = new Film("film name 1", "film description 1",
                LocalDate.of(2010, 1,1), 60, genres, mpa);
        newFilmOne.setId(filmStorage.add(newFilmOne).getId());
        Film newFilmTwo = new Film("film name 2", "film description 2",
                LocalDate.of(2010, 2,2), 120, genres, mpa);
        newFilmTwo.setId(filmStorage.add(newFilmTwo).getId());
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        User newUserOne = new User("vanya123", "Ivan Petrov", "userOne@email.ru",
                LocalDate.of(1990, 1, 1));
        newUserOne.setId(userStorage.add(newUserOne).orElseThrow().getId());
        User newUserTwo = new User("petya456", "Petr Ivanov", "userTwo@email.ru",
                LocalDate.of(2000, 2, 2));
        newUserTwo.setId(userStorage.add(newUserTwo).orElseThrow().getId());
        Collection<Film> films = new ArrayList<>();
        films.add(newFilmTwo);
        // вызываем тестируемый метод
        filmStorage.addLike(newFilmOne.getId(), newUserOne.getId());
        filmStorage.addLike(newFilmTwo.getId(), newUserTwo.getId());
        filmStorage.addLike(newFilmTwo.getId(), newUserOne.getId());
        Collection<Film> popularFilms = filmStorage.getPopular(1);
        // проверяем утверждения
        assertThat(popularFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
        // меняем условие - удаляем все лайки фильма newFilmTwo
        films.remove(newFilmTwo);
        films.add(newFilmOne);
        // вызываем тестируемый метод
        filmStorage.deleteLike(newFilmTwo.getId(), newUserTwo.getId());
        filmStorage.deleteLike(newFilmTwo.getId(), newUserOne.getId());
        popularFilms = filmStorage.getPopular(1);
        // проверяем утверждения
        assertThat(popularFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }
}
