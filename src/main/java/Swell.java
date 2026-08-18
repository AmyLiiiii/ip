import java.util.ArrayList;
import java.util.Scanner;

// Starts the Swell chatbot
public class Swell {
    private static final String LINE = "____________________________________________________________";

    //Runs the chatbot until the user enters the bye command
    public static void main(String[] args) {
        ArrayList<Task> tasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            printGreeting();

            String input = scanner.nextLine();
            while (!input.equals("bye")) {
                if (input.equals("list")) {
                    printTasks(tasks);
                } else if (input.startsWith("mark ")) {
                    updateTaskStatus(tasks, input, true);
                } else if (input.startsWith("unmark ")) {
                    updateTaskStatus(tasks, input, false);
                } else {
                    Task task = new Task(input);
                    tasks.add(task);
                    printTaskAdded(task, tasks.size());
                }
                input = scanner.nextLine();
            }

            printGoodbye();
        }
    }

    // Prints the greeting message when the chatbot starts
    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hey there! I'm Swell.");
        System.out.println(" Tell me what's on your mind.");
        System.out.println(LINE);
    }

    // Prints the list of tasks or a message if the list is empty
    private static void printTasks(ArrayList<Task> tasks) {
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

    // Prints a message confirming that a task has been added to the list
    private static void printTaskAdded(Task task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've added this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    // Updates a task's done status based on a mark or unmark command
    private static void updateTaskStatus(ArrayList<Task> tasks, String input, boolean isDone) {
        int taskNumber = Integer.parseInt(input.split(" ", 2)[1]);
        Task task = tasks.get(taskNumber - 1);

        if (isDone) {
            task.markAsDone();
            printTaskMarked(task);
        } else {
            task.markAsNotDone();
            printTaskUnmarked(task);
        }
    }

    // Prints a message confirming that a task has been marked as done
    private static void printTaskMarked(Task task) {
        System.out.println();
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    // Prints a message confirming that a task has been marked as not done
    private static void printTaskUnmarked(Task task) {
        System.out.println();
        System.out.println(" No problem. I've marked this task as not done yet:");
        System.out.println("  - " + task);
        System.out.println(LINE);
    }

    // Prints the goodbye message when the user exits the chatbot
    private static void printGoodbye() {
        System.out.println();
        System.out.println(" Bye for now! Keep shining and come back when you're ready.");
        System.out.println(LINE);
    }
}
