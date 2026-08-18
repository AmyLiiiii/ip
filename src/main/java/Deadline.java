// Represents a task that should be completed by a deadline
public class Deadline extends Task {
    private final String by;

    // Creates a deadline task with the given description and deadline text
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
