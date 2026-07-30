package ru.vk.education.boot.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.vk.education.boot.domain.Job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JobRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<Job> jobRowMapper = this::mapJob;

    public JobRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Job mapJob(ResultSet rs, int rowNum) throws SQLException {
        Job job = new Job(rs.getString("title"));
        job.setCompany(rs.getString("company"));
        job.setExp(rs.getInt("exp"));

        String tagsStr = rs.getString("tags");
        if (tagsStr != null && !tagsStr.isEmpty()) {
            job.setTags(new TreeSet<>(Arrays.asList(tagsStr.split(","))));
        }
        return job;
    }

    public Job save(Job job) {
        String tagsStr = String.join(",", job.getTags());
        // company может быть null, Map.of не принимает null, поэтому заменяем на ""
        String companyVal = job.getCompany() != null ? job.getCompany() : "";

        Map<String, Object> params = Map.of(
                "title", job.getTitle(),
                "company", companyVal,
                "exp", job.getExp(),
                "tags", tagsStr
        );

        if (!existsByTitle(job.getTitle())) {
            String sql = "INSERT INTO jobs (title, company, exp, tags) VALUES (:title, :company, :exp, :tags)";
            jdbcTemplate.update(sql, params);
        } else {
            String sql = "UPDATE jobs SET company = :company, exp = :exp, tags = :tags WHERE title = :title";
            jdbcTemplate.update(sql, params);
        }
        return findByTitle(job.getTitle()).orElse(job);
    }

    public Optional<Job> findByTitle(String title) {
        String sql = "SELECT * FROM jobs WHERE title = :title";
        List<Job> jobs = jdbcTemplate.query(sql, Map.of("title", title), jobRowMapper);
        return jobs.stream().findFirst();
    }

    public List<Job> findAll() {
        return jdbcTemplate.query("SELECT * FROM jobs", jobRowMapper);
    }

    public boolean existsByTitle(String title) {
        String sql = "SELECT COUNT(*) FROM jobs WHERE title = :title";
        Integer count = jdbcTemplate.queryForObject(sql, Map.of("title", title), Integer.class);
        return count != null && count > 0;
    }
}