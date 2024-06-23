package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File file;

    @BeforeEach
    public void beforeEach() {
        assertDoesNotThrow(() -> {
            this.file = File.createTempFile("test", "csv");
            this.taskManager = new FileBackedTaskManager(file);
        });
    }

    @Test
    void shouldStoreOnlyTitleIfNoTasksAdded() {
        assertDoesNotThrow(() -> {
            String[] lines = Files.readString(file.toPath()).split("\n");
            assertEquals(lines.length, 1);
            assertEquals(lines[0], "id,type,name,status,description,epic,start_time,duration");
        });
    }

    @Test
    void shouldStoreTasksToFile() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

            Task task = new Task("Подстричь волосы", "Взять ножницы",
                    LocalDateTime.of(2024, Month.JUNE, 19, 10, 10), Duration.ofMinutes(1));
            taskManager.createNewTask(task);

            Epic epic = new Epic("Купить машину", "Найти консультанта");
            taskManager.createNewEpic(epic);

            Subtask subTask = new Subtask("Поиск консультанта", "Заплатить консультанту",
                    LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(2),
                    epic.getTaskId());
            taskManager.createNewSubtask(subTask);

            String[] lines = Files.readString(file.toPath()).split("\n");

            assertEquals(lines.length, 4);

            assertEquals(lines[0], "id,type,name,status,description,epic,start_time,duration");
            assertEquals(lines[1], "1,TASK,Подстричь волосы,NEW,Взять ножницы,,19.06.24 10:10,1");
            assertEquals(lines[2], "2,EPIC,Купить машину,NEW,Найти консультанта,,19.06.24 10:20,2");
            assertEquals(lines[3], "3,SUBTASK,Поиск консультанта,NEW,Заплатить консультанту,2,19.06.24 10:20,2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldLoadNoTasksFromEmptyFile() {
        try {
            File file = File.createTempFile("test", "csv");
            FileBackedTaskManager taskManager = FileBackedTaskManager.loadFromFile(file);

            assertEquals(taskManager.getListAllSubtasks().size(), 0);
            assertEquals(taskManager.getListAllTasks().size(), 0);
            assertEquals(taskManager.getListAllEpics().size(), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}