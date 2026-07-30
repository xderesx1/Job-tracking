package ru.vk.education.boot.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserRequest request) {
        User user = userService.createUser(request.name, request.skills, request.exp);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{name}")
    public ResponseEntity<User> getUser(@PathVariable String name) {
        User user = userService.getUser(name);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    public static class UserRequest {
        public String name;
        public List<String> skills = new ArrayList<>();
        public int exp = 0;
    }
}