/**
 * Данный класс наследуется от Task и в него добавлено новое поле epicId, благодаря ему подзадачи будет удобно
 * хранить в нужном нам эпике.
 * <p>
 * Также в данном классе переопределен toString
 */

public class Subtask extends Task {
    private int epicId;

    public Subtask(String taskName, String taskDescription, int epicId) {
        super(taskName, taskDescription);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, int taskId, ProgressStatus progressStatus, int epicId) {
        super(taskName, taskDescription, taskId, progressStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "\nSubtask{" +
                "Id Эпика в котором хранится данная подзадача=" + epicId +
                ", Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Id подзадачи=" + taskId +
                ", Статус подзадачи=" + progressStatus +
                '}';
    }
}