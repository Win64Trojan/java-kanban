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

import java.util.Objects;

public class Task {

    protected String taskName;
    protected String taskDescription;
    protected int taskId;
    protected ProgressStatus progressStatus = ProgressStatus.NEW;  // статус прогресса задачи

    public Task(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }

    public Task(String taskName, String taskDescription, ProgressStatus progressStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.progressStatus = progressStatus;
    }

    public Task(String taskName, String taskDescription, int taskId, ProgressStatus progressStatus) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
        this.progressStatus = progressStatus;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public int getTaskId() {
        return taskId;
    }

    public ProgressStatus getProgressStatus() {
        return progressStatus;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setProgressStatus(ProgressStatus progressStatus) {
        this.progressStatus = progressStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId &&
                Objects.equals(taskName, task.taskName) &&
                Objects.equals(taskDescription, task.taskDescription) &&
                progressStatus == task.progressStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, taskId, progressStatus);
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Id задачи=" + taskId +
                ", Статус задачи=" + progressStatus +
                '}';
    }
}