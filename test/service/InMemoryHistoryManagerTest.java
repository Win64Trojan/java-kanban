package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class InMemoryHistoryManagerTest {

    private TaskManager manager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void creatingTestManager() {
        manager = Managers.getDefault();

        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void checkTaskDoesNotChangeInHistory() {
        Task task = manager.createNewTask(new Task("task", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1)));
        manager.getTaskById(task.getTaskId());
        task.setTaskName("TASK1");
        manager.updateTask(task);

        List<Task> history = historyManager.getHistory();
        for (Task historyTasks : history) {
            assertNotEquals(manager.getTaskById(task.getTaskId()).getTaskName(), historyTasks);
        }
    }

    @Test
    public void checkEpicDoesNotChangeInHistory() {
        Epic epic = manager.createNewEpic(new Epic("Epic", ""));
        manager.getEpicById(epic.getTaskId());
        epic.setTaskName("EPIC1");
        manager.updateEpic(epic);

        List<Task> history = historyManager.getHistory();
        for (Task historyTasks : history) {
            assertNotEquals(manager.getEpicById(epic.getTaskId()).getTaskName(), historyTasks);
        }
    }

    @Test
    public void checkSubtaskDoesNotChangeInHistory() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask subtask = manager.createNewSubtask(new Subtask("Subtask", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic.getTaskId()));
        manager.getSubtaskById(subtask.getTaskId());
        subtask.setTaskName("SUBTASK1");
        manager.updateSubtask(subtask);

        List<Task> history = historyManager.getHistory();
        for (Task historyTasks : history) {
            assertNotEquals(manager.getSubtaskById(subtask.getTaskId()).getTaskName(), historyTasks);
        }
    }


    @Test
    public void checkRemoveTaskById() {

        Task task = new Task("Task", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        task.setTaskId(56);

        historyManager.add(task);

        Task task2 = new Task("Task2", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        task2.setTaskId(84);

        historyManager.add(task2);

        Task task3 = new Task("Task3", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        task3.setTaskId(300);

        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size());

        historyManager.remove(task2.getTaskId());

        history = historyManager.getHistory();

        assertEquals(2, history.size());

        for (Task historyTasks : history) {
            assertNotEquals(task2, historyTasks);
        }
    }


    @Test
    void checkEmptyAfterRemoveIfSingleTask() {

        Task task = new Task("test", "test description",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        task.setTaskId(1);

        historyManager.add(task);

        historyManager.remove(task.getTaskId());

        List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size());
    }


}


