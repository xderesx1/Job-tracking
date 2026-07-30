package ru.vk.education.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.repository.JobRepository;
import ru.vk.education.boot.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SuggestService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getSuggestions(String username, int limit) {
        User user = userRepository.findByName(username).orElse(null);
        if (user == null) return Collections.emptyList();

        return jobRepository.findAll().stream()
                .map(job -> new AbstractMap.SimpleEntry<>(job, job.calculateScore(user)))
                .sorted((e1, e2) -> {
                    int cmp = Double.compare(e2.getValue(), e1.getValue());
                    return cmp != 0 ? cmp : e1.getKey().getTitle().compareTo(e2.getKey().getTitle());
                })
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}