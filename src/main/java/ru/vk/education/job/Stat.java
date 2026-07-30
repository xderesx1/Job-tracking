package ru.vk.education.job;
import java.util.*;
import java.util.stream.*;

public class Stat {

    public void jobsWithExp(int exp) {
        JobAdd.jobs.values().stream()
                .filter(job -> job.getExp() >= exp)
                .sorted(Comparator.comparing(Job::getTitle))
                .forEach(job -> System.out.println(job.toListString()));
    }

    public void usersWithMatches(int minMatches) {
        Map<User, Long> userMatchCounts = UserAdd.users.values().stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> JobAdd.jobs.values().stream()
                                .filter(job -> job.calculateScore(user) > 0)
                                .count()
                ));

        userMatchCounts.entrySet().stream()
                .filter(entry -> entry.getValue() >= minMatches)
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparing(User::toListString))
                .forEach(user -> System.out.println(user.toListString()));
    }

    public void topSkills(int n) {
        UserAdd.users.values().stream()
                .flatMap(user -> user.skills.stream())
                .collect(Collectors.groupingBy(
                        skill -> skill,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> {
                    int countCmp = Long.compare(e2.getValue(), e1.getValue());
                    if (countCmp != 0) return countCmp;
                    return e1.getKey().compareTo(e2.getKey());
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .sorted()
                .forEach(System.out::println);
    }
}