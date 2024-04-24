/**
 * Данный класс наследуется от model.Task и в него добавлено новое поле epicId, благодаря ему подзадачи будет удобно
 * хранить в нужном нам эпике.
 * <p>
 * Также в данном классе переопределен toString
 */
package model;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, int taskId, Status status, int epicId) {
        super(taskName, taskDescription, taskId, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    @Override
    public String toString() {
        return "\nmodel.Subtask{" +
                "Id Эпика в котором хранится данная подзадача=" + epicId +
                ", Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Id подзадачи=" + taskId +
                ", Статус подзадачи=" + status +
                '}';
    }
}