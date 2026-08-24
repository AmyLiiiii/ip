package swell.ui;

import swell.task.Task;
import swell.task.TaskList;

/**
 * Handles user-facing text output.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    /**
     * Creates a user interface component for printing chatbot messages.
     */
    public Ui() {
    }

    /**
     * Prints the greeting message when the chatbot starts.
     */
    public void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hey there! I'm Swell.");
        System.out.println(" Tell me what's on your mind.");
        System.out.println(LINE);
    }

    /**
     * Prints the list of tasks or a message if the list is empty.
     *
     * @param tasks task list to print.
     */
    public void printTasks(TaskList tasks) {
        System.out.println();
        if (tasks.isEmpty()) {
            System.out.println(" Your list is clear for now. Fresh start!");
        } else {
            System.out.println(" Here's what we've got so far:");
            for (int i = 0; i < tasks.size(); i += 1) {
                System.out.println(" " + (i + 1) + ". " + tasks.get(i));
            }
        }
        System.out.println(LINE);
    }

    /**
     * Prints a message confirming that a task has been added to the list.
     *
     * @param task task that was added.
     * @param taskCount number of tasks after adding the task.
     */
    public void printTaskAdded(Task task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've added this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task"
                + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints a message confirming that a task has been marked as done.
     *
     * @param task task that was marked as done.
     */
    public void printTaskMarked(Task task) {
        System.out.println();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    /**
     * Prints a message confirming that a task has been marked as not done.
     *
     * @param task task that was marked as not done.
     */
    public void printTaskUnmarked(Task task) {
        System.out.println();
        System.out.println(" No problem. I've marked this task as not done yet:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    /**
     * Prints a message confirming that a task has been deleted.
     *
     * @param task task that was deleted.
     * @param taskCount number of tasks after deleting the task.
     */
    public void printTaskDeleted(Task task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've removed this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task"
                + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    /**
     * Prints an error message without stopping the chatbot.
     *
     * @param message error message to print.
     */
    public void printError(String message) {
        System.out.println();
        System.out.println(" Oops! " + message);
        System.out.println(LINE);
    }

    /**
     * Prints the goodbye message when the user exits the chatbot.
     */
    public void printGoodbye() {
        System.out.println();
        System.out.println(" Bye for now! Keep shining and come back when you're ready.");
        System.out.println(LINE);
    }
}
