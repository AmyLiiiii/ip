import java.util.ArrayList;
import java.util.Scanner;

// Starts the Swell chatbot.
public class Swell {
    private static final String LINE = "____________________________________________________________";

    //Runs the chatbot until the user enters the bye command.
    public static void main(String[] args) {
        ArrayList<String> tasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            printGreeting();

            String input = scanner.nextLine();
            while (!input.equals("bye")) {
                if (input.equals("list")) {
                    printTasks(tasks);
                } else {
                    tasks.add(input);
                    printTaskAdded(input, tasks.size());
                }
                input = scanner.nextLine();
            }

            printGoodbye();
        }
    }

    private static void printGreeting() {
        System.out.println(LINE);
        System.out.println(" Hey there! I'm Swell.");
        System.out.println(" Tell me what's on your mind.");
        System.out.println(LINE);
    }

    private static void printTasks(ArrayList<String> tasks) {
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

    private static void printTaskAdded(String task, int taskCount) {
        System.out.println();
        System.out.println(" Got it. I've added this task:");
        System.out.println("  - " + task);
        System.out.println(" You now have " + taskCount + " task" + (taskCount == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    private static void printGoodbye() {
        System.out.println();
        System.out.println(LINE);
        System.out.println(" Bye for now! Keep shining and come back when you're ready.");
        System.out.println(LINE);
    }
}
