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
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

    private Integer idNumber = 0;

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    @Override
    public Task createNewTask(Task newTask) {
        newTask.setTaskId(++idNumber);
        tasks.put(newTask.getTaskId(), newTask);
        updatePrioritizedTasks();
        return newTask;
    }

    @Override
    public Epic createNewEpic(Epic newEpic) {
        newEpic.setTaskId(++idNumber);
        epics.put(newEpic.getTaskId(), newEpic);
        updatePrioritizedTasks();

        return newEpic;
    }

    @Override
    public Subtask createNewSubtask(Subtask newSubtask) {

        if (epics.containsKey(newSubtask.getEpicId())) {
            newSubtask.setTaskId(++idNumber);
            subtasks.put(newSubtask.getTaskId(), new Subtask(newSubtask));
            Epic epic = epics.get(newSubtask.getEpicId());
            epic.addSubtask(subtasks.get(newSubtask.getTaskId()));

            if (epic.getStartTime() == null || epic.getStartTime().isAfter(newSubtask.getStartTime())) {
                epic.setStartTime(newSubtask.getStartTime());
            }

            if (epic.getEndTime() == null || epic.getEndTime().isBefore(newSubtask.getEndTime())) {
                epic.setEndTime(newSubtask.getEndTime());
            }

            epic.setDuration(epic.getDuration().plus(newSubtask.getDuration()));

            epic.updateStatus();
        }
        updatePrioritizedTasks();

        return newSubtask;
    }

    // получение списка всех задач
    @Override
    public List<Task> getListAllTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<Epic> getListAllEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public List<Subtask> getListAllSubtasks() {
        return subtasks.values().stream().toList();
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
        updatePrioritizedTasks();

        return new Task(newTask);
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        // обновить можно только имя и описание
        if (epics.containsKey(newEpic.getTaskId())) {
            Epic epic = epics.get(newEpic.getTaskId());
            epic.setTaskName(newEpic.getTaskName());
            epic.setTaskDescription(newEpic.getTaskDescription());

            updatePrioritizedTasks();

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
            updatePrioritizedTasks();
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
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
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
        updatePrioritizedTasks();
    }

    // Удаление по идентификатору
    @Override
    public void removeTaskById(Integer taskId) {
        tasks.remove(taskId);
        historyManager.remove(taskId);
        updatePrioritizedTasks();
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
            updatePrioritizedTasks();
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
            updatePrioritizedTasks();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public boolean isIntersectedTasks(Task task1, Task task2) {
        return ((task1.getStartTime().isBefore(task2.getEndTime()))
                && (task2.getStartTime().isBefore(task1.getEndTime())));
    }

    private void updatePrioritizedTasks() {
        prioritizedTasks.clear();
        for (Task task : tasks.values()) {
            if (task.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(task);
        }

        for (Subtask subTask : subtasks.values()) {
            if (subTask.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(subTask);
        }

        for (Epic epic : epics.values()) {
            if (epic.getStartTime() == null) {
                continue;
            }
            prioritizedTasks.add(epic);
        }

    }

    private boolean isIntersectsExistingTask(Task input) {
        List<Task> intersectedTasks = prioritizedTasks.stream()
                .filter((task) -> isIntersectedTasks(input, task))
                .toList();
        return !intersectedTasks.isEmpty();
    }


}