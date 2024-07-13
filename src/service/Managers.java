package service;

import java.io.File;

public class Managers {

    static final File file = new File("./saveTasks.csv");

    private Managers() {
    }


    public static TaskManager getDefault() {
        if (file.exists()) {
            return FileBackedTaskManager.loadFromFile(file);
        }
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

