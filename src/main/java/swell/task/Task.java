package swell.task;

/**
 * Represents one task in Swell's task list.
 */
public class Task {
    private final TaskType type;
    private final String description;
    private boolean isDone;

    /**
     * Creates a task with the given type and description.
     *
     * @param type task type.
     * @param description task description.
     */
    public Task(TaskType type, String description) {
        this.type = type;
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns this task's type.
     *
     * @return task type.
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Returns this task's description.
     *
     * @return task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return true if this task is done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the status icon used in task display text.
     *
     * @return status icon.
     */
    protected String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns the display form of this task.
     *
     * @return display form of this task.
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
