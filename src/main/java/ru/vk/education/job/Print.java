package ru.vk.education.job;
import java.util.List;

public class Print {
    public void printJobList(){
        for (Job job : JobAdd.jobs.values()){
            System.out.println(job.toListString());
        }
    }

    public void printUserList(){
        for (User user : UserAdd.users.values()){
            System.out.println(user.toListString());
        }
    }

    public void printHistory(List<String> commands) {
        for (String command : commands) {
            System.out.println(command);
        }
    }
}