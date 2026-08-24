import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Parses user commands into task objects and command arguments
public class Parser {
    // Returns the first word of the command
    public static String getCommandWord(String command) {
        return command.split("\\s+", 2)[0];
    }

    // Returns the task described by a todo, deadline, or event command
    public static Task getTask(String command) throws SwellException {
        String commandWord = getCommandWord(command);
        switch (commandWord) {
            case "todo":
                return createTodo(command);
            case "deadline":
                return createDeadline(command);
            case "event":
                return createEvent(command);
            default:
                throw new SwellException("I don't know that task type yet. Try todo, deadline, or event.");
            }
    }

    // Returns the task number from commands such as mark 1 or delete 1
    public static int getTaskNumber(String command, String action) throws SwellException {
        String[] commandParts = command.split("\\s+", 2);
        if (commandParts.length < 2) {
            throw new SwellException("I need a task number for that. Try: " + action + " 1");
        }

        try {
            return Integer.parseInt(commandParts[1]);
        } catch (NumberFormatException e) {
            throw new SwellException("I need a task number for that. Try: " + action + " 1");
        }
    }

    private static Task createTodo(String command) throws SwellException {
        String description = getCommandBody(command, "todo");
        if (description.isEmpty()) {
            throw new SwellException("A todo needs a description. Try: todo read book");
        }
        return new Todo(description);
    }

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

    private static String getCommandBody(String command, String commandWord) {
        return command.substring(commandWord.length()).trim();
    }
}
