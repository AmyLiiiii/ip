package swell;

import java.util.Scanner;

import swell.exception.SwellException;
import swell.parser.Parser;
import swell.storage.Storage;
import swell.task.Task;
import swell.task.TaskList;
import swell.ui.Ui;

/**
 * Starts the Swell chatbot.
 */
public class Swell {
    /**
     * Prevents instantiation of this utility class.
     */
    private Swell() {
    }

    /**
     * Runs the chatbot until the user enters the bye command.
     *
     * @param args command line arguments supplied to the program.
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage();
        TaskList tasks = loadTasks(storage, ui);

        try (Scanner scanner = new Scanner(System.in)) {
            ui.printGreeting();

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.equals("bye")) {
                    break;
                }

                try {
                    boolean shouldSave = processCommand(tasks, command, ui);
                    if (shouldSave) {
                        storage.saveTasks(tasks);
                    }
                } catch (SwellException e) {
                    ui.printError(e.getMessage());
                }
            }

            ui.printGoodbye();
        }
    }

    /**
     * Loads saved tasks without stopping Swell if the data file cannot be read.
     *
     * @param storage storage component used to load saved tasks.
     * @param ui user interface component used to show load errors.
     * @return loaded task list, or an empty task list if loading fails.
     */
    private static TaskList loadTasks(Storage storage, Ui ui) {
        try {
            return storage.loadTasks();
        } catch (SwellException e) {
            ui.printError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Processes one user command.
     *
     * @param tasks task list to update.
     * @param command user command to process.
     * @param ui user interface component used to show command results.
     * @return true if the task list should be saved after processing.
     * @throws SwellException if the command is invalid.
     */
    private static boolean processCommand(TaskList tasks, String command, Ui ui) throws SwellException {
        if (command.isEmpty()) {
            throw new SwellException("I'm ready when you are. Try a command like todo read book.");
        }

        String commandWord = Parser.getCommandWord(command);

        switch (commandWord) {
            case "list":
                ui.printTasks(tasks);
                return false;
            case "todo":
            case "deadline":
            case "event":
                addTask(tasks, command, ui);
                return true;
            case "mark":
                ui.printTaskMarked(tasks.markTask(Parser.getTaskNumber(command, "mark")));
                return true;
            case "unmark":
                ui.printTaskUnmarked(tasks.unmarkTask(Parser.getTaskNumber(command, "unmark")));
                return true;
            case "delete":
                ui.printTaskDeleted(tasks.deleteTask(Parser.getTaskNumber(command, "delete")), tasks.size());
                return true;
            default:
                throw new SwellException(
                        "I don't know that command yet. Try todo, deadline, event, list, mark, "
                                + "unmark, delete, or bye.");
            }
    }

    /**
     * Adds a todo, deadline, or event based on the user command.
     *
     * @param tasks task list to update.
     * @param command user command containing the task details.
     * @param ui user interface component used to show the added task.
     * @throws SwellException if the command does not contain a valid task.
     */
    private static void addTask(TaskList tasks, String command, Ui ui) throws SwellException {
        Task task = Parser.getTask(command);

        tasks.add(task);
        ui.printTaskAdded(task, tasks.size());
    }
}
