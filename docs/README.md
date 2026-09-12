# Swell User Guide

Swell is a calm task navigator that helps you keep todos, deadlines, events, and tags in one place.
It responds with a steady sea-navigation personality while keeping commands short and predictable.

When Swell starts, it greets you with:

```text
Hi, I'm Swell, your calm task navigator.
Send me a task, and we'll chart a steady course.
```

## Command Input Rules

Swell reads commands in this general format:

```text
COMMAND REQUIRED_DETAILS [OPTIONAL_DETAILS]
```

Use exactly one command word at the start, such as `todo`, `deadline`, `event`, `list`,
`mark`, `unmark`, `delete`, `find`, `findtag`, or `bye`.

For commands that need a task number, enter one positive whole number only.

Correct:

```text
mark 1
delete 2
```

Incorrect:

```text
mark
mark 0
mark 1 2
delete one
```

For deadline commands, use `/by` exactly once and write the date as `YYYY-MM-DD`.

Correct:

```text
deadline submit report /by 2026-09-18
```

Incorrect:

```text
deadline submit report
deadline submit report /by Sunday
deadline submit report /by 2026-09-18 /by 2026-09-19
```

For event commands, use `/from` exactly once and `/to` exactly once. The `/from` and `/to`
values may be the same if the event happens at one point in time.

Correct:

```text
event project meeting /from Monday 2pm /to 4pm
event quick sync /from Monday /to Monday
```

Incorrect:

```text
event project meeting /from Monday 2pm
event project meeting /from Monday 2pm /from Tuesday 2pm /to 4pm
```

For tags, use letters, numbers, hyphens, or underscores only. Tags can be written with or
without `#` when using `findtag`.

Correct:

```text
todo email Alice #followup
findtag followup
findtag #cs2103
```

Incorrect:

```text
findtag
findtag follow up
findtag #
```

## Adding Todos

Use `todo` for tasks without a date or time.

Format:

```text
todo DESCRIPTION [#TAG]...
```

Example:

```text
todo read book #school
```

Expected response:

```text
Logged and anchored. I've added this task:
 - [T][ ] read book #school
There is now 1 task on board.
```

## Adding Deadlines

Use `deadline` for tasks that must be done by a specific date.

Format:

```text
deadline DESCRIPTION [#TAG]... /by YYYY-MM-DD
```

Example:

```text
deadline submit report #cs2103 /by 2026-09-18
```

## Adding Events

Use `event` for tasks that happen over a time period.

Format:

```text
event DESCRIPTION [#TAG]... /from START /to END
```

Example:

```text
event project meeting #team /from Monday 2pm /to 4pm
```

## Viewing Tasks

Use `list` to see every task currently on board.

Format:

```text
list
```

Swell introduces the list with:

```text
Here's the current chart:
```

## Marking Tasks

Use `mark` when a task is done, and `unmark` when it should be set back to not done.

Formats:

```text
mark TASK_NUMBER
unmark TASK_NUMBER
```

Examples:

```text
mark 1
unmark 1
```

## Deleting Tasks

Use `delete` to remove a task.

Format:

```text
delete TASK_NUMBER
```

Example:

```text
delete 1
```

## Finding Tasks

Use `find` to search by keyword, or `findtag` to search by tag.

Formats:

```text
find KEYWORD
findtag TAG
```

Examples:

```text
find report
findtag cs2103
```

## Handling Errors

If a command is missing information or uses an unsupported format, Swell explains the issue and gives
you a working example.

Some examples of commands Swell will reject:

```text
todo
deadline submit report /by Sunday
event project meeting /from Monday
findtag follow up
delete one
```

Example response:

```text
todo
```

Expected response:

```text
Oops! Choppy water ahead: A todo needs cargo to carry. Try: todo read book
```

## Exiting Swell

Use `bye` when you are done.

Format:

```text
bye
```

Swell replies:

```text
Docking for now. Come back when you're ready to set sail again.
```
