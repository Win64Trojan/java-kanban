/**
 * Данный класс наследуется от model.Task и в него добавлено новое поле, а именно лист, для хранения индексов подзадач.
 * Благодаря ему мы сможем получить все подзадачи в определенном эпике.
 * <p>
 * Также в данном классе переопределен toString
 */
package model;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    //для хранения индексов сабтаска
    protected ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic(String taskName, String taskDescription) {
        super(taskName, taskDescription);
    }

    public Epic(String taskName, String taskDescription, ArrayList<Integer> subtaskIds) {
        super(taskName, taskDescription);
        this.subtaskIds = subtaskIds;
    }

    public Epic(String taskName, String taskDescription, int taskId, Status status, ArrayList<Integer> subtaskIds) {
        super(taskName, taskDescription, taskId, status);
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {
        return "\nmodel.Epic{" +
                "Id подзадач в данном эпике=" + subtaskIds +
                ", Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Id эпика=" + taskId +
                ", Статус эпика=" + status +
                '}';

    }
}