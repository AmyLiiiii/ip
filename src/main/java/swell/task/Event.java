package swell.task;

import java.util.ArrayList;

/**
 * Represents a task that happens over a period of time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task with the given description, start, and end text.
     *
     * @param description task description.
     * @param from event start text.
     * @param to event end text.
     */
    public Event(String description, String from, String to) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an event task with the given description, start, end, and tags.
     *
     * @param description task description.
     * @param from event start text.
     * @param to event end text.
     * @param tags task tags.
     */
    public Event(String description, String from, String to, ArrayList<String> tags) {
        super(TaskType.EVENT, description, tags);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event start text.
     *
     * @return event start text.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event end text.
     *
     * @return event end text.
     */
    public String getTo() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
