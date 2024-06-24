/**
 * Данный класс наследуется от model.Task и в него добавлено новое поле epicId, благодаря ему подзадачи будет удобно
 * хранить в нужном нам эпике.
 * <p>
 * Также в данном классе переопределен toString
 */
package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task {
    private final Integer epicId;

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask.getTaskName(), subtask.getTaskDescription(), subtask.getStartTime(), subtask.getDuration());
        this.epicId = subtask.epicId;
        this.taskId = subtask.taskId;
        this.status = subtask.status;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String startTimeString = "";
        if (startTime != null) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            startTimeString = startTime.format(dateTimeFormatter);
        }
        return "Subtask{" +
                "id=" + taskId +
                ", epicId=" + epicId +
                ", name='" + taskName + '\'' +
                ", description='" + taskDescription + '\'' +
                ", status=" + status +
                ", start_time=" + startTimeString +
                ", duration=" + duration.toMinutes() + "мин." +
                '}';
    }
}