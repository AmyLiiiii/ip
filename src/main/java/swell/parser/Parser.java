package swell.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import swell.exception.SwellException;
import swell.task.Deadline;
import swell.task.Event;
import swell.task.Task;
import swell.task.Todo;

/**
 * Parses user commands into task objects and command arguments.
 */
public class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String FIND_COMMAND = "find";
    private static final String FIND_TAG_COMMAND = "findtag";
    private static final String DEADLINE_SEPARATOR = "\\s+/by\\s+";
    private static final String EVENT_FROM_SEPARATOR = "\\s+/from\\s+";
    private static final String EVENT_TO_SEPARATOR = "\\s+/to\\s+";
    private static final Pattern TAG_PATTERN = Pattern.compile("(?<!\\S)#([A-Za-z0-9][A-Za-z0-9_-]*)");
    private static final String TODO_FORMAT_ERROR =
            "A todo needs a description. Try: todo read book";
    private static final String DEADLINE_FORMAT_ERROR =
            "A deadline needs a description and /by. Try: deadline return book /by 2019-10-15";
    private static final String DEADLINE_DATE_ERROR =
            "Please use yyyy-mm-dd for deadlines. Try: deadline return book /by 2019-10-15";
    private static final String EVENT_FORMAT_ERROR =
            "An event needs a description, /from, and /to. "
                    + "Try: event project meeting /from Mon 2pm /to 4pm";
    private static final String FIND_FORMAT_ERROR =
            "A find command needs a keyword. Try: find book";
    private static final String FIND_TAG_FORMAT_ERROR =
            "A findtag command needs a tag. Try: findtag followup";

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Returns the first word of the command.
     *
     * @param command user command to read.
     * @return command word.
     */
    public static String getCommandWord(String command) {
        return command.split("\\s+", 2)[0];
    }

    /**
     * Returns the task described by a todo, deadline, or event command.
     *
     * @param command user command to parse.
     * @return task described by the command.
     * @throws SwellException if the command does not describe a valid task.
     */
    public static Task getTask(String command) throws SwellException {
        String commandWord = getCommandWord(command);
        switch (commandWord) {
            case TODO_COMMAND:
                return createTodo(command);
            case DEADLINE_COMMAND:
                return createDeadline(command);
            case EVENT_COMMAND:
                return createEvent(command);
            default:
                throw new SwellException("I don't know that task type yet. Try todo, deadline, or event.");
        }
    }

    /**
     * Returns the task number from commands such as mark 1 or delete 1.
     *
     * @param command user command to parse.
     * @param action command action used in error messages.
     * @return task number given in the command.
     * @throws SwellException if the command does not contain a valid task number.
     */
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

    /**
     * Returns the keyword used by a find command.
     *
     * @param command user command to parse.
     * @return keyword to search for.
     * @throws SwellException if the find command has no keyword.
     */
    public static String getFindKeyword(String command) throws SwellException {
        return getRequiredCommandBody(command, FIND_COMMAND, FIND_FORMAT_ERROR);
    }

    /**
     * Returns the tag used by a findtag command.
     *
     * @param command user command to parse.
     * @return tag to search for.
     * @throws SwellException if the findtag command has no tag.
     */
    public static String getTagKeyword(String command) throws SwellException {
        String tag = getRequiredCommandBody(command, FIND_TAG_COMMAND, FIND_TAG_FORMAT_ERROR);
        String tagWithoutPrefix = tag.startsWith("#") ? tag.substring(1) : tag;
        if (tagWithoutPrefix.isEmpty()) {
            throw new SwellException(FIND_TAG_FORMAT_ERROR);
        }
        return tagWithoutPrefix;
    }

    private static Task createTodo(String command) throws SwellException {
        String body = getRequiredCommandBody(command, TODO_COMMAND, TODO_FORMAT_ERROR);
        ParsedTaskDetails details = parseTaskDetails(body, TODO_FORMAT_ERROR);
        return new Todo(details.description, details.tags);
    }

    private static Task createDeadline(String command) throws SwellException {
        String body = getCommandBody(command, DEADLINE_COMMAND);
        String[] deadlineParts = body.split(DEADLINE_SEPARATOR, 2);
        if (deadlineParts.length < 2) {
            throw new SwellException(DEADLINE_FORMAT_ERROR);
        }

        ParsedTaskDetails details = parseTaskDetails(deadlineParts[0], DEADLINE_FORMAT_ERROR);
        String by = deadlineParts[1].trim();
        requireNonEmptyFields(DEADLINE_FORMAT_ERROR, by);

        try {
            return new Deadline(details.description, LocalDate.parse(by), details.tags);
        } catch (DateTimeParseException e) {
            throw new SwellException(DEADLINE_DATE_ERROR);
        }
    }

    private static Task createEvent(String command) throws SwellException {
        String body = getCommandBody(command, EVENT_COMMAND);
        String[] eventParts = body.split(EVENT_FROM_SEPARATOR, 2);
        if (eventParts.length < 2) {
            throw new SwellException(EVENT_FORMAT_ERROR);
        }

        ParsedTaskDetails details = parseTaskDetails(eventParts[0], EVENT_FORMAT_ERROR);
        String[] timeParts = eventParts[1].split(EVENT_TO_SEPARATOR, 2);
        if (timeParts.length < 2) {
            throw new SwellException(EVENT_FORMAT_ERROR);
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();
        requireNonEmptyFields(EVENT_FORMAT_ERROR, from, to);
        return new Event(details.description, from, to, details.tags);
    }

    private static String getRequiredCommandBody(String command, String commandWord,
            String errorMessage) throws SwellException {
        String body = getCommandBody(command, commandWord);
        if (body.isEmpty()) {
            throw new SwellException(errorMessage);
        }
        return body;
    }

    private static void requireNonEmptyFields(String errorMessage, String... fields)
            throws SwellException {
        for (String field : fields) {
            if (field.isEmpty()) {
                throw new SwellException(errorMessage);
            }
        }
    }

    private static String getCommandBody(String command, String commandWord) {
        return command.substring(commandWord.length()).trim();
    }

    private static ParsedTaskDetails parseTaskDetails(String text, String errorMessage)
            throws SwellException {
        String description = TAG_PATTERN.matcher(text).replaceAll("").trim().replaceAll("\\s+", " ");
        if (description.isEmpty()) {
            throw new SwellException(errorMessage);
        }
        return new ParsedTaskDetails(description, extractTags(text));
    }

    private static ArrayList<String> extractTags(String text) {
        ArrayList<String> tags = new ArrayList<>();
        Matcher matcher = TAG_PATTERN.matcher(text);
        while (matcher.find()) {
            String tag = matcher.group(1);
            if (!containsTag(tags, tag)) {
                tags.add(tag);
            }
        }
        return tags;
    }

    private static boolean containsTag(ArrayList<String> tags, String tagToFind) {
        return tags.stream().anyMatch(tag -> tag.equalsIgnoreCase(tagToFind));
    }

    private static class ParsedTaskDetails {
        private final String description;
        private final ArrayList<String> tags;

        private ParsedTaskDetails(String description, ArrayList<String> tags) {
            this.description = description;
            this.tags = tags;
        }
    }
}
