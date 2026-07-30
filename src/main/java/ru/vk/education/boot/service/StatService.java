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
public class StatService {
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Job> jobsWithMinimumExp(int exp) {
        return jobRepository.findAll().stream()
                .filter(job -> job.getExp() >= exp)
                .sorted(Comparator.comparing(Job::getTitle))
                .collect(Collectors.toList());
    }

    public List<User> usersWithMinimumMatches(int minMatches) {
        Map<User, Long> matchCounts = userRepository.findAll().stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> jobRepository.findAll().stream()
                                .filter(job -> job.calculateScore(user) > 0)
                                .count()
                ));

        return matchCounts.entrySet().stream()
                .filter(e -> e.getValue() >= minMatches)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(User::toListString))
                .collect(Collectors.toList());
    }

    public List<String> getTopSkills(int n) {
        return userRepository.findAll().stream()
                .flatMap(user -> user.getSkills().stream())
                .collect(Collectors.groupingBy(
                        skill -> skill,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> {
                    int cmp = Long.compare(e2.getValue(), e1.getValue());
                    return cmp != 0 ? cmp : e1.getKey().compareTo(e2.getKey());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }
}