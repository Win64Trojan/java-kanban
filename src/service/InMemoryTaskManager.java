package service;
/**
 * Данный класс создан для выполнения операций самого приложения.
 * <p>
 * В него входят следующий поля:
 * 1. Идентификационный номер - Который присваивается при создании новой задачи.
 * При этом у каждой задачи свой универсальный номер, который не будет повторяться.
 * 2. final Map - В которых будут храниться все задачи, эпики и подзадачи.
 * <p>
 * Данный клас реализует следующий методы для всех типов задач, эпиков и подзадач.
 * 1. Создание
 * 2. Получение списка всех задач
 * 3. Получение по идентификатору
 * 4. Получение списка всех подзадач определённого эпика.
 * 5. Обновление задачи
 * 6. Удаление всех задач
 * 7. Удаление по идентификатору
 * 8. Проверка статуса
 * 9. Получение истории просмотров задач
 */


import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private Integer idNumber = 0;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createNewTask(Task newTask) {
        newTask.setTaskId(++idNumber);
        tasks.put(newTask.getTaskId(), newTask);
        return newTask;
    }

    @Override
    public Epic createNewEpic(Epic newEpic) {
        newEpic.setTaskId(++idNumber);
        epics.put(newEpic.getTaskId(), newEpic);
        return newEpic;
    }

    @Override
    public Subtask createNewSubtask(Subtask newSubtask) {
        if (epics.containsKey(newSubtask.getEpicId())) {
            newSubtask.setTaskId(++idNumber);
            subtasks.put(newSubtask.getTaskId(), new Subtask(newSubtask));
            Epic epic = epics.get(newSubtask.getEpicId());
            epic.addSubtask(subtasks.get(newSubtask.getTaskId()));
            epic.updateStatus();
        }
        return newSubtask;
    }

    // получение списка всех задач
    @Override
    public List<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getListAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getListAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // получение по идентификатору
    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return subtask;
    }


    // Обновление задачи
    @Override
    public Task updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getTaskId())) {
            tasks.put(newTask.getTaskId(), new Task(newTask));
        } else {
            System.err.println("Данной задачи не существует");
        }
        return new Task(newTask);
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        // обновить можно только имя и описание
        if (epics.containsKey(newEpic.getTaskId())) {
            Epic epic = epics.get(newEpic.getTaskId());
            epic.setTaskName(newEpic.getTaskName());
            epic.setTaskDescription(newEpic.getTaskDescription());

            return new Epic(epic);
        }
        return newEpic;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getTaskId()) &&
                epics.containsKey(newSubtask.getEpicId())) {
            subtasks.put(newSubtask.getTaskId(), new Subtask(newSubtask));
            Epic epic = epics.get(newSubtask.getEpicId());
            epic.updateSubtask(subtasks.get(newSubtask.getTaskId()));
            epic.updateStatus();
        }
        return newSubtask;
    }

    // получение списка всех подзадач, определенного эпика
    @Override
    public List<Subtask> getAllSubtasksOfEpic(Epic epic) {
        if (epics.containsKey(epic.getTaskId())) {
            return epic.getSubtasks();
        }
        return new ArrayList<>();
    }

    // Удаление всех задач
    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : subtasks.keySet()) {
            subtasks.remove(id);
            historyManager.remove(id);
        }
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasks();
            epic.updateStatus();
        }
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    // Удаление по идентификатору
    @Override
    public void removeTaskById(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
    }

    @Override
    public void removeEpicById(Integer epicId) {
        if (epics.containsKey(epicId)) {
            for (Subtask subtask : epics.get(epicId).getSubtasks()) {
                subtasks.remove(subtask.getTaskId());
                historyManager.remove(subtask.getTaskId());
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void removeSubtaskById(Integer subtaskIdForRemove) {
        if (subtasks.containsKey(subtaskIdForRemove)) {
            Epic epic = getEpicById(subtasks.get(subtaskIdForRemove).getEpicId());
            epic.removeSubtask(subtaskIdForRemove);
            epic.updateStatus();
            subtasks.remove(subtaskIdForRemove);
            historyManager.remove(subtaskIdForRemove);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}