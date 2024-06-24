/**
 * Данный класс предназначен для задач, который включает в себя следующие поля:
 * 1. Название задачи
 * 2. Описание задачи
 * 3. Номер задачи
 * 4. Статус прогресса (NEW, IN_PROGRESS, DONE), который  реализован в enum ProgressStatus.
 * <p>
 * Также в данном классе реализованы методы:
 * 1. Геттеры и сеттеры
 * 2. Переопределенные методы equals-hashCode-toString.
 */
package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task implements Comparable<Task> {

    protected String taskName;
    protected String taskDescription;
    protected Integer taskId;
    protected Status status = Status.NEW;  // статус прогресса задачи
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String taskName, String taskDescription, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Task task) {
        this(task.taskName, task.taskDescription, task.startTime, task.duration);
        this.status = task.status;
        this.taskId = task.taskId;
    }


    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public int compareTo(Task t) {
        if (startTime == null || startTime.isAfter(t.getStartTime())) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) &&
                status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId);
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            startTimeString = startTime.format(dateTimeFormatter);
        }
        return "model.Task{" +
                "id задачи=" + taskId +
                "Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Статус задачи=" + status +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                '}';
    }
}