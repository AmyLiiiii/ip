# Swell project template

This is a project template for a greenfield Java project. It's named _Swell_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
    1. Click `Open`.
    1. Select the project directory, and click `OK`.
    1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Swell.java` file, right-click it, and choose `Run Swell.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
    ```
    ____________________________________________________________
     Hello! I'm Swell
     What can I do for you?
    ____________________________________________________________
     Bye. Hope to see you again soon!
    ____________________________________________________________
    ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Project Architecture

Swell is organized into small classes with clear responsibilities:

- `Swell` runs the main chatbot loop and coordinates the other classes.
- `Parser` interprets user commands and creates the correct task objects.
- `TaskList` manages the task collection, including adding, marking, unmarking, and deleting tasks.
- `Ui` handles all user-facing messages.
- `Storage` loads tasks from `data/swell.txt` and saves changes automatically.
- `Task`, `Todo`, `Deadline`, `Event`, and `TaskType` represent the different task types and their display formats.
- `SwellException` represents user input errors that the chatbot can recover from.
