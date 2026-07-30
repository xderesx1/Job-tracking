package ru.vk.education.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.repository.JobRepository;

import java.util.*;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Job createJob(String title, String company, List<String> tags, int exp) {
        if (jobRepository.existsByTitle(title)) {
            return jobRepository.findByTitle(title).orElse(null);
        }
        Job job = new Job(title);
        job.setCompany(company);
        job.setExp(exp);
        if (tags != null) {
            for (String tag : tags) {
                if (tag != null && !tag.isEmpty()) {
                    job.addTag(tag);
                }
            }
        }
        return jobRepository.save(job);
    }

    public Job getJob(String title) {
        return jobRepository.findByTitle(title).orElse(null);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
}