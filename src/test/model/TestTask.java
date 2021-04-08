package model;

import model.*;
import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestTask {
    private static Task task;
    private static Set<Tag> taskSet;

    @BeforeEach
    public void runBefore() {
        try {
            task = new Task("Finish ComputerScience");
        } catch (EmptyStringException e) {
            e.printStackTrace();
            fail("EmptyStringException");
        }

    }

    @Test
    public void constructorTest() {
        assertEquals("Finish ComputerScience", task.getDescription());
        Assertions.assertEquals(new Priority(4).toString(), task.getPriority().toString());
        Assertions.assertEquals(Status.TODO, task.getStatus());
        assertEquals(0, task.getEstimatedTimeToComplete());
        assertEquals(0, task.getProgress());
    }

    @Test
    void testConstructorExceptionExpected() {
        Task task2;
        try {
            task2 = new Task("");
            fail("EmptyStringException should have been called");
        } catch (EmptyStringException e) {
            assertTrue(true);
        }
    }

    @Test
    void testSetProgress() {
        task.setProgress(20);
        assertEquals(20, task.getProgress());
    }

    @Test
    void testSetProgressExceptionExpected() {
        try {
            task.setProgress(-1);
            fail("Exception should hvae been throwm");
        } catch (InvalidProgressException e) {
            //
        }
        assertEquals(0, task.getProgress());
    }

    @Test
    void testSetEstimatedTimeToComplete() {
        task.setEstimatedTimeToComplete(4);
        assertEquals(4, task.getEstimatedTimeToComplete());
    }

    @Test
    void testSetEstimatedTimeToCompleteExceptionExpected() {
        try {
            task.setEstimatedTimeToComplete(-3);
            fail("Exception should have been thrown");
        } catch (NegativeInputException e) {
            //
        }
    }


    @Test
    public void addTagTest() {
        try {
            task.addTag("cpsc210");
            assertTrue(task.containsTag("cpsc210"));
            assertFalse(task.containsTag("GregorKiczales"));
            task.addTag("GregorKiczales");
            assertTrue(task.containsTag("cpsc210"));
            assertTrue(task.containsTag("GregorKiczales"));
            task.addTag("cpsc210");
            assertEquals(2, task.getTags().size());
        } catch (EmptyStringException e) {
            e.printStackTrace();
            fail("EmptyStringException should not have been called");
        }
    }

    @Test
    void testAddTagExceptionExpected() {
        try {
            task.addTag("");
            fail("EmptyStringException should have been called");
        } catch (EmptyStringException e) {
            assertTrue(true);
        }
    }

    @Test
    void removeTagTest() {
        try {
            task.addTag("cpsc210");
            assertTrue(task.containsTag("cpsc210"));
            assertFalse(task.containsTag("GregorKiczales"));
            task.addTag("GregorKiczales");
            assertTrue(task.containsTag("cpsc210"));
            assertTrue(task.containsTag("GregorKiczales"));
            assertEquals(2, task.getTags().size());
            task.removeTag("cpsc210");
            assertFalse(task.containsTag("cpsc210"));
            assertTrue(task.containsTag("GregorKiczales"));
            task.removeTag("cpsc210");
            assertEquals(1, task.getTags().size());
        } catch (EmptyStringException e) {
            e.printStackTrace();
            fail("EmptyStringException should have been called");
        }
    }

    @Test
    void testRemoveTagExceptionExpected() {
        try {
            task.removeTag("");
            fail("EmptyStringException should have been called");
        } catch (EmptyStringException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testSetPriority() {
        try {
            task.setPriority(new Priority(3));
            task.setPriority(new Priority(2));
            assertEquals(new Priority(2).toString(), task.getPriority().toString());
        } catch (NullArgumentException n) {
            n.printStackTrace();
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testSetPriorityExceptionExpected() {
        try {
            task.setPriority(null);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException n) {
            assertTrue(true);
        }
    }

    @Test
    public void setStatusTest() {
        try {
            task.setStatus(Status.IN_PROGRESS);
            assertEquals(Status.IN_PROGRESS, task.getStatus());
            task.setStatus(Status.DONE);
            assertEquals(Status.DONE, task.getStatus());
        } catch (NullArgumentException n) {
            n.printStackTrace();
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testSetStatusExceptionExpected() {
        try {
            task.setStatus(null);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException n) {
            assertTrue(true);
        }
    }

    @Test
    public void setDescriptionTest() {
        try {
            task.setDescription("Finish CompSci");
            assertEquals("Finish CompSci", task.getDescription());
        } catch (EmptyStringException e) {
            e.printStackTrace();
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testSetDescriptionExceptionExpected() {
        try {
            task.setDescription("");
            fail("EmptyStringException should have been thrown");
        } catch (EmptyStringException e) {
            assertTrue(true);
        }
    }

    @Test
    public void setDueDateTest() {
        DueDate dueDate = new DueDate();
        dueDate.setDueTime(9,30);
        task.setDueDate(dueDate);
        assertEquals(dueDate, task.getDueDate());
        dueDate.setDueTime(10,12);
        assertEquals(dueDate, task.getDueDate());
    }

    @Test
    public void toStringTest() {
        DueDate dueDate = new DueDate();
        task.setStatus(Status.TODO);
        task.setDueDate(new DueDate());
        task.addTag("cpsc210");
        task.addTag("help");
        assertTrue(task.toString().contains("Description: " + "Finish ComputerScience"));
        assertTrue(task.toString().contains("Due date: " + dueDate.toString()));
        assertTrue(task.toString().contains("Priority: " + "DEFAULT"));
        assertTrue(task.toString().contains("Status: " + "TODO"));
        assertTrue(task.toString().contains("Tags: " + "#help, #cpsc210" ));
    }

    @Test
    public void containsTagTest() {
        task.addTag("cpsc210");
        assertTrue(task.containsTag("cpsc210"));
        task.addTag("GregorKiczales");
        task.addTag("dick");
        assertTrue(task.containsTag("cpsc210"));
        assertFalse(task.containsTag("ChairmanMao"));
    }


}