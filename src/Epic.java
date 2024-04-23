/**
 * Данный класс наследуется от Task и в него добавлено новое поле, а именно лист, для хранения индексов подзадач.
 * Благодаря ему мы сможем получить все подзадачи в определенном эпике.
 * <p>
 * Также в данном классе переопределен toString
 */

import java.util.ArrayList;

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

    public Epic(String taskName, String taskDescription, int taskId, ProgressStatus progressStatus, ArrayList<Integer> subtaskIds) {
        super(taskName, taskDescription, taskId, progressStatus);
        this.subtaskIds = subtaskIds;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "\nEpic{" +
                "Id подзадач в данном эпике=" + subtaskIds +
                ", Название задачи='" + taskName + '\'' +
                ", Описание задачи='" + taskDescription + '\'' +
                ", Id эпика=" + taskId +
                ", Статус эпика=" + progressStatus +
                '}';

    }
}