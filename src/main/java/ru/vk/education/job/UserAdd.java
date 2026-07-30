package ru.vk.education.job;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UserAdd {
    public static Map<String, User> users = new TreeMap<>();

    public void createUser(String[] parts){
        if (parts.length < 2) return;
        String name = parts[1];

        if (users.containsKey(name)) return;

        User user = new User(name);

        for (int i = 2; i < parts.length; i++){
            String part = parts[i];
            if (part.startsWith("--skills=")){
                String skillsStr = part.substring("--skills=".length());
                if (!skillsStr.isEmpty()){
                    String[] skills = skillsStr.split(",");
                    for (String skill : skills){
                        if (!skill.isEmpty()){
                            user.addSkills(skill);
                        }
                    }
                }
            } else if (part.startsWith("--exp=")) {
                String expStr = part.substring("--exp=".length());
                try {
                    user.setExp(Integer.parseInt(expStr));
                } catch (NumberFormatException e) {
                    // игнорируем
                }
            }
        }
        users.put(name, user);
    }
}