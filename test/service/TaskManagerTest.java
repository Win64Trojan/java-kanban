package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    @Test
    void addNewTask() {
        assertDoesNotThrow(() -> {
            Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createNewTask(task);
            int taskId = task.getTaskId();

            final Task savedTask = taskManager.getTaskById(taskId);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = taskManager.getListAllTasks();

            assertNotNull(tasks, "Задачи не возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.createNewEpic(epic);
        int epicId = epic.getTaskId();

        final Epic savedTask = taskManager.getEpicById(epicId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getListAllEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubTask() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
            taskManager.createNewEpic(epic);
            Integer epicId = epic.getTaskId();

            Subtask subTask = new Subtask("Test addNewSubTAsk", "Test addNewSubTAsk description", LocalDateTime.of(2024, Month.JUNE, 22, 23, 07), Duration.ofMinutes(1), epicId);
            taskManager.createNewSubtask(subTask);

            final Subtask savedTask = taskManager.getSubtaskById(subTask.getTaskId());

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(subTask, savedTask, "Задачи не совпадают.");

            final List<Subtask> subTasks = taskManager.getListAllSubtasks();

            assertNotNull(subTasks, "Задачи не возвращаются.");
            assertEquals(1, subTasks.size(), "Неверное количество задач.");
            assertEquals(subTask, subTasks.getFirst(), "Задачи не совпадают.");
        });
    }

    @Test
    void shouldReturnEpicById() {
        Epic epic = new Epic("test", "test");
        taskManager.createNewEpic(epic);

        Epic epic2 = taskManager.getEpicById(epic.getTaskId());

        Assertions.assertEquals(epic, epic2);
    }

    @Test
    void shouldReturnTaskById() {
        assertDoesNotThrow(() -> {
            Task task = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createNewTask(task);

            Task task2 = taskManager.getTaskById(task.getTaskId());

            Assertions.assertEquals(task, task2);
        });
    }

    @Test
    void shouldReturnSubTaskById() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createNewSubtask(subTask);

            Subtask subTask2 = taskManager.getSubtaskById(subTask.getTaskId());

            Assertions.assertEquals(subTask, subTask2);
        });
    }

    @Test
    void shouldUpdateIdsOnCreate() {
        Epic epic = new Epic("test", "test");
        epic.setTaskId(0);
        taskManager.createNewEpic(epic);

        Epic epic2 = new Epic("test", "test");
        epic.setTaskId(0);
        taskManager.createNewEpic(epic);

        Assertions.assertNotEquals(epic.getTaskId(), epic2.getTaskId());
    }

    @Test
    void shouldEqualTaskFieldsBeforeAndAfterCreate() {
        assertDoesNotThrow(() -> {
            Task oldTask = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1));
            taskManager.createNewTask(oldTask);

            Task newTask = taskManager.getTaskById(oldTask.getTaskId());


            Assertions.assertEquals(oldTask.getTaskName(), newTask.getTaskName());
            Assertions.assertEquals(oldTask.getTaskDescription(), newTask.getTaskDescription());
            Assertions.assertEquals(oldTask.getStatus(), newTask.getStatus());
            Assertions.assertEquals(oldTask.getClass(), newTask.getClass());
        });
    }

    @Test
    void shouldNotSubtasksIdEqualToEpicId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);

            Subtask subTask = new Subtask("test", "test",
                    LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createNewSubtask(subTask);

            List<Subtask> subTasksBeforeUpdate = taskManager.getListAllSubtasks();

            subTask.setTaskId(1);
            taskManager.updateSubtask(subTask);

            List<Subtask> subTasksAfterUpdate = taskManager.getListAllSubtasks();

            Assertions.assertArrayEquals(subTasksBeforeUpdate.toArray(), subTasksAfterUpdate.toArray());
        });
    }

    @Test
    void shouldNotEpicContainRemovedTaskId() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), 1);
            taskManager.createNewSubtask(subTask);

            taskManager.removeSubtaskById(subTask.getTaskId());

            List<Subtask> subTasksList = taskManager.getAllSubtasksOfEpic(epic);
            Assertions.assertEquals(subTasksList.size(), 0);
        });
    }

    @Test
    void shouldReturnTrueWhenTaskIncludesAnotherTask() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(2));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldReturnTrueWhenTaskInctersectsAnotherTask() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 05), Duration.ofMinutes(15));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertTrue(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertTrue(isIntersected);
    }

    @Test
    void shouldBeFalseWhenTasksRangesAreNotIntersects() {
        Task task1 = new Task("task1", "task1", LocalDateTime.of(2024, 06, 19, 15, 00), Duration.ofMinutes(10));

        Task task2 = new Task("task2", "task2", LocalDateTime.of(2024, 06, 19, 15, 10), Duration.ofMinutes(15));

        boolean isIntersected = taskManager.isIntersectedTasks(task1, task2);

        Assertions.assertFalse(isIntersected);

        isIntersected = taskManager.isIntersectedTasks(task2, task1);

        Assertions.assertFalse(isIntersected);
    }

    @Test
    void shouldReturnNewIfAllSubTasksOfEpicAreNew() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);
            int epicId = epic.getTaskId();

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            taskManager.createNewSubtask(subTask);

            Subtask subTask2 = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            taskManager.createNewSubtask(subTask2);

            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getStatus(), Status.NEW);
        });
    }

    @Test
    void shouldReturnDoneIfAllSubTasksOfEpicAreDone() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);
            Integer epicId = epic.getTaskId();

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setStatus(Status.DONE);
            taskManager.createNewSubtask(subTask);

            Subtask subTask2 = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setStatus(Status.DONE);
            taskManager.createNewSubtask(subTask2);

            epic = taskManager.getEpicById(epicId);
            assertEquals(epic.getStatus(), Status.DONE);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInNewAndDoneStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);
            int epicId = epic.getTaskId();

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setStatus(Status.NEW);
            taskManager.createNewSubtask(subTask);

            Subtask subTask2 = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setStatus(Status.DONE);
            taskManager.createNewSubtask(subTask2);


            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        });
    }

    @Test
    void shouldReturnInProgressIfSubTasksOfEpicInProgressStatus() {
        assertDoesNotThrow(() -> {
            Epic epic = new Epic("test", "test");
            taskManager.createNewEpic(epic);
            int epicId = epic.getTaskId();

            Subtask subTask = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(1), epicId);
            subTask.setStatus(Status.IN_PROGRESS);
            taskManager.createNewSubtask(subTask);

            Subtask subTask2 = new Subtask("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1), epicId);
            subTask2.setStatus(Status.IN_PROGRESS);
            taskManager.createNewSubtask(subTask2);

            epic = taskManager.getEpicById(epicId);

            assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        });
    }

    @Test
    void shouldThrowNothingOnNoIntersectedTaskTimes() {
        assertDoesNotThrow(() -> {
            Task task1 = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 20), Duration.ofMinutes(20));
            Task task2 = new Task("test", "test", LocalDateTime.of(2024, Month.JUNE, 19, 10, 50), Duration.ofMinutes(1));
            taskManager.createNewTask(task1);
            taskManager.createNewTask(task2);
        });
    }
}
