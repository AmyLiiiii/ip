package swell.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

// Represents a task that should be completed by a deadline
public class Deadline extends Task {
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    private final LocalDate by;

    // Creates a deadline task with the given description and deadline date
    public Deadline(String description, LocalDate by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    // Returns the deadline date
    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
