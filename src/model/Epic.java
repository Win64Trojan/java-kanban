/**
 * Данный класс наследуется от model.Task и в него добавлено новое поле, а именно HashMap, для хранения сабтасков.
 * Благодаря ему мы сможем получить все подзадачи в определенном эпике.
 * <p>
 * В данный класс добавлены методы для работы с сабтасками
 * <p>
 * Также в данном классе переопределен toString
 */
package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {

    private final HashMap<Integer, Subtask> subtasks;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, null, Duration.ofMinutes(0));
        subtasks = new HashMap<>();
    }

    public Epic(Epic epic) {
        super(epic.getTaskName(), epic.getTaskDescription(), null, Duration.ofMinutes(0));
        this.taskId = getTaskId();
        this.endTime = null;
        this.subtasks = new HashMap<>();
    }

    private HashMap<Integer, Subtask> deepCopyHashMap(Epic epic) {
        HashMap<Integer, Subtask> map = new HashMap<>();

        for (Map.Entry<Integer, Subtask> entry : epic.subtasks.entrySet()) {
            map.put(entry.getKey(), new Subtask(entry.getValue()));
        }
        return map;
    }

    private ArrayList<Subtask> deepCopyArrayList() {
        ArrayList<Subtask> arrayList = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            arrayList.add(new Subtask(subtask));
        }
        return arrayList;
    }

    public void addSubtask(Subtask newSubtask) {
        subtasks.putIfAbsent(newSubtask.getTaskId(), newSubtask);
    }

    public void updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getTaskId())) {
            subtasks.put(newSubtask.getTaskId(), newSubtask);
        }
    }

    public ArrayList<Subtask> getSubtasks() {
        return deepCopyArrayList();
    }

    public void removeSubtask(Integer id) {
        subtasks.remove(id);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void setStatus(Status status) {
    }

    public void updateStatus() {
        // Если список подзадач пуст, то ставим статус NEW и ничего не проверяем
        if (subtasks.isEmpty()) {
            status = Status.NEW;
            return;
        }

        int countDone = 0;
        int countNew = 0;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() == Status.NEW) {
                countNew++;
            } else if (subtask.getStatus() == Status.DONE) {
                countDone++;
            }
        }

        if (countDone == subtasks.size()) {
            status = Status.DONE;
        } else if (countNew == subtasks.size()) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {

        String startTimeString = "";
        String endTimeString = "";

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

        if (startTime != null) {
            startTimeString = startTime.format(dateTimeFormatter);
        }

        if (endTime != null) {
            endTimeString = endTime.format(dateTimeFormatter);
        }

        return "Epic{" +
                " id=" + taskId +
                ", subtasksSize=" + subtasks.size() +
                ", name='" + taskName + '\'' +
                ", description='" + taskDescription + '\'' +
                ", status=" + status +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                ", end_time=" + endTimeString +
                '}';
    }
}