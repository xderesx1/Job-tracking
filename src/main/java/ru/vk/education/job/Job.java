package ru.vk.education.job;
import java.util.Set;
import java.util.TreeSet;

public class Job {
    private String title;
    private String company;
    private int exp;
    private Set<String> tags = new TreeSet<>();

    public Job(String title){
        this.title = title;
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public void setExp(int exp){
        this.exp = exp;
    }

    public void setCompany(String company){
        this.company = company;
    }

    public String toListString(){
        return title + " at " + company;
    }

    public double calculateScore(User user){
        double matches = 0;
        for (String skill : user.skills){
            if (this.tags.contains(skill)){
                matches++;
            }
        }
        double score = matches;
        if (user.getExp() < this.exp){
            score /= 2.0;
        }
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public int getExp() {
        return exp;
    }
}