package ru.vk.education.job;
import java.util.Set;
import java.util.TreeSet;

public class User {
    public String name;
    private int exp;
    public Set<String> skills = new TreeSet<>();

    public User(String name){
        this.name = name;
    }

    public void addSkills(String skill){
        skills.add(skill);
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public String toListString(){
        return name + " " + String.join(",", skills) + " " + exp;
    }

    public int getExp() {
        return exp;
    }
}