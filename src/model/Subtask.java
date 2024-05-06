/**
 * Данный класс наследуется от model.Task и в него добавлено новое поле epicId, благодаря ему подзадачи будет удобно
 * хранить в нужном нам эпике.
 * <p>
 * Также в данном классе переопределен toString
 */
package model;

import java.util.Objects;

public class Subtask extends Task {
    private final Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(description, name);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + taskName + '\'' +
                ", description='" + taskDescription + '\'' +
                ", id=" + taskId +
                ", status=" + status +
                '}';
    }
}