package swell.task;

// Represents the supported task types in Swell
public enum TaskType {
    TODO("T"),
    DEADLINE("D"),
    EVENT("E");

    private final String symbol;

    // Creates a task type with the display symbol used in task lists
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    // Returns the display symbol for this task type
    public String getSymbol() {
        return symbol;
    }
}
