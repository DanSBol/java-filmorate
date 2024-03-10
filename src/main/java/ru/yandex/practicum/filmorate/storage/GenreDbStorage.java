package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

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

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt(1), rs.getString(2));
    }
}