package ru.vk.education.job;

import java.util.Comparator;
import java.util.Optional;

public class BestOfferFinder implements Runnable {

    @Override
    public void run() {
        // Для каждого пользователя находим лучшее предложение
        for (User user : UserAdd.users.values()) {
            findBestOfferForUser(user);
        }
    }

    private void findBestOfferForUser(User user) {
        Optional<Job> bestJob = JobAdd.jobs.values().stream()
                .max(Comparator.comparingDouble(job -> job.calculateScore(user)));

        bestJob.ifPresent(job -> {
            System.out.println(user.name + ", лучшее предложение — " +
                    job.getTitle() + " в " + job.getCompany());
        });
    }
}