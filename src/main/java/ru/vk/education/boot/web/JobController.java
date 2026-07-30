package ru.vk.education.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.service.JobService;

import java.util.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody JobRequest request) {
        Job job = jobService.createJob(request.title, request.company, request.tags, request.exp);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<Job>> listJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{title}")
    public ResponseEntity<Job> getJob(@PathVariable String title) {
        Job job = jobService.getJob(title);
        return job != null ? ResponseEntity.ok(job) : ResponseEntity.notFound().build();
    }

    public static class JobRequest {
        public String title;
        public String company;
        public List<String> tags = new ArrayList<>();
        public int exp = 0;
    }
}