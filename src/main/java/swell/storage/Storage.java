package swell.storage;

import swell.exception.SwellException;
import swell.task.Deadline;
import swell.task.Event;
import swell.task.Task;
import swell.task.TaskList;
import swell.task.Todo;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

// Handles loading and saving Swell tasks
public class Storage {
    private static final Path DATA_FILE = Path.of("data", "swell.txt");
    private static final String SEPARATOR = " | ";

    // Loads tasks from the data file, creating the file first if needed
    public TaskList loadTasks() throws SwellException {
        ensureDataFileExists();

        ArrayList<Task> tasks = new ArrayList<>();
        try {
            for (String line : Files.readAllLines(DATA_FILE)) {
                if (!line.trim().isEmpty()) {
                    tasks.add(parseTask(line));
                }
            }
        } catch (IOException e) {
            throw new SwellException("I couldn't read the saved tasks, so I'm starting fresh.");
        }
        return new TaskList(tasks);
    }

    // Saves all tasks to the data file
    public void saveTasks(TaskList tasks) throws SwellException {
        ensureDataFileExists();

        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks.asList()) {
            lines.add(formatTask(task));
        }

        try {
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            throw new SwellException("I couldn't save the task list this time.");
        }
    }

    private void ensureDataFileExists() throws SwellException {
        try {
            Path parent = DATA_FILE.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (Files.notExists(DATA_FILE)) {
                Files.createFile(DATA_FILE);
            }
        } catch (IOException e) {
            throw new SwellException("I couldn't prepare the data file for saving tasks.");
        }
    }

    private String formatTask(Task task) {
        ArrayList<String> fields = new ArrayList<>();
        fields.add(task.getType().getSymbol());
        fields.add(task.isDone() ? "1" : "0");
        fields.add(encode(task.getDescription()));

        if (task instanceof Deadline) {
            fields.add(encode(((Deadline) task).getBy().toString()));
        } else if (task instanceof Event) {
            fields.add(encode(((Event) task).getFrom()));
            fields.add(encode(((Event) task).getTo()));
        }

        return String.join(SEPARATOR, fields);
    }

    private Task parseTask(String line) throws SwellException {
        String[] fields = line.split("\\s+\\|\\s+", -1);
        if (fields.length < 3) {
            throw new SwellException("Some saved tasks look corrupted, so I couldn't load them.");
        }

        Task task = createTaskFromFields(fields);
        if (fields[1].equals("1")) {
            task.markAsDone();
        } else if (!fields[1].equals("0")) {
            throw new SwellException("Some saved tasks have invalid done statuses.");
        }
        return task;
    }

    private Task createTaskFromFields(String[] fields) throws SwellException {
        String description = decode(fields[2]);
        switch (fields[0]) {
            case "T":
                if (fields.length != 3) {
                    throw new SwellException("A saved todo task has the wrong format.");
                }
                return new Todo(description);
            case "D":
                if (fields.length != 4) {
                    throw new SwellException("A saved deadline task has the wrong format.");
                }
                return new Deadline(description, parseSavedDate(fields[3]));
            case "E":
                if (fields.length != 5) {
                    throw new SwellException("A saved event task has the wrong format.");
                }
                return new Event(description, decode(fields[3]), decode(fields[4]));
            default:
                throw new SwellException("A saved task has an unknown task type.");
            }
    }

    private LocalDate parseSavedDate(String encodedDate) throws SwellException {
        try {
            return LocalDate.parse(decode(encodedDate));
        } catch (DateTimeParseException e) {
            throw new SwellException("A saved deadline task has an invalid date.");
        }
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }

    private String decode(String text) {
        return URLDecoder.decode(text, StandardCharsets.UTF_8);
    }
}
