package utility;

import model.Task;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.*;
import java.util.List;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");

    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        TaskParser taskParser = new TaskParser();
        try {
            FileReader fileReader = new FileReader(jsonDataFile.toString());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String s = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                s = s.concat("\n" + line);
            }
            return taskParser.parse(s);
        } catch (IOException e) {
            //
        }
        return null;

    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        try {
            FileWriter fileWriter = new FileWriter(jsonDataFile.toString());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(Jsonifier.taskListToJson(tasks).toString(3));
            bufferedWriter.close();
        } catch (IOException e) {
            //
        }
    }
}
