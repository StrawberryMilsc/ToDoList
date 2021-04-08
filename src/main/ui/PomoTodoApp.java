package ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Task;
import utility.JsonFileIO;

import java.util.ArrayList;
import java.util.List;

// The PomoTODO GUI Application
public class PomoTodoApp extends Application {
    public static final String TITLE = "PomoTODO";
    public static final double WIDTH = 520;
    public static final double HEIGHT = 800;
    private static List<Task> tasks = new ArrayList<>();
    private static Stage primaryStage;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    // EFFECTS: returns the primary Stage
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    // REQUIRES: stage != null
    // MODIFIES: this
    // EFFECTS: sets the primary stage
    private static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    
    // EFFECTS: returns the list of tasks in this PomoTODO APP
    public static List<Task> getTasks() {
        JsonFileIO.write(tasks);
        return tasks;
    }

    public static void deleteTask(Task task) {
        int delete = -1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).equals(task)) {
                delete = i;
            }
        }
        if (delete >= 0) {
            tasks.remove(delete);
        }

        setScene(new ListView(tasks));
        JsonFileIO.write(tasks);
    }

    public static void editTask() {
        setScene(new ListView(tasks));
        JsonFileIO.write(tasks);
    }

    public static void cancelTask() {
        setScene(new ListView(tasks));
    }

    
    // REQUIRES: primaryStage != null AND root != null
    public static void setScene(Parent root) {
        try {
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            primaryStage.setTitle(TITLE);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Failed to load new Scene!");
        }
    }
    
    // EFFECTS: Application starts here!
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (!(JsonFileIO.read() == null)) {
            tasks.addAll(JsonFileIO.read());
        }
        setPrimaryStage(primaryStage);
        setScene(new ListView(tasks));
    }
}
