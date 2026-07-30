package ru.vk.education.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.boot.domain.Job;
import ru.vk.education.boot.service.SuggestService;

import java.util.List;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    private SuggestService matchService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Job>> suggestJobs(@PathVariable String username) {
        List<Job> suggestions = matchService.getSuggestions(username, 2);
        return ResponseEntity.ok(suggestions);
    }
}