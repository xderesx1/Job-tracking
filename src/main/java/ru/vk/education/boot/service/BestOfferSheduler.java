package ru.vk.education.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.repository.JobRepository;
import ru.vk.education.boot.repository.UserRepository;

import java.util.Comparator;
import java.util.Optional;

@Component
public class BestOfferSheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @Scheduled(fixedRate = 60_000, initialDelay = 0)
    public void findBestOffersForAllUsers() {
        for (User user : userRepository.findAll()) {
            findBestOfferForUser(user);
        }
    }

    private void findBestOfferForUser(User user) {
        Optional<Job> bestJob = jobRepository.findAll().stream()
                .max(Comparator.comparingDouble(job -> job.calculateScore(user)));

        bestJob.ifPresent(job -> {
            System.out.println(user.getName() + ", лучшее предложение — " +
                    job.getTitle() + " в " + job.getCompany());
        });
    }
}