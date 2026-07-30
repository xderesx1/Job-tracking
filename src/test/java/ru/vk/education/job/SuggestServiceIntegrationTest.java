package ru.vk.education.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.repository.JobRepository;
import ru.vk.education.boot.repository.UserRepository;
import ru.vk.education.boot.service.SuggestService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(classes = ru.vk.education.boot.TestApp.class)
class SuggestServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private SuggestService suggestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Test
    void suggestIntegrationTest() {
        // Arrange: создаём пользователей и вакансии в реальной БД
        User user1 = new User("alice");
        user1.setExp(3);
        user1.addSkill("java");
        user1.addSkill("spring");
        user1.addSkill("postgresql");
        userRepository.save(user1);

        User user2 = new User("bob");
        user2.setExp(5);
        user2.addSkill("python");
        user2.addSkill("django");
        userRepository.save(user2);

        Job job1 = new Job("Backend_Java");
        job1.setCompany("VK");
        job1.setExp(2);
        job1.addTag("java");
        job1.addTag("spring");
        job1.addTag("postgresql");
        jobRepository.save(job1);

        Job job2 = new Job("Backend_Python");
        job2.setCompany("Yandex");
        job2.setExp(3);
        job2.addTag("python");
        job2.addTag("django");
        job2.addTag("redis");
        jobRepository.save(job2);

        Job job3 = new Job("Frontend_React");
        job3.setCompany("Google");
        job3.setExp(1);
        job3.addTag("javascript");
        job3.addTag("react");
        job3.addTag("css");
        jobRepository.save(job3);

        // Act: получаем рекомендации для alice
        List<Job> suggestions = suggestService.getSuggestions("alice", 2);

        // Assert
        assertNotNull(suggestions);
        assertEquals(2, suggestions.size());

        // Первая вакансия должна быть Backend_Java (3 совпадения: java, spring, postgresql)
        assertEquals("Backend_Java", suggestions.get(0).getTitle());
        assertEquals("VK", suggestions.get(0).getCompany());

        // Вторая вакансия может быть Backend_Python или Frontend_React (0 совпадений)
        // Проверяем, что вакансии существуют в системе
        assertTrue(
                suggestions.get(1).getTitle().equals("Backend_Python") ||
                        suggestions.get(1).getTitle().equals("Frontend_React")
        );

        // Дополнительно: проверяем рекомендации для bob
        List<Job> bobSuggestions = suggestService.getSuggestions("bob", 2);
        assertNotNull(bobSuggestions);
        assertEquals(2, bobSuggestions.size());
        assertEquals("Backend_Python", bobSuggestions.get(0).getTitle());
    }
}
