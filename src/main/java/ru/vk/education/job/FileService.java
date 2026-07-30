package ru.vk.education.job;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    private static final String FILE_NAME = "commands.txt";

    public void saveCommand(String command) {
        try {
            Files.write(
                    Paths.get(FILE_NAME),
                    (command + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            // Игнорируем
        }
    }

    public List<String> loadCommands() {
        List<String> commands = new ArrayList<>();
        try {
            if (Files.exists(Paths.get(FILE_NAME))) {
                commands = Files.readAllLines(Paths.get(FILE_NAME));
            }
        } catch (IOException e) {
            // Игнорируем
        }
        return commands;
    }
}