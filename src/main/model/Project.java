package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private String description;
    private List<Todo> tasks;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        if (description.length() == 0) {
            throw new EmptyStringException("Cannot construct a project with no description");
        }
        this.description = description;
        tasks = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }
        if (task != this) {
            if (!contains(task)) {
                tasks.add(task);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (contains(task)) {
            tasks.remove(task);
        }
    }

    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    @Override
    public int getEstimatedTimeToComplete() {
        int hours = 0;
        for (Todo t : tasks) {
            hours += t.getEstimatedTimeToComplete();
        }
        return hours;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
//     the percentage of completion (rounded down to the nearest integer).
//     the value returned is the average of the percentage of completion of
//     all the tasks and sub-projects in this project.
    public int getProgress() {
        int x = 0;
        for (Todo t : tasks) {
            x += t.getProgress();
        }
        return (int) Math.floor((double) x / getNumberOfTasks());
    }

    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
    //          If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }

    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return tasks.contains(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new SequenceIterator(tasks.iterator());
    }

    private class SequenceIterator implements Iterator<Todo> {
        private Iterator<Todo> priority1;
        private int priorityLevel;
        private Iterator<Todo> itr;
        boolean stop = false;

        // Constructs a new SequenceIterator with duplicated Iterator fields
        SequenceIterator(Iterator<Todo> itr) {
            priority1 = itr;
            this.itr = itr;
            priorityLevel = 1;
        }

        // EFFECTS: returns true if the last iterator has not yet been iterated through
        @Override
        public boolean hasNext() {
            if (stop) {
                return false;
            }
            if (priorityLevel == 4) {
                while (itr.hasNext()) {
                    Iterator<Todo> priorityTest = itr;
                    Todo todo = priorityTest.next();
                    if (!todo.getPriority().isUrgent() && !todo.getPriority().isImportant()) {
                        return true;
                    }
                }

                return false;
            }
            return priorityLevel < 5 && tasks.size() >= 1;
        }

        // EFFECTS: Runs though each iterator in turn checking their respective parameters.
        @Override
        public Todo next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (priorityLevel == 1) {
                while (priority1.hasNext()) {
                    Todo todo = priority1.next();
                    if (todo.getPriority().isUrgent() && todo.getPriority().isImportant()) {
                        return todo;
                    }
                }
                priorityLevel++;
                priority1 = tasks.iterator();
                return priorityLevel2();
            } else {
                return priorityLevel2();
            }

        }

        private Todo priorityLevel2() {
            if (priorityLevel == 2) {
                while (priority1.hasNext()) {
                    Todo todo = priority1.next();
                    if (!todo.getPriority().isUrgent() && todo.getPriority().isImportant()) {
                        return todo;
                    }
                }
                priorityLevel++;
                priority1 = tasks.iterator();
                return priorityLevel3();
            } else {
                return priorityLevel3();
            }
        }

        private Todo priorityLevel3() {
            if (priorityLevel == 3) {
                while (priority1.hasNext()) {
                    Todo todo = priority1.next();
                    if (todo.getPriority().isUrgent() && !todo.getPriority().isImportant()) {
                        return todo;
                    }
                }
                priorityLevel++;
                priority1 = tasks.iterator();
                return priorityLevel4();
            } else {
                return priorityLevel4();
            }
        }

        private Todo priorityLevel4() {
            while (priority1.hasNext()) {
                Todo todo = priority1.next();
                if (itr.hasNext()) {
                    itr.next();
                }
                if (!todo.getPriority().isUrgent() && !todo.getPriority().isImportant()) {
                    if (!priority1.hasNext()) {
                        stop = true;
                    }
                    return todo;
                }
            }

            priorityLevel++;
            return null;
        }
    }
}




