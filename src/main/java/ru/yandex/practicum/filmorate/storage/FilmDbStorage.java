package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT \"ID\", \"NAME\" " +
                "FROM \"GENRES\" " +
                "ORDER BY \"ID\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> getGenre(int id) {
        String sql = String.format("SELECT \"ID\", \"NAME\" " +
                "FROM \"GENRES\" " +
                "WHERE \"ID\" = %d;", id);
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs)));
        return (genres.isEmpty() ? Optional.empty() : Optional.of(genres.get(0)));
    }

    @Override
    public Collection<Rating> getAllRatings() {
        String sql = "SELECT \"ID\", \"NAME\" " +
                "FROM \"RATINGS\" " +
                "ORDER BY \"ID\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Optional<Rating> getRating(int id) {
        String sql = String.format("SELECT \"ID\", \"NAME\" " +
                "FROM \"RATINGS\" " +
                "WHERE \"ID\" = %d;", id);
        List<Rating> ratings = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs)));
        return (ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0)));
    }

    @Override
    public Optional<Film> get(int id) {
        String sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", " +
                "f.\"DURATION\", r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                "FROM \"FILMS\" f " +
                "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                "WHERE f.\"ID\" = %d;", id);
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));
        return (films.isEmpty() ? Optional.empty() : Optional.of(films.get(0)));
    }

    @Override
    public Film add(Film film) {
            String sql = String.format("INSERT INTO \"FILMS\" (\"NAME\", \"DESCRIPTION\", \"RELEASE_DATE\", " +
                    "\"DURATION\", \"MPA\") " +
                    "VALUES ('%s', '%s', '%s', %d, %d);",
                    film.getName(), film.getDescription(),
                    film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), film.getDuration(),
                    film.getMpa().getId());
            jdbcTemplate.update(sql);

            sql = String.format("SELECT f.\"ID\" " +
                    "FROM \"FILMS\" f " +
                    "WHERE f.\"NAME\" = '%s' AND f.\"DESCRIPTION\" = '%s' AND " +
                    "f.\"RELEASE_DATE\" = '%s' AND f.\"DURATION\" = %d;",
                    film.getName(), film.getDescription(),
                    film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), film.getDuration());

            int filmId = jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0);

            Set<Genre> newGenres = film.getGenres();
            for (Genre item : film.getGenres()) {
                    sql = String.format("INSERT INTO \"FILM_GENRE\" (\"FILM_ID\", \"GENRE_ID\") " +
                            "VALUES (%d, %d);", filmId, item.getId());
                    jdbcTemplate.update(sql);

                    sql = String.format("SELECT \"ID\", \"NAME\" FROM \"GENRES\" " +
                            "WHERE \"ID\" = %d;", item.getId());
                    Genre newGenre = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs)).get(0);
                    item.setName(newGenre.getName());
            }

            sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", f.\"DURATION\", " +
                    "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                    "FROM \"FILMS\" f " +
                    "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                    "WHERE f.\"NAME\" = '%s' AND f.\"DESCRIPTION\" = '%s' AND " +
                    "f.\"RELEASE_DATE\" = '%s' AND f.\"DURATION\" = %d;", film.getName(), film.getDescription(),
                    film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), film.getDuration());
            jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
            List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));

            film = films.get(0);
            film.setGenres(newGenres);
            return film;
    }

    @Override
    public Optional<Film> update(Film film) {
        String sql = String.format("UPDATE \"FILMS\" SET \"NAME\" = '%s', \"DESCRIPTION\" = '%s', " +
                "\"RELEASE_DATE\" = '%s', \"DURATION\" = %d, \"MPA\" = %d " +
                "WHERE \"ID\" = %d", film.getName(), film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), film.getDuration(),
                film.getMpa().getId(), film.getId());
        jdbcTemplate.update(sql);
        sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", f.\"DURATION\", " +
                "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                "FROM \"FILMS\" f " +
                "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                "WHERE f.\"ID\" = %d;", film.getId());
        List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));
        return (films.isEmpty() ? Optional.empty() : Optional.of(films.get(0)));
    }

    @Override
    public void delete() {
        String sql = "DELETE FROM \"LIKES\";";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM \"FILM_GENRE\";";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM \"FILMS\";";
        jdbcTemplate.update(sql);
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", f.\"DURATION\", " +
                "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                "FROM \"FILMS\" f " +
                "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Optional<Film> addLike(int id, int userId) {
        String sql = String.format("SELECT count(*) " +
                "FROM \"LIKES\" l " +
                "WHERE l.\"FILM_ID\" = '%s' AND l.\"USER_ID\" = '%s';", id, userId);
        if (jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0) == 0) {
            sql = String.format("INSERT INTO \"LIKES\" (\"FILM_ID\", \"USER_ID\") " +
                    "VALUES (%d, %d);", id, userId);
            jdbcTemplate.update(sql);
            sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", f.\"DURATION\", " +
                    "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                    "FROM \"FILMS\" f " +
                    "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                    "WHERE f.\"ID\" = %d;", id);
            List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));
            return (films.isEmpty() ? Optional.empty() : Optional.of(films.get(0)));
        } else {
            throw new AlreadyExistsException("Like already exists.");
        }
    }

    @Override
    public Optional<Film> deleteLike(int id, int userId) {
        String sql = String.format("SELECT count(*) " +
                "FROM \"LIKES\" l " +
                "WHERE l.\"FILM_ID\" = %d AND l.\"USER_ID\" = %d;", id, userId);
        if (jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0) > 0) {
            sql = String.format("DELETE FROM \"LIKES\" l " +
                    "WHERE l.\"FILM_ID\" = %d AND l.\"USER_ID\" = %d;", id, userId);
            jdbcTemplate.update(sql);
            sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", f.\"DURATION\", " +
                    "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                    "FROM \"FILMS\" f " +
                    "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                    "WHERE f.\"ID\" = %d;", id);
            List<Film> films = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs)));
            return (films.isEmpty() ? Optional.empty() : Optional.of(films.get(0)));
        } else {
            throw new NotFoundException("Like not found.");
        }
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String sql = String.format("SELECT f.\"ID\", f.\"NAME\", f.\"DESCRIPTION\", f.\"RELEASE_DATE\", " +
                "f.\"DURATION\", " +
                "r.\"ID\" \"MPA_ID\", r.\"NAME\" \"MPA_NAME\" " +
                "FROM \"FILMS\" f " +
                "INNER JOIN \"RATINGS\" r ON f.\"MPA\" = r.\"ID\" " +
                "INNER JOIN (SELECT TOP %d f2.\"ID\", count(LIKES.*) \"num\" " +
                "FROM \"FILMS\" f2 " +
                "LEFT JOIN \"LIKES\" ON f2.\"ID\" = LIKES.\"FILM_ID\" " +
                "GROUP BY f2.\"ID\" " +
                "ORDER BY \"num\" DESC) AS t ON f.\"ID\" = t.\"ID\";", count);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setMpa(new Rating(rs.getInt("MPA_ID"), rs.getString("MPA_NAME")));
        film.setGenres(getGenres(film.getId()));
        return film;
    }

    private int makeCount(ResultSet rs) throws SQLException {
        return rs.getInt(1);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt(1));
        genre.setName(rs.getString(2));
        return genre;
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        return new Rating(id, name);
    }

    private Set<Genre> getGenres(int id) {
        String sql = String.format("SELECT fg.\"GENRE_ID\", g.\"NAME\" " +
                "FROM \"FILM_GENRE\" fg " +
                "INNER JOIN \"GENRES\" g ON fg.\"GENRE_ID\" = g.\"ID\" " +
                "WHERE fg.\"FILM_ID\" = %d;", id);
        return new HashSet<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs)));
    }
}