package swell.task;

import swell.exception.SwellException;

import java.util.ArrayList;

/**
 * Manages the tasks in Swell.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with existing tasks.
     *
     * @param tasks existing tasks to use as the backing list
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Marks a task as done and returns it.
     *
     * @param taskNumber one-based task number
     * @return marked task
     * @throws SwellException if the task number is outside the current list
     */
    public Task markTask(int taskNumber) throws SwellException {
        Task task = getTask(taskNumber, "mark");
        task.markAsDone();
        return task;
    }

    /**
     * Marks a task as not done and returns it.
     *
     * @param taskNumber one-based task number
     * @return unmarked task
     * @throws SwellException if the task number is outside the current list
     */
    public Task unmarkTask(int taskNumber) throws SwellException {
        Task task = getTask(taskNumber, "unmark");
        task.markAsNotDone();
        return task;
    }

    /**
     * Deletes a task and returns it.
     *
     * @param taskNumber one-based task number
     * @return deleted task
     * @throws SwellException if the task number is outside the current list
     */
    public Task deleteTask(int taskNumber) throws SwellException {
        getTask(taskNumber, "delete");
        return tasks.remove(taskNumber - 1);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns whether the task list is empty.
     *
     * @return true if the task list has no tasks
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param index zero-based task index
     * @return task at the given index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the backing task list for storage.
     *
     * @return backing task list
     */
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
