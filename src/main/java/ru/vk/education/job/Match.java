package ru.vk.education.job;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private static class JobMatch {
        Job job;
        double score;

        JobMatch(Job job, double score) {
            this.job = job;
            this.score = score;
        }
    }

    public void suggestJobs(String[] parts){
        if (parts.length < 2) return;
        String username = parts[1];

        User user = UserAdd.users.get(username);
        if (user == null) return;

        List<JobMatch> matches = new ArrayList<>();
        for (Job job : JobAdd.jobs.values()){
            double score = job.calculateScore(user);
            matches.add(new JobMatch(job, score));
        }

        matches.sort((m1, m2) -> {
            int scoreCmp = Double.compare(m2.score, m1.score);
            if (scoreCmp != 0) return scoreCmp;
            return m1.job.getTitle().compareTo(m2.job.getTitle());
        });

        int limit = Math.min(2, matches.size());
        for (int i = 0; i < limit; i++){
            System.out.println(matches.get(i).job.toListString());
        }
    }
}