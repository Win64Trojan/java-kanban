package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    // Создание. Объект должен передаваться в качестве параметров
    Task createNewTask(Task newTask);

    Epic createNewEpic(Epic newEpic);

    Subtask createNewSubtask(Subtask newSubtask);

    // получение списка всех задач
    List<Task> getListAllTasks();

    List<Epic> getListAllEpics();

    List<Subtask> getListAllSubtasks();

    // получение по идентификатору
    Task getTaskById(Integer id);

    Epic getEpicById(Integer id);

    Subtask getSubtaskById(Integer id);

    // Обновление задачи
    Task updateTask(Task newTask);

    Epic updateEpic(Epic newEpic);

    Subtask updateSubtask(Subtask newSubtask);

    // получение списка всех подзадач, определенного эпика
    List<Subtask> getAllSubtasksOfEpic(Epic epic);

    // Удаление всех задач
    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    // Удаление по идентификатору
    void removeTaskById(Integer taskId);

    void removeEpicById(Integer epicId);

    void removeSubtaskById(Integer subtaskIdForRemove);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    boolean isIntersectedTasks(Task task1, Task task2);
}
