// Represents one task in Swell's task list.
public class Task {
    private final String description;
    private boolean isDone;

    // Creates a task
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    //Marks this task as done
    public void markAsDone() {
        this.isDone = true;
    }

    // Unmarks this task
    public void markAsNotDone() {
        this.isDone = false;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
