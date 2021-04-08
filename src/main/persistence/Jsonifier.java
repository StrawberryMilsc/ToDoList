package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject myTag = new JSONObject();
        myTag.put("name", tag.getName());
        return myTag;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject myPriority = new JSONObject();
        myPriority.put("Important", priority.isImportant());
        myPriority.put("Urgent", priority.isUrgent());
        return myPriority;
    }

    // EFFECTS: returns JSON respresentation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        JSONObject myDueDate = new JSONObject();
        if (dueDate == null) {
            myDueDate.put("due-date", JSONObject.NULL);
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(dueDate.getDate());
            myDueDate.put("year", calendar.get(Calendar.YEAR));
            myDueDate.put("month", calendar.get(Calendar.MONTH));
            myDueDate.put("day", calendar.get(Calendar.DAY_OF_MONTH));
            myDueDate.put("hour", calendar.get(Calendar.HOUR_OF_DAY));
            myDueDate.put("minute", calendar.get(Calendar.MINUTE));
        }
        return myDueDate;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject myTask = new JSONObject();
        JSONArray myTagArray = new JSONArray();
        if (!task.getTags().isEmpty()) {
            for (Tag t : task.getTags()) {
                myTagArray.put(Jsonifier.tagToJson(t));
            }
        }
        myTask.put("description", task.getDescription());
        myTask.put("tags", myTagArray);
        myTask.put("due-date", Jsonifier.dueDateToJson(task.getDueDate()));
        myTask.put("priority", Jsonifier.priorityToJson(task.getPriority()));
        myTask.put("status", task.getStatus());
        return myTask;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray myTaskList = new JSONArray();
        if (tasks.size() > 1) {
            for (Task t : tasks) {
                myTaskList.put(Jsonifier.taskToJson(t));
            }
        } else {
            myTaskList.put(Jsonifier.taskToJson(tasks.get(0)));
        }
        return myTaskList;
    }
}
