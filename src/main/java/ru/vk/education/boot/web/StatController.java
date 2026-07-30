package ru.vk.education.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.service.StatService;

import java.util.List;

@RestController
@RequestMapping("/api/stat")
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping("/exp/{exp}")
    public ResponseEntity<List<Job>> jobsWithExp(@PathVariable int exp) {
        return ResponseEntity.ok(statService.jobsWithMinimumExp(exp));
    }

    @GetMapping("/match/{n}")
    public ResponseEntity<List<User>> usersWithMatches(@PathVariable int n) {
        return ResponseEntity.ok(statService.usersWithMinimumMatches(n));
    }

    @GetMapping("/top-skills/{n}")
    public ResponseEntity<List<String>> topSkills(@PathVariable int n) {
        return ResponseEntity.ok(statService.getTopSkills(n));
    }
}