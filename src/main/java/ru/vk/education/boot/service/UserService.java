package ru.vk.education.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vk.education.boot.domain.User;
import ru.vk.education.boot.repository.UserRepository;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(String name, List<String> skills, int exp) {
        if (userRepository.existsByName(name)) {
            return userRepository.findByName(name).orElse(null);
        }
        User user = new User(name);
        user.setExp(exp);
        if (skills != null) {
            for (String skill : skills) {
                if (skill != null && !skill.isEmpty()) {
                    user.addSkill(skill);
                }
            }
        }
        return userRepository.save(user);
    }

    public User getUser(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}