// Represents one task in Swell's task list
public class Task {
    private final TaskType type;
    private final String description;
    private boolean isDone;

    // Creates a task
    public Task(TaskType type, String description) {
        this.type = type;
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

    // Returns this task's type
    public TaskType getType() {
        return type;
    }

    // Returns this task's description
    public String getDescription() {
        return description;
    }

    // Returns whether this task is marked as done
    public boolean isDone() {
        return isDone;
    }

    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
