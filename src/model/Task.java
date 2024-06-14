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

import java.util.Objects;

public class Task {

    protected String taskName;
    protected String taskDescription;
    protected Integer taskId;
    protected Status status = Status.NEW;  // статус прогресса задачи

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(Task task) {
        this(task.taskName, task.taskDescription);
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
        return "model.Task{" +
                "id задачи=" + taskId +
                "Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Статус задачи=" + status +
                '}';
    }
}