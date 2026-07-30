package ru.vk.education.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TestApp {
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
        System.out.println("Job Recommender API запущен на порту 8080");
    }
}