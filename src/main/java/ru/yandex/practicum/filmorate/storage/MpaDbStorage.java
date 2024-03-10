package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Primary
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Rating> getAllMpa() {
        String sql = "SELECT \"ID\", \"NAME\" " +
                "FROM \"RATINGS\" " +
                "ORDER BY \"ID\";";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs));
    }

    @Override
    public Optional<Rating> getMpa(int id) {
        String sql = String.format("SELECT \"ID\", \"NAME\" " +
                "FROM \"RATINGS\" " +
                "WHERE \"ID\" = %d;", id);
        List<Rating> ratings = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeRating(rs)));
        return (ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0)));
    }

    private Rating makeRating(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String name = rs.getString("NAME");
        return new Rating(id, name);
    }
}