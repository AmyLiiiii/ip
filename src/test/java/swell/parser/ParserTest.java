package swell.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import swell.exception.SwellException;
import swell.task.Deadline;
import swell.task.Event;
import swell.task.Task;
import swell.task.Todo;

public class ParserTest {
    @Test
    public void getTask_todoCommand_returnsTodoTask() throws SwellException {
        Task task = Parser.getTask("todo read book");

        assertInstanceOf(Todo.class, task);
        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    public void getTask_deadlineCommandWithIsoDate_returnsFormattedDeadline() throws SwellException {
        Task task = Parser.getTask("deadline return book /by 2019-10-15");

        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] return book (by: Oct 15 2019)", task.toString());
    }

    @Test
    public void getTask_eventCommand_returnsEventTask() throws SwellException {
        Task task = Parser.getTask("event meeting /from Mon /to Tue");

        assertInstanceOf(Event.class, task);
        assertEquals("[E][ ] meeting (from: Mon to: Tue)", task.toString());
    }

    @Test
    public void getTask_deadlineCommandWithInvalidDate_throwsSwellException() {
        assertThrows(SwellException.class, () -> Parser.getTask("deadline return book /by Sunday"));
    }

    @Test
    public void getTaskNumber_validMarkCommand_returnsTaskNumber() throws SwellException {
        assertEquals(12, Parser.getTaskNumber("mark 12", "mark"));
    }

    @Test
    public void getTaskNumber_missingNumber_throwsSwellException() {
        assertThrows(SwellException.class, () -> Parser.getTaskNumber("delete", "delete"));
    }
}
