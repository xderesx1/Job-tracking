package ru.vk.education.boot.domain;

import java.util.Set;
import java.util.TreeSet;

public class User {
    public String name;
    private int exp;
    public Set<String> skills = new TreeSet<>();

    public User() {}

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }

    public Set<String> getSkills() { return skills; }
    public void setSkills(Set<String> skills) { this.skills = skills; }

    public void addSkill(String skill) { skills.add(skill); }

    public String toListString() {
        return name + " " + String.join(",", skills) + " " + exp;
    }
}