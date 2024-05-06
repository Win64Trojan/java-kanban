package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class InMemoryHistoryManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void creatingTestManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void checkTaskDoesNotChangeInHistory() {
        Task task = manager.createNewTask(new Task("task", ""));
        manager.getTaskById(task.getTaskId());
        task.setTaskName("TASK1");
        manager.updateTask(task);

        Assertions.assertNotEquals(
                manager.getHistory().getFirst().getTaskName(),
                manager.getTaskById(task.getTaskId()).getTaskName());
    }

    @Test
    public void checkEpicDoesNotChangeInHistory() {
        Epic epic = manager.createNewEpic(new Epic("Epic", ""));
        manager.getEpicById(epic.getTaskId());
        epic.setTaskName("EPIC1");
        manager.updateEpic(epic);

        Assertions.assertNotEquals(
                manager.getHistory().getFirst().getTaskName(),
                manager.getEpicById(epic.getTaskId()).getTaskName());
    }

    @Test
    public void checkSubtaskDoesNotChangeInHistory() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask subtask = manager.createNewSubtask(new Subtask("Subtask", "", epic.getTaskId()));
        manager.getSubtaskById(subtask.getTaskId());
        subtask.setTaskName("SUBTASK1");
        manager.updateSubtask(subtask);

        Assertions.assertNotEquals(
                manager.getHistory().getFirst().getTaskName(),
                manager.getSubtaskById(subtask.getTaskId()).getTaskName());
    }


}