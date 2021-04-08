package model;

import model.Project;
import model.Task;
import model.Todo;
import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestProject {
    private static Project project;

    @BeforeEach
    void runBefore() {
        project = new Project("Term Assignment");
    }

    @Test
    void testConstructor() {
        assertEquals("Term Assignment", project.getDescription());
    }

    @Test
    void testConstructorNullArgumentExceptionExpected() {
        Project project2;
        try {
            project2 = new Project("");
            fail("EmptyStringException should have been called");
        } catch (EmptyStringException e) {
            assertTrue(true);
        }
    }

    @Test
    void testGetEstimatedTimeToComplete() {
        Task task1 = new Task("5");
        Task task2 = new Task("10");
        Project project1 = new Project("11");
        Task task3 = new Task("1");
        task1.setEstimatedTimeToComplete(5);
        task2.setEstimatedTimeToComplete(10);
        task3.setEstimatedTimeToComplete(1);
        project1.add(task2);
        project1.add(task3);
        project.add(project1);
        project.add(task1);
        assertEquals(16, project.getEstimatedTimeToComplete());
    }

    @Test
    void testAdd() {
        Task task1 = new Task("Phase 1");
        try {
            project.add(task1);
            assertEquals(1, project.getNumberOfTasks());
            project.add(task1);
            assertEquals(1, project.getNumberOfTasks());
        } catch (NullArgumentException n) {
            fail("NullArgumentException should not have been called");
            n.printStackTrace();
        }
    }

    @Test
    void testAddSameProject() {
        Project project1 = project;
        project.add(project1);
        assertFalse(project.contains(project1));
    }

    @Test
    void testAddNullArgumentExceptionExpected() {
        Task task1 = null;
        try {
            project.add(task1);
            fail("NullArgumentException should have been called");
        } catch (NullArgumentException n) {
            assertTrue(true);
        }
        assertEquals(0, project.getNumberOfTasks());
    }


    @Test
    void removeTest() {
        Task task1 = new Task("Phase 1");
        Task task2 = new Task("Phase 2");
        try {
            project.add(task1);
            project.add(task2);
            assertEquals(2, project.getNumberOfTasks());
            project.remove(task1);
            assertEquals(1, project.getNumberOfTasks());
            project.remove(task2);
            assertEquals(0, project.getNumberOfTasks());
        } catch (NullArgumentException n) {
            n.printStackTrace();
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testRemoveNullArgumentExceptionExpected() {
        Task task1 = null;
        try {
            project.remove(task1);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException n) {
            assertTrue(true);
        }
    }

    @Test
    void getProgressTest() {
        assertEquals(0, project.getProgress());
        Task task1 = new Task("Phase 1");
        Task task2 = new Task("Phase 2");
        project.add(task1);
        project.add(task2);
        assertEquals(0, project.getProgress());
        task1.setProgress(100);
        assertEquals(50, project.getProgress());
    }

    @Test
    void isCompletedTest() {
        assertFalse(project.isCompleted());
        Task task1 = new Task("Phase 1");
        Task task2 = new Task("Phase 2");
        project.add(task1);
        project.add(task2);
        assertFalse(project.isCompleted());
        task1.setProgress(100);
        assertFalse(project.isCompleted());
        task2.setProgress(100);
        assertTrue(project.isCompleted());
    }

    @Test
    void containsTest() {
        Task task1 = new Task("Phase 1");
        Task task2 = new Task("Phase 2");
        assertFalse(project.contains(task1));
        try {
            project.add(task1);
            assertTrue(project.contains(task1));
            assertFalse(project.contains(task2));
            project.add(task2);
            assertTrue(project.contains(task2));
        } catch (NullArgumentException n) {
            n.printStackTrace();
            fail("NullArgumentException should not have been called");
        }
    }

    @Test
    void testContainsNullArgumentExceptionExpected() {
        Task task1 = null;
        try {
            project.contains(task1);
            fail("NullArgumentException should have been called");
        } catch (NullArgumentException n) {
            assertTrue(true);
        }
    }

    @Test
    void testHasNext() {
        assertFalse(project.iterator().hasNext());
        project.add(new Task("New Task"));
        assertTrue(project.iterator().hasNext());
    }

    @Test
    void testIterator() {
        Todo iandu = new Task("Important and Urgent");
        Todo uandnoti = new Task("Urgent and Not Important");
        Todo notuandnoti = new Project("Not Important and not Urgent");
        iandu.getPriority().setImportant(true);
        iandu.getPriority().setUrgent(true);
        uandnoti.getPriority().setUrgent(true);
        uandnoti.getPriority().setImportant(false);
        notuandnoti.getPriority().setUrgent(false);
        notuandnoti.getPriority().setImportant(false);
        project.add(uandnoti);
        project.add(iandu);
        project.add(notuandnoti);
        assertEquals(iandu, project.iterator().next());
        int numTodos = 0;
        for (Todo t : project) {
            numTodos++;
        }
        assertEquals(project.getNumberOfTasks(), numTodos);
    }

    @Test
    void testIteratorMultipleImportantAndUrgent() {
        Todo iandu = new Task("Important and Urgent");
        Todo iandu2 = new Task("Urgent and Important");
        Todo notuandnoti = new Project("Not Important and not Urgent");
        iandu.getPriority().setImportant(true);
        iandu.getPriority().setUrgent(true);
        iandu2.getPriority().setUrgent(true);
        iandu2.getPriority().setImportant(true);
        notuandnoti.getPriority().setUrgent(false);
        notuandnoti.getPriority().setImportant(false);
        project.add(notuandnoti);
        project.add(iandu2);
        project.add(iandu);
        ArrayList<Todo> importantAndUrgent = new ArrayList<>();
        for (Todo t : project) {
            importantAndUrgent.add(t);
        }
        assertEquals(iandu2, importantAndUrgent.get(0));
        assertEquals(iandu, importantAndUrgent.get(1));
    }

    @Test
    void testIteratorException() {
        try {
            project.iterator().next();
            fail("Exception should have been thrown");
        } catch (NoSuchElementException e) {
            //
        }
    }

    @Test
    void testIteratorTwoOfEach() {
        Todo iandu = new Task("Important and Urgent");
        Todo iandu2 = new Task("Urgent and Important");
        Todo notuandnoti = new Task("Not Important and not Urgent");
        Todo notuandnoti2 = new Task("Not Urgent and not Not Important");
        Todo notuandi = new Task("Not Urgent and Important");
        Todo notuandi2 = new Task("Important and not Urgent");
        Todo notiandu = new Task("Not Important and urgent");
        Todo notiandu2 = new Task("Urgent and Not Important");
        iandu.getPriority().setImportant(true);
        iandu.getPriority().setUrgent(true);
        iandu2.getPriority().setUrgent(true);
        iandu2.getPriority().setImportant(true);
        notuandnoti.getPriority().setUrgent(false);
        notuandnoti.getPriority().setImportant(false);
        notuandnoti2.getPriority().setUrgent(false);
        notuandnoti2.getPriority().setImportant(false);
        notuandi.getPriority().setUrgent(false);
        notuandi.getPriority().setImportant(true);
        notuandi2.getPriority().setUrgent(false);
        notuandi2.getPriority().setImportant(true);
        notiandu.getPriority().setImportant(false);
        notiandu.getPriority().setUrgent(true);
        notiandu2.getPriority().setImportant(false);
        notiandu2.getPriority().setUrgent(true);
        project.add(notuandi);
        project.add(notiandu2);
        project.add(notuandnoti);
        project.add(notuandnoti2);
        project.add(iandu2);
        project.add(iandu);
        project.add(notiandu);
        project.add(notiandu2);
        ArrayList<Todo> list = new ArrayList<>();
        for (Todo t : project) {
            list.add(t);
        }
        assertEquals(iandu2, list.get(0));
        assertEquals(iandu, list.get(1));
        assertEquals(notuandi, list.get(2));
        assertEquals(notiandu, list.get(4));
    }
}