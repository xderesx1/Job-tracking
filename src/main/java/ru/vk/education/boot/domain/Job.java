package ru.vk.education.boot.domain;

import java.util.Set;
import java.util.TreeSet;

public class Job {
    private String title;
    private String company;
    private int exp;
    private Set<String> tags = new TreeSet<>();

    public Job() {}

    public Job(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }

    public void addTag(String tag) { tags.add(tag); }

    public String toListString() {
        return title + " at " + company;
    }

    public double calculateScore(User user) {
        long matches = user.getSkills().stream()
                .filter(tags::contains)
                .count();
        double score = (double) matches;
        if (user.getExp() < this.exp) {
            score /= 2.0;
        }
        return score;
    }
}