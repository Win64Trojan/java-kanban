/**
 * Данный класс создан для выполнения операций самого приложения.
 * <p>
 * В него входят следующий поля:
 * 1. Идентификационный номер - Который присваивается при создании новой задачи.
 * При этом у каждой задачи свой универсальный номер, который не будет повторяться.
 * 2. final HashMap - В которых будут храниться все задачи, эпики и подзадачи.
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
 */


import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private static int idNumber = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // Создание. Объект должен передаваться в качестве параметров
    public void createNewTask(Task newTask) {
        newTask.setTaskId(++idNumber);
        tasks.put(idNumber, newTask);
    }

    public void createNewEpic(Epic newEpic) {
        newEpic.setTaskId(++idNumber);
        epics.put(idNumber, newEpic);
    }

    public void createNewSubtask(Subtask newSubtask) {
        newSubtask.setTaskId(++idNumber);
        subtasks.put(idNumber, newSubtask);
        int epicId = newSubtask.getEpicId();
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        subtaskIds.add(idNumber);
        checkEpicStatus(epicId);
    }


    // получение списка всех задач
    public ArrayList<Task> getListAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getListAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getListAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }


    // получение по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // получение списка всех подзадач, определенного эпика
    public ArrayList<Subtask> getListSubtaskByIdEpic(int id) {
        ArrayList<Integer> subtaskIds = epics.get(id).getSubtaskIds();
        ArrayList<Subtask> subtasksByOneEpic = new ArrayList<>();
        for (int subtaskId : subtaskIds) {
            subtasksByOneEpic.add(subtasks.get(subtaskId));
        }
        return subtasksByOneEpic;
    }

    // Обновление задачи
    public void updateTheTask(Task updateTask) {
        tasks.put(updateTask.getTaskId(), updateTask);
    }

    public void updateTheEpic(Epic updateEpic) {
        epics.put(updateEpic.getTaskId(), updateEpic);
    }

    public void updateTheSubtask(Subtask updateSubtask) {
        subtasks.put(updateSubtask.getTaskId(), updateSubtask);
        int epicId = subtasks.get(updateSubtask.getTaskId()).getEpicId();
        checkEpicStatus(epicId);
    }

    // Удаление всех задач
    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            int epicId = epic.getTaskId();
            ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
            for (Integer subtaskId : subtaskIds) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    public void removeAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.getSubtaskIds().clear();
            checkEpicStatus(epic.getTaskId());
        }
        subtasks.clear();
    }

    // Удаление по идентификатору
    public void removeTaskById(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpicById(int epicId) {
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    public void removeSubtaskById(int subtaskIdForRemove) {
        int epicId = subtasks.get(subtaskIdForRemove).getEpicId();
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        subtaskIds.remove((Integer) subtaskIdForRemove);
        subtasks.remove(subtaskIdForRemove);
        checkEpicStatus(epicId);
    }


    // проверка статуса
    private void checkEpicStatus(int epicId) {
        int counterNew = 0;
        int counterDone = 0;
        ArrayList<Integer> subtaskIds = epics.get(epicId).getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            if (subtasks.get(subtaskId).getProgressStatus().equals(ProgressStatus.NEW)) {
                counterNew++;
            } else if (subtasks.get(subtaskId).getProgressStatus().equals(ProgressStatus.DONE)) {
                counterDone++;
            }
        }
        if (subtaskIds.size() == counterNew || subtaskIds.isEmpty()) {
            epics.get(epicId).setProgressStatus(ProgressStatus.NEW);
        } else if (subtaskIds.size() == counterDone) {
            epics.get(epicId).setProgressStatus(ProgressStatus.DONE);
        } else {
            epics.get(epicId).setProgressStatus(ProgressStatus.IN_PROGRESS);
        }
    }
}