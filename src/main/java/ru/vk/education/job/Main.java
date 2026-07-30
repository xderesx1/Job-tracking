package ru.vk.education.job;
import java.util.Scanner;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static ExecutorService executorService;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Print print = new Print();
        JobAdd jobAdd = new JobAdd();
        UserAdd userAdd = new UserAdd();
        Match match = new Match();
        Stat stat = new Stat();
        FileService fileService = new FileService();

        // Загружаем команды из файла и выполняем только user и job
        List<String> savedCommands = fileService.loadCommands();
        for (String command : savedCommands) {
            String[] parts = command.trim().split("\\s+");
            if (parts.length > 0) {
                String cmd = parts[0];
                if (cmd.equals("user")) {
                    userAdd.createUser(parts);
                } else if (cmd.equals("job")) {
                    jobAdd.createJob(parts);
                }
            }
        }

        // Создаем ExecutorService с одним потоком
        executorService = Executors.newSingleThreadScheduledExecutor();

        // Планируем периодическое выполнение задачи поиска лучших предложений
        // Запускаем сразу (через 0 секунд) и затем каждые 60 секунд (1 минута)
        ScheduledExecutorService scheduledExecutor = (ScheduledExecutorService) executorService;
        BestOfferFinder bestOfferFinder = new BestOfferFinder();
        scheduledExecutor.scheduleAtFixedRate(bestOfferFinder, 0, 60, TimeUnit.SECONDS);

        // Добавляем хук для корректного завершения при выходе
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdownExecutor();
        }));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String command = parts[0];

            switch (command) {
                case "user":
                    userAdd.createUser(parts);
                    fileService.saveCommand(line);
                    break;
                case "user-list":
                    print.printUserList();
                    fileService.saveCommand(line);
                    break;
                case "job":
                    jobAdd.createJob(parts);
                    fileService.saveCommand(line);
                    break;
                case "job-list":
                    print.printJobList();
                    fileService.saveCommand(line);
                    break;
                case "suggest":
                    match.suggestJobs(parts);
                    fileService.saveCommand(line);
                    break;
                case "history":
                    print.printHistory(fileService.loadCommands());
                    fileService.saveCommand(line);
                    break;
                case "stat":
                    handleStatCommand(stat, parts);
                    fileService.saveCommand(line);
                    break;
                case "exit":
                    // Корректно завершаем ExecutorService перед выходом
                    shutdownExecutor();
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }

    private static void handleStatCommand(Stat stat, String[] parts) {
        for (int i = 1; i < parts.length; i++) {
            if (parts[i].equals("--exp") && i + 1 < parts.length) {
                try {
                    int exp = Integer.parseInt(parts[i + 1]);
                    stat.jobsWithExp(exp);
                } catch (NumberFormatException e) {
                    // игнорируем
                }
                break;
            } else if (parts[i].equals("--match") && i + 1 < parts.length) {
                try {
                    int matchCount = Integer.parseInt(parts[i + 1]);
                    stat.usersWithMatches(matchCount);
                } catch (NumberFormatException e) {
                    // игнорируем
                }
                break;
            } else if (parts[i].equals("--top-skills") && i + 1 < parts.length) {
                try {
                    int n = Integer.parseInt(parts[i + 1]);
                    stat.topSkills(n);
                } catch (NumberFormatException e) {
                    // игнорируем
                }
                break;
            }
        }
    }

    private static void shutdownExecutor() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                // Ждем завершения до 5 секунд
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    // Если не успел завершиться, пробуем принудительно
                    executorService.shutdownNow();
                    // Ждем еще немного
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        System.err.println("ExecutorService не завершился корректно");
                    }
                }
            } catch (InterruptedException e) {
                // Если прервали ожидание, пробуем принудительно завершить
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}