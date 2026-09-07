package swell.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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
    public void getTask_todoCommandWithTags_returnsTaggedTodoTask() throws SwellException {
        Task task = Parser.getTask("todo email Alice #Acme #followup");

        assertInstanceOf(Todo.class, task);
        assertEquals("[T][ ] email Alice #Acme #followup", task.toString());
        assertIterableEquals(List.of("Acme", "followup"), task.getTags());
    }

    @Test
    public void getTask_deadlineCommandWithIsoDate_returnsFormattedDeadline() throws SwellException {
        Task task = Parser.getTask("deadline return book /by 2019-10-15");

        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] return book (by: Oct 15 2019)", task.toString());
    }

    @Test
    public void getTask_deadlineCommandWithTag_returnsTaggedDeadlineTask() throws SwellException {
        Task task = Parser.getTask("deadline submit resume #Acme /by 2019-10-15");

        assertInstanceOf(Deadline.class, task);
        assertEquals("[D][ ] submit resume #Acme (by: Oct 15 2019)", task.toString());
        assertIterableEquals(List.of("Acme"), task.getTags());
    }

    @Test
    public void getTask_eventCommand_returnsEventTask() throws SwellException {
        Task task = Parser.getTask("event meeting /from Mon /to Tue");

        assertInstanceOf(Event.class, task);
        assertEquals("[E][ ] meeting (from: Mon to: Tue)", task.toString());
    }

    @Test
    public void getTagKeyword_tagWithHash_returnsTagWithoutHash() throws SwellException {
        assertEquals("followup", Parser.getTagKeyword("findtag #followup"));
    }

    @Test
    public void getTagKeyword_emptyHashTag_throwsSwellException() {
        assertThrows(SwellException.class, () -> Parser.getTagKeyword("findtag #"));
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
