package parsers;

import model.DueDate;
import model.Priority;
import model.Status;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import utility.JsonFileIO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

// Represents Task parser
public class TaskParser {
    private boolean addTask;
    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray

    public List<Task> parse(String input) {
        JSONArray jsonArray = new JSONArray(input);
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            addTask = true;
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String string = "";
            if (jsonObject.toString().contains("description")) {
                string = jsonObject.get("description").toString();
            }
            Task task = new Task(string);
            dateStatusPriorityAndTags(jsonObject, task);

            taskList.add(task);

        }
        return taskList;
    }

    private void dateStatusPriorityAndTags(JSONObject jsonObject, Task task) {
        dueDate(jsonObject, task);
        if (jsonObject.toString().contains("status") && addTask) {
            task.setStatus(makeStatus(jsonObject.get("status").toString()));
        } else {
            addTask = false;
        }
        handlePriority(jsonObject, task);
        if (jsonObject.toString().contains("tags") && addTask) {
            handleTags(jsonObject, task);
        } else {
            addTask = false;
        }
    }

    private void handlePriority(JSONObject jsonObject, Task task) {
        if (jsonObject.toString().contains("priority") && addTask) {
            if (jsonObject.get("priority").toString().contains("important")
                    && jsonObject.get("priority").toString().contains("urgent")) {
                task.setPriority(makePriority((JSONObject) jsonObject.get("priority")));
            } else {
                addTask = false;
            }
        } else {
            addTask = false;
        }
    }

    private void handleTags(JSONObject jsonObject, Task task) {
        JSONArray tagsList = jsonObject.getJSONArray("tags");
        for (int n = 0; n < tagsList.length(); n++) {
            task.addTag(tagsList.get(n).toString());
        }
    }

    private void dueDate(JSONObject jsonObject, Task task) {
        if (jsonObject.toString().contains("due-date") && addTask) {
            if (jsonObject.get("due-date") == JSONObject.NULL) {
                task.setDueDate(null);
            } else {
                task.setDueDate(makeDueDate(jsonObject.getJSONObject("due-date")));
            }
        } else {
            addTask = false;
        }
    }

    private DueDate makeDueDate(JSONObject jsonObject) {
        DueDate dueDate = new DueDate();
        Calendar calendar = new GregorianCalendar();
        dueDate.setDueDate(checkDate(calendar, jsonObject).getTime());
        return dueDate;
    }

    private Calendar checkDate(Calendar calendar, JSONObject jsonObject) {
        if (jsonObject.toString().contains("month")) {
            calendar.set(Calendar.MONTH, jsonObject.getInt("month"));
        }
        if (jsonObject.toString().contains("hour")) {
            calendar.set(Calendar.HOUR_OF_DAY, jsonObject.getInt("hour"));
        }
        if (jsonObject.toString().contains("year")) {
            calendar.set(Calendar.YEAR, (int) jsonObject.get("year"));
        }
        if (jsonObject.toString().contains("day")) {
            calendar.set(Calendar.DAY_OF_MONTH, jsonObject.getInt("day"));
        }
        if (jsonObject.toString().contains("minute")) {
            calendar.set(Calendar.MINUTE, jsonObject.getInt("minute"));
        }
        return calendar;
    }

    private Priority makePriority(JSONObject jsonObject) {
        Priority priority = new Priority();
        priority.setImportant(true);
        priority.setUrgent(true);
        if (jsonObject.getBoolean("important") && jsonObject.getBoolean("urgent")) {
            return priority;
        } else if (jsonObject.getBoolean("important") && !(jsonObject.getBoolean("urgent"))) {
            priority.setUrgent(false);
            return priority;
        } else if (!(jsonObject.getBoolean("important")) && !(jsonObject.getBoolean("urgent"))) {
            priority.setImportant(false);
            priority.setUrgent(false);
            return priority;
        } else if (!(jsonObject.getBoolean("important")) && (jsonObject.getBoolean("urgent"))) {
            priority.setImportant(false);
            return priority;
        } else {
            return new Priority();
        }
    }

    private Status makeStatus(String string) {
        if (string.toLowerCase().contains("in_progress")) {
            return Status.IN_PROGRESS;
        } else if (string.toLowerCase().contains("todo")) {
            return Status.TODO;
        } else if (string.toLowerCase().contains("up_next")) {
            return Status.UP_NEXT;
        } else if (string.toLowerCase().contains("done")) {
            return Status.DONE;
        } else {
            return null;
        }
    }

}
