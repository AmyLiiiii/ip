import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Starts the Swell chatbot
public class Swell {
    private static final String LINE = "____________________________________________________________";

    //Runs the chatbot until the user enters the bye command
    public static void main(String[] args) {
        Storage storage = new Storage();
        ArrayList<Task> tasks = loadTasks(storage);

        try (Scanner scanner = new Scanner(System.in)) {
            printGreeting();

            while (scanner.hasNextLine()) {
                String command = scanner.nextLine().trim();
                if (command.equals("bye")) {
                    break;
                }

                try {
                    boolean shouldSave = processCommand(tasks, command);
                    if (shouldSave) {
                        storage.saveTasks(tasks);
                    }
                } catch (SwellException e) {
                    printError(e.getMessage());
                }
            }

            printGoodbye();
        }
    }

    // Loads tasks from the data file without stopping Swell if the file cannot be read
    private static ArrayList<Task> loadTasks(Storage storage) {
        try {
            return storage.loadTasks();
        } catch (SwellException e) {
            printError(e.getMessage());
            return new ArrayList<>();
        }
    }

    // Prints the greeting message when the chatbot starts
    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hey there! I'm Swell.");
        System.out.println(" Tell me what's on your mind.");
        System.out.println(LINE);
    }

    // Processes one user command
    private static boolean processCommand(ArrayList<Task> tasks, String command) throws SwellException {
        if (command.isEmpty()) {
            throw new SwellException("I'm ready when you are. Try a command like todo read book.");
        }

        String[] commandParts = command.split("\\s+", 2);
        String commandWord = commandParts[0];

        switch (commandWord) {
        case "list":
            printTasks(tasks);
            return false;
        case "todo":
        case "deadline":
        case "event":
            addTask(tasks, command);
            return true;
        case "mark":
            updateTaskStatus(tasks, command, true);
            return true;
        case "unmark":
            updateTaskStatus(tasks, command, false);
            return true;
        case "delete":
            deleteTask(tasks, command);
            return true;
        default:
            throw new SwellException(
                    "I don't know that command yet. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    // Adds a todo, deadline, or event based on the user command
    private static void addTask(ArrayList<Task> tasks, String command) throws SwellException {
        Task task = createTask(command);

        tasks.add(task);
        printTaskAdded(task, tasks.size());
    }

    // Creates a todo, deadline, or event based on the user command
    private static Task createTask(String command) throws SwellException {
        if (command.equals("todo") || command.startsWith("todo ")) {
            return createTodo(command);
        } else if (command.equals("deadline") || command.startsWith("deadline ")) {
            return createDeadline(command);
        } else if (command.equals("event") || command.startsWith("event ")) {
            return createEvent(command);
        }

        throw new SwellException("I don't know that task type yet. Try todo, deadline, or event.");
    }

    // Creates a todo from a todo command
    private static Task createTodo(String command) throws SwellException {
        String description = getCommandBody(command, "todo");
        if (description.isEmpty()) {
            throw new SwellException("A todo needs a description. Try: todo read book");
        }
        return new Todo(description);
    }

    // Creates a deadline from a deadline command
    private static Task createDeadline(String command) throws SwellException {
        String body = getCommandBody(command, "deadline");
        String[] deadlineParts = body.split("\\s+/by\\s+", 2);
        if (deadlineParts.length < 2) {
            throw new SwellException(
                    "A deadline needs a description and /by. Try: deadline return book /by 2019-10-15");
        }

        String description = deadlineParts[0].trim();
        String by = deadlineParts[1].trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new SwellException(
                    "A deadline needs a description and /by. Try: deadline return book /by 2019-10-15");
        }

        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException e) {
            throw new SwellException("Please use yyyy-mm-dd for deadlines. Try: deadline return book /by 2019-10-15");
        }
    }

    // Creates an event from an event command
    private static Task createEvent(String command) throws SwellException {
        String body = getCommandBody(command, "event");
        String[] eventParts = body.split("\\s+/from\\s+", 2);
        if (eventParts.length < 2) {
            throw new SwellException(
                    "An event needs a description, /from, and /to. Try: event project meeting /from Mon 2pm /to 4pm");
        }

        String description = eventParts[0].trim();
        String[] timeParts = eventParts[1].split("\\s+/to\\s+", 2);
        if (timeParts.length < 2) {
            throw new SwellException(
                    "An event needs a description, /from, and /to. Try: event project meeting /from Mon 2pm /to 4pm");
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new SwellException(
                    "An event needs a description, /from, and /to. Try: event project meeting /from Mon 2pm /to 4pm");
        }
        return new Event(description, from, to);
    }

    // Returns the command text after the command word
    private static String getCommandBody(String command, String commandWord) {
        return command.substring(commandWord.length()).trim();
    }

    // Prints the list of tasks or a message if the list is empty
    private static void printTasks(ArrayList<Task> tasks) {
        System.out.println();
        if (tasks.isEmpty()) {
            System.out.println(" Your list is clear for now. Fresh start!");
        } else {
            System.out.println(" Here's what we've got so far:");
            for (int i = 0; i < tasks.size(); i += 1) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }

    // Prints a message confirming that a task has been added to the list
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've added this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    // Updates a task's done status based on a mark or unmark command
    private static void updateTaskStatus(ArrayList<Task> tasks, String input, boolean isDone) throws SwellException {
        String action = isDone ? "mark" : "unmark";
        int taskNumber = getTaskNumber(tasks, input, action);
        Task task = tasks.get(taskNumber - 1);

        if (isDone) {
            task.markAsDone();
            printTaskMarked(task);
        } else {
            task.markAsNotDone();
            printTaskUnmarked(task);
        }
    }

    // Deletes a task from the list
    private static void deleteTask(ArrayList<Task> tasks, String input) throws SwellException {
        int taskNumber = getTaskNumber(tasks, input, "delete");
        Task task = tasks.remove(taskNumber - 1);
        printTaskDeleted(task, tasks.size());
    }

    // Returns the task number from commands such as mark 1 or delete 1
    private static int getTaskNumber(ArrayList<Task> tasks, String input, String action) throws SwellException {
        String[] commandParts = input.split("\\s+", 2);
        if (commandParts.length < 2) {
            throw new SwellException("I need a task number for that. Try: " + action + " 1");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException e) {
            throw new SwellException("I need a task number for that. Try: " + action + " 1");
        }

        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new SwellException(getTaskNotFoundMessage(tasks.size(), action));
        }

        return taskNumber;
    }

    // Returns a message when the task number is outside the current list
    private static String getTaskNotFoundMessage(int taskCount, String action) {
        if (taskCount == 0) {
            return "There aren't any tasks to " + action + " yet.";
        }
        return "I only have tasks 1 to " + taskCount + " right now.";
    }

    // Prints a message confirming that a task has been marked as done
    private static void printTaskMarked(Task task) {
        System.out.println();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    // Prints a message confirming that a task has been marked as not done
    private static void printTaskUnmarked(Task task) {
        System.out.println();
        System.out.println(" No problem. I've marked this task as not done yet:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    // Prints a message confirming that a task has been deleted
    private static void printTaskDeleted(Task task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've removed this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    // Prints an error message without stopping the chatbot
    private static void printError(String message) {
        System.out.println();
        System.out.println(" Oops! " + message);
        System.out.println(LINE);
    }

    // Prints the goodbye message when the user exits the chatbot
    private static void printGoodbye() {
        System.out.println();
        System.out.println(" Bye for now! Keep shining and come back when you're ready.");
        System.out.println(LINE);
    }
}
