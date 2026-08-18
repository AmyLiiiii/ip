import java.util.Scanner;

/**
 * Starts the Swell chatbot.
 */
public class Swell {
    private static final String LINE = "____________________________________________________________";

    //Runs the chatbot until the user enters the bye command.
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(LINE);
            System.out.println(" Hey there! I'm Swell.");
            System.out.println(" Tell me what's on your mind.");
            System.out.println(LINE);

            String input = scanner.nextLine();
            while (!input.equals("bye")) {
                System.out.println();
                System.out.println(" You said: " + input);
                System.out.println(" Nice, I'm ready for the next one.");
                System.out.println(LINE);
                input = scanner.nextLine();
            }

            System.out.println(" Bye for now! Keep shining and come back when you're ready.");
            System.out.println(LINE);
        }
    }
}
