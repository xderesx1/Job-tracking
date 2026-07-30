package ru.vk.education.boot.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.education.boot.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = this::mapUser;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getString("name"));
        user.setExp(rs.getInt("exp"));

        String skillsStr = rs.getString("skills");
        if (skillsStr != null && !skillsStr.isEmpty()) {
            user.setSkills(new TreeSet<>(Arrays.asList(skillsStr.split(","))));
        }
        return user;
    }

    public User save(User user) {
        String skillsStr = String.join(",", user.getSkills());
        Map<String, Object> params = Map.of(
                "name", user.getName(),
                "exp", user.getExp(),
                "skills", skillsStr
        );

        if (!existsByName(user.getName())) {
            String sql = "INSERT INTO users (name, exp, skills) VALUES (:name, :exp, :skills)";
            jdbcTemplate.update(sql, params);
        } else {
            String sql = "UPDATE users SET exp = :exp, skills = :skills WHERE name = :name";
            jdbcTemplate.update(sql, params);
        }
        return findByName(user.getName()).orElse(user);
    }

    public Optional<User> findByName(String name) {
        String sql = "SELECT * FROM users WHERE name = :name";
        List<User> users = jdbcTemplate.query(sql, Map.of("name", name), userRowMapper);
        return users.stream().findFirst();
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM users WHERE name = :name";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("name", name), Integer.class);
        return count != null && count > 0;
    }
}