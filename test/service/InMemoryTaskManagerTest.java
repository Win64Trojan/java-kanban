package service;


import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;


class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void creatingTestManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void SubtaskCanNotBeEpicToItself() {
        Subtask subtask = manager.createNewSubtask(new Subtask("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 0));
        Assertions.assertNull(subtask.getTaskId());
    }

    @Test
    public void checkThatManagerCanCreateAndGiveSubtaskById() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask subtask = manager.createNewSubtask(new Subtask("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic.getTaskId()));
        Assertions.assertNotNull(manager.getSubtaskById(subtask.getTaskId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveEpicById() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Assertions.assertNotNull(manager.getEpicById(epic.getTaskId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveTaskById() {
        Task task = manager.createNewTask(new Task("", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1)));
        Assertions.assertNotNull(manager.getTaskById(task.getTaskId()));
    }

    @Test
    public void checkImmutabilityOfEpicWhenCreatedValueChanged() {
        Epic created = manager.createNewEpic(new Epic("a", ""));
        Epic modifiedEpic = new Epic(created.getTaskName(), created.getTaskDescription());
        modifiedEpic.setTaskName("b");
        Assertions.assertNotEquals(modifiedEpic.getTaskName(), manager.getEpicById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfEpicWhenSourceChanged() {
        Epic source = new Epic("a", "");
        Epic created = manager.createNewEpic(new Epic(source.getTaskName(), source.getTaskDescription()));
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getEpicById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfSubtaskWhenCreatedValueChanged() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask created = manager.createNewSubtask(new Subtask("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic.getTaskId()));
        created.setTaskName("b");
        Assertions.assertNotEquals(created.getTaskName(), manager.getSubtaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfSubtaskWhenSourceChanged() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask source = new Subtask("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epic.getTaskId());
        Subtask created = manager.createNewSubtask(source);
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getSubtaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenCreatedValueChanged() {
        Task created = manager.createNewTask(new Task("a", "", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1)));
        Task retrieved = manager.getTaskById(created.getTaskId());
        Task updated = new Task(retrieved);
        updated.setTaskName("b");
        Assertions.assertNotEquals(updated.getTaskName(), manager.getTaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenSourceChanged() {
        Task source = new Task("a", "",
                LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
        Task created = manager.createNewTask(new Task(source));
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getTaskById(created.getTaskId()).getTaskName());
    }
}