package ru.vk.education.job;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.repository.JobRepository;
import ru.vk.education.boot.repository.UserRepository;
import ru.vk.education.boot.service.SuggestService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private SuggestService suggestService;

    @Test
    void suggestTest() {
        // Arrange: создаём пользователя и несколько вакансий
        User user = new User("alice");
        user.setExp(3);
        user.addSkill("java");
        user.addSkill("spring");

        Job job1 = new Job("Backend_Dev");
        job1.setCompany("VK");
        job1.setExp(2);
        job1.addTag("java");
        job1.addTag("spring");
        job1.addTag("backend");

        Job job2 = new Job("Frontend_Dev");
        job2.setCompany("Yandex");
        job2.setExp(1);
        job2.addTag("javascript");
        job2.addTag("react");

        Job job3 = new Job("Java_Developer");
        job3.setCompany("Google");
        job3.setExp(3);
        job3.addTag("java");
        job3.addTag("kotlin");

        when(userRepository.findByName("alice")).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(Arrays.asList(job1, job2, job3));

        // Act
        List<Job> result = suggestService.getSuggestions("alice", 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        // Первая вакансия должна быть Backend_Dev (2 совпадения: java, spring)
        assertEquals("Backend_Dev", result.get(0).getTitle());
        // Вторая вакансия должна быть Java_Developer (1 совпадение: java)
        assertEquals("Java_Developer", result.get(1).getTitle());
        assertFalse(result.contains("Frontend_Dev"));

        verify(userRepository, times(1)).findByName("alice");
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void emptyVacanciesTest() {
        // Arrange: пользователь есть, но вакансий нет
        User user = new User("bob");
        user.setExp(5);
        user.addSkill("python");

        when(userRepository.findByName("bob")).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Job> result = suggestService.getSuggestions("bob", 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByName("bob");
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void singleVacancyTest() {
        // Arrange: пользователь и одна вакансия
        User user = new User("charlie");
        user.setExp(2);
        user.addSkill("go");

        Job job = new Job("Golang_Developer");
        job.setCompany("Tinkoff");
        job.setExp(1);
        job.addTag("go");
        job.addTag("microservices");

        when(userRepository.findByName("charlie")).thenReturn(Optional.of(user));
        when(jobRepository.findAll()).thenReturn(Collections.singletonList(job));

        // Act
        List<Job> result = suggestService.getSuggestions("charlie", 2);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Golang_Developer", result.get(0).getTitle());
        assertEquals("Tinkoff", result.get(0).getCompany());

        verify(userRepository, times(1)).findByName("charlie");
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    void userNotFoundTest() {
        // Arrange: пользователя не существует
        when(userRepository.findByName("nonexistent")).thenReturn(Optional.empty());

        // Act
        List<Job> result = suggestService.getSuggestions("nonexistent", 2);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByName("nonexistent");
        verify(jobRepository, never()).findAll();
    }
}