package service;


import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void creatingTestManager() {
        manager = Managers.getDefault();
    }

    @Test
    public void SubtaskCanNotBeEpicToItself() {
        Subtask subtask = manager.createNewSubtask(new Subtask("", "", 0));
        Assertions.assertNull(subtask.getTaskId());
    }

    @Test
    public void checkThatManagerCanCreateAndGiveSubtaskById() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask subtask = manager.createNewSubtask(new Subtask("", "", epic.getTaskId()));
        Assertions.assertNotNull(manager.getSubtaskById(subtask.getTaskId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveEpicById() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Assertions.assertNotNull(manager.getEpicById(epic.getTaskId()));
    }

    @Test
    public void checkThatManagerCanCreateAndGiveTaskById() {
        Task task = manager.createNewTask(new Task("", ""));
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
        Subtask created = manager.createNewSubtask(new Subtask("a", "", epic.getTaskId()));
        created.setTaskName("b");
        Assertions.assertNotEquals(created.getTaskName(), manager.getSubtaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfSubtaskWhenSourceChanged() {
        Epic epic = manager.createNewEpic(new Epic("", ""));
        Subtask source = new Subtask("a", "", epic.getTaskId());
        Subtask created = manager.createNewSubtask(source);
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getSubtaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenCreatedValueChanged() {
        Task created = manager.createNewTask(new Task("a", ""));
        Task retrieved = manager.getTaskById(created.getTaskId());
        Task updated = new Task(retrieved);
        updated.setTaskName("b");
        Assertions.assertNotEquals(updated.getTaskName(), manager.getTaskById(created.getTaskId()).getTaskName());
    }

    @Test
    public void checkImmutabilityOfTaskWhenSourceChanged() {
        Task source = new Task("a", "");
        Task created = manager.createNewTask(new Task(source));
        source.setTaskName("b");
        Assertions.assertNotEquals(source.getTaskName(), manager.getTaskById(created.getTaskId()).getTaskName());
    }
}