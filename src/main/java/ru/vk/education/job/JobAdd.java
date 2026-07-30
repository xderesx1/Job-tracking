package ru.vk.education.job;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class JobAdd {
    public static Map<String, Job> jobs = new TreeMap<>();

    public void createJob(String[] parts){
        if (parts.length < 2) return;
        String title = parts[1];

        if (jobs.containsKey(title)) return;

        Job job = new Job(title);

        for (int i = 2; i < parts.length; i++){
            String part = parts[i];
            if (part.startsWith("--company=")){
                job.setCompany(part.substring("--company=".length()));
            } else if (part.startsWith("--tags=")) {
                String tagsStr = part.substring("--tags=".length());
                if (!tagsStr.isEmpty()){
                    String[] tags = tagsStr.split(",");
                    for (String tag : tags){
                        if (!tag.isEmpty()){
                            job.addTag(tag);
                        }
                    }
                }
            } else if (part.startsWith("--exp=")) {
                String expStr = part.substring("--exp=".length());
                try {
                    job.setExp(Integer.parseInt(expStr));
                } catch (NumberFormatException e){
                    // игнорируем
                }
            }
        }
        jobs.put(title, job);
    }
}