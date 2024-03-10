package ru.yandex.practicum.filmorate.storage;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistsException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Optional<User> get(int id) {
        String sql = String.format("SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                "FROM \"USERS\" u " +
                "WHERE u.\"ID\" = %d;", id);
        List<User> users = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs)));
        return (users.isEmpty() ? Optional.empty() : Optional.of(users.get(0)));
    }

    @Override
    public Optional<User> add(User user) {
        String sql = String.format("SELECT count(*) " +
                "FROM \"USERS\" u " +
                "WHERE u.\"LOGIN\" = '%s';", user.getLogin());
        if (jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0) == 0) {
            sql = String.format("INSERT INTO \"USERS\" (\"LOGIN\", \"NAME\", \"EMAIL\", \"BIRTHDAY\") " +
                    "SELECT '%s', '%s', '%s', '%s';", user.getLogin(), user.getName(), user.getEmail(),
                    user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            jdbcTemplate.update(sql);
            sql = String.format("SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                    "FROM \"USERS\" u " +
                    "WHERE \"LOGIN\" = '%s';", user.getLogin());
            List<User> users = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs)));
            return (users.isEmpty() ? Optional.empty() : Optional.of(users.get(0)));
        } else {
            throw new AlreadyExistsException("User with this login already exists.");
        }
    }

    @Override
    public Optional<User> update(User user) {
        String sql = String.format("UPDATE \"USERS\" SET \"LOGIN\" = '%s', \"NAME\" = '%s', \"EMAIL\" = '%s', " +
                        "\"BIRTHDAY\" = '%s' " +
                        "WHERE \"ID\" = %d", user.getLogin(), user.getName(), user.getEmail(),
                user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), user.getId());
        jdbcTemplate.update(sql);
        sql = String.format("SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                "FROM \"USERS\" u " +
                "WHERE u.\"ID\" = %d;", user.getId());
        List<User> users = new ArrayList<>(jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs)));
        return (users.isEmpty() ? Optional.empty() : Optional.of(users.get(0)));
    }

    @Override
    public void delete() {
        String sql = "DELETE FROM \"LIKES\";";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM \"FRIENDSHIP\";";
        jdbcTemplate.update(sql);
        sql = "DELETE FROM \"USERS\";";
        jdbcTemplate.update(sql);
    }

    @Override
    public Collection<User> getAll() {
        String sql = "SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                "FROM \"USERS\" u;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public void addToFriends(int id, int friendId) {
        String sql = String.format("INSERT INTO \"FRIENDSHIP\" (\"ID\", \"FRIEND_ID\", \"IS_CONFIRMED\") " +
                "VALUES ('%s', '%s', '%s');", id, friendId, false);
        jdbcTemplate.update(sql);
        correctFriends(id, friendId, true);
    }

    @Override
    public void deleteFromFriends(int id, int friendId) {
        String sql = String.format("DELETE FROM \"FRIENDSHIP\" " +
        "WHERE \"ID\" = %d AND \"FRIEND_ID\" = %d;", id, friendId);
        jdbcTemplate.update(sql);
        correctFriends(id, friendId, false);
    }

    @Override
    public Collection<User> getAllFriends(int id) {
        String sql = String.format("SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                "FROM \"USERS\" u " +
                "INNER JOIN \"FRIENDSHIP\" f ON u.\"ID\" = f.\"FRIEND_ID\" " +
                "WHERE f.\"ID\" = %d", id);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Collection<User> getCommonFriends(int id, int otherId) {
        String sql = String.format("SELECT u.\"ID\", u.\"LOGIN\", u.\"NAME\", u.\"EMAIL\", u.\"BIRTHDAY\" " +
                "FROM \"USERS\" u " +
                "INNER JOIN (SELECT f.\"FRIEND_ID\" " +
                "FROM \"FRIENDSHIP\" f " +
                "WHERE f.\"ID\" = %d) AS f_one " +
                "ON u.\"ID\" = f_one.\"FRIEND_ID\" " +
                "INNER JOIN (SELECT f.\"FRIEND_ID\" " +
                "FROM \"FRIENDSHIP\" f " +
                "WHERE f.\"ID\" = %d) AS f_two " +
                "ON u.\"ID\" = f_two.\"FRIEND_ID\";", id, otherId);
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String login = rs.getString("LOGIN");
        String name = rs.getString("NAME");
        String email = rs.getString("EMAIL");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        return new User(id, login, name, email, birthday);
    }

    private int makeCount(ResultSet rs) throws SQLException {
        return rs.getInt(1);
    }

    private void correctFriends(int id, int friendId, boolean option) {
        // подтверждение двух встречных заявок в друзья
        if (option) {
            String sql = String.format("SELECT count(*) " +
                    "FROM \"FRIENDSHIP\" f " +
                    "WHERE f.\"ID\" = %d AND f.\"FRIEND_ID\" = %d;", friendId, id);
            if (jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0) > 0) {
                sql = String.format("UPDATE \"FRIENDSHIP\" SET \"IS_CONFIRMED\" = true " +
                                "WHERE (\"ID\" = %d AND \"FRIEND_ID\" = %d) OR (\"ID\" = %d AND " +
                        "\"FRIEND_ID\" = %d);", id, friendId, friendId, id);
                jdbcTemplate.update(sql);
            }
        // понижение статуса заявки в друзья до неподтвержденной при отзыве встречной заявки
        } else {
            String sql = String.format("SELECT count(*) " +
                    "FROM \"FRIENDSHIP\" f " +
                    "WHERE f.\"ID\" = %d AND f.\"FRIEND_ID\" = %d AND f.\"IS_CONFIRMED\" = true;", friendId, id);
            if (jdbcTemplate.query(sql, (rs, rowNum) -> makeCount(rs)).get(0) > 0) {
                sql = String.format("UPDATE \"FRIENDSHIP\" SET \"IS_CONFIRMED\" = false " +
                        "WHERE \"ID\" = %d AND \"FRIEND_ID\" = %d;", friendId, id);
                jdbcTemplate.update(sql);
            }
        }
    }
}
