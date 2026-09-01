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
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private boolean isExit;

    /**
     * Creates a Swell chatbot and loads saved tasks.
     */
    public Swell() {
        ui = new Ui();
        storage = new Storage();
        tasks = loadTasks(storage, ui);
        isExit = false;
    }

    /**
     * Runs the chatbot until the user enters the bye command.
     *
     * @param args command line arguments supplied to the program.
     */
    public static void main(String[] args) {
        Swell swell = new Swell();

        try (Scanner scanner = new Scanner(System.in)) {
            swell.ui.printGreeting();

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.equals("bye")) {
                    break;
                }

                String response = swell.getResponse(command);
                swell.ui.printMessage(response);
            }

            swell.ui.printGoodbye();
        }
    }

    /**
     * Returns the greeting message for GUI clients.
     *
     * @return greeting message.
     */
    public String getGreeting() {
        return ui.getGreeting();
    }

    /**
     * Returns Swell's response to one user command.
     *
     * @param command user command to process.
     * @return response message.
     */
    public String getResponse(String command) {
        String trimmedCommand = command.trim();
        if (trimmedCommand.equals("bye")) {
            isExit = true;
            return ui.getGoodbye();
        }

        try {
            return processCommand(trimmedCommand);
        } catch (SwellException e) {
            return ui.getErrorText(e.getMessage());
        }
    }

    /**
     * Returns whether the user has entered the bye command.
     *
     * @return true if Swell should stop accepting input.
     */
    public boolean isExit() {
        return isExit;
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
    private String processCommand(String command) throws SwellException {
        if (command.isEmpty()) {
            throw new SwellException("I'm ready when you are. Try a command like todo read book.");
        }

        String commandWord = Parser.getCommandWord(command);

        switch (commandWord) {
            case "list":
                return ui.getTasksText(tasks);
            case "find":
                return ui.getMatchingTasksText(tasks.findTasks(Parser.getFindKeyword(command)));
            case "todo":
            case "deadline":
            case "event":
                return addTask(command);
            case "mark":
                Task markedTask = tasks.markTask(Parser.getTaskNumber(command, "mark"));
                storage.saveTasks(tasks);
                return ui.getTaskMarkedText(markedTask);
            case "unmark":
                Task unmarkedTask = tasks.unmarkTask(Parser.getTaskNumber(command, "unmark"));
                storage.saveTasks(tasks);
                return ui.getTaskUnmarkedText(unmarkedTask);
            case "delete":
                Task deletedTask = tasks.deleteTask(Parser.getTaskNumber(command, "delete"));
                storage.saveTasks(tasks);
                return ui.getTaskDeletedText(deletedTask, tasks.size());
            default:
                throw new SwellException(
                        "I don't know that command yet. Try todo, deadline, event, list, find, "
                                + "mark, unmark, delete, or bye.");
        }
    }

    /**
     * Adds a todo, deadline, or event based on the user command.
     *
     * @param tasks task list to update.
     * @param command user command containing the task details.
     * @return task-added confirmation text.
     * @throws SwellException if the command does not contain a valid task.
     */
    private String addTask(String command) throws SwellException {
        Task task = Parser.getTask(command);

        tasks.add(task);
        storage.saveTasks(tasks);
        return ui.getTaskAddedText(task, tasks.size());
    }
}
