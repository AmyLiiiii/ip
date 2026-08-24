package swell.task;

import swell.exception.SwellException;

import java.util.ArrayList;

// Manages the tasks in Swell
public class TaskList {
    private final ArrayList<Task> tasks;

    // Creates an empty task list
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    // Creates a task list with existing tasks
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    // Adds a task to the list
    public void add(Task task) {
        tasks.add(task);
    }

    // Marks a task as done and returns it
    public Task markTask(int taskNumber) throws SwellException {
        Task task = getTask(taskNumber, "mark");
        task.markAsDone();
        return task;
    }

    // Marks a task as not done and returns it
    public Task unmarkTask(int taskNumber) throws SwellException {
        Task task = getTask(taskNumber, "unmark");
        task.markAsNotDone();
        return task;
    }

    // Deletes a task and returns it
    public Task deleteTask(int taskNumber) throws SwellException {
        getTask(taskNumber, "delete");
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns tasks whose descriptions contain the given keyword.
     *
     * @param keyword keyword to search for.
     * @return task list containing matching tasks.
     */
    public TaskList findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        String keywordLowerCase = keyword.toLowerCase();

        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(keywordLowerCase)) {
                matchingTasks.add(task);
            }
        }
        return new TaskList(matchingTasks);
    }

    // Returns the number of tasks in the list
    public int size() {
        return tasks.size();
    }

    // Returns whether the task list is empty
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    // Returns the task at the given zero-based index
    public Task get(int index) {
        return tasks.get(index);
    }

    // Returns the backing task list for storage
    public ArrayList<Task> asList() {
        return tasks;
    }

    private Task getTask(int taskNumber, String action) throws SwellException {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new SwellException(getTaskNotFoundMessage(action));
        }
        return tasks.get(taskNumber - 1);
    }

    private String getTaskNotFoundMessage(String action) {
        if (tasks.isEmpty()) {
            return "There aren't any tasks to " + action + " yet.";
        }
        return "I only have tasks 1 to " + tasks.size() + " right now.";
    }
}
