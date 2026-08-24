package swell.task;

// Represents a todo task without a date or time
public class Todo extends Task {
    // Creates a todo task with the given description
    public Todo(String description) {
        super(TaskType.TODO, description);
    }
}
