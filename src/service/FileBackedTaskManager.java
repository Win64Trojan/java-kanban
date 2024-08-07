package service;

import exeptions.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static model.TaskType.SUBTASK;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final File outPutFile;

    private static final String title = "id,type,name,status,description,epic,start_time,duration";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    FileBackedTaskManager(File outPutFile) {
        this.outPutFile = outPutFile;
        save();
    }

    public static void main(String[] args) {
        try {
            File fileSave = new File("saveTasks.csv");

            FileBackedTaskManager taskManager1 = new FileBackedTaskManager(fileSave);
            Task task = new Task("Купить кувшин", "Найти хороший магазин кувшинов", LocalDateTime.of(2024, Month.JUNE, 21, 0, 0), Duration.ofMinutes(10));
            taskManager1.createNewTask(task);

            Task task2 = new Task("Купить машину", "Найти хорошего консультанта", LocalDateTime.of(2024, Month.JUNE, 21, 0, 0), Duration.ofMinutes(10));
            taskManager1.createNewTask(task2);

            Epic epic = new Epic("Праздник", "Найти прадздничные шарики");
            taskManager1.createNewEpic(epic);

            Subtask subTask = new Subtask("Лотерея", "Купить лотерейный билет", LocalDateTime.of(2024, Month.JUNE, 23, 10, 20), Duration.ofMinutes(1), epic.getTaskId());
            taskManager1.createNewSubtask(subTask);

            Subtask subTask2 = new Subtask("Ботинки", "Почистить ботинки", LocalDateTime.of(2024, Month.JUNE, 23, 10, 30), Duration.ofMinutes(1), epic.getTaskId());
            taskManager1.createNewSubtask(subTask2);


            FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(fileSave);

            if (taskManager1.getListAllTasks().size() != taskManager2.getListAllTasks().size()) {
                System.out.println("Размер задач не равны");
            }

            if (taskManager1.getListAllSubtasks().size() != taskManager2.getListAllSubtasks().size()) {
                System.out.println("Размер подзадач не равны");
            }

            if (taskManager1.getListAllEpics().size() != taskManager2.getListAllEpics().size()) {
                System.out.println("Размер Эпика не равны");
            }
            System.out.println(taskManager1.getPrioritizedTasks());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Task createNewTask(Task newTask) {
        if (newTask == null) {
            return null;
        }
        Task task = super.createNewTask(newTask);
        save();
        return task;

    }

    @Override
    public Epic createNewEpic(Epic newEpic) {
        if (newEpic == null) {
            return null;
        }
        Epic epic = super.createNewEpic(newEpic);
        save();
        return epic;

    }

    @Override
    public Subtask createNewSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return null;
        }
        Subtask subtask = super.createNewSubtask(newSubtask);
        save();
        return subtask;

    }

    @Override
    public List<Task> getListAllTasks() {
        save();
        return super.getListAllTasks();

    }

    @Override
    public List<Epic> getListAllEpics() {
        save();
        return super.getListAllEpics();

    }

    @Override
    public List<Subtask> getListAllSubtasks() {
        save();
        return super.getListAllSubtasks();

    }

    @Override
    public Task getTaskById(Integer id) {
        save();
        return super.getTaskById(id);


    }

    @Override
    public Epic getEpicById(Integer id) {
        save();
        return super.getEpicById(id);

    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;

    }

    @Override
    public List<Subtask> getAllSubtasksOfEpic(Epic epic) {
        save();
        return super.getAllSubtasksOfEpic(epic);

    }

    @Override
    public void removeTaskById(Integer taskId) {
        super.removeTaskById(taskId);
        save();
    }

    @Override
    public void removeEpicById(Integer epicId) {
        super.removeEpicById(epicId);
        save();
    }

    @Override
    public void removeSubtaskById(Integer subtaskIdForRemove) {
        super.removeSubtaskById(subtaskIdForRemove);
        save();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();

    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public Task updateTask(Task updateTask) {
        save();
        return super.updateTask(updateTask);

    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        save();
        return super.updateEpic(updateEpic);
    }

    @Override
    public Subtask updateSubtask(Subtask updateSubtask) {
        save();
        return super.updateSubtask(updateSubtask);

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try {
            String[] lines = Files.readString(file.toPath()).split("\n");
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

            List<Task> tasks = Arrays.stream(lines)
                    .skip(1)
                    .map(line -> Optional.ofNullable(convertStringToTask(line)))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();


            for (Task task : tasks) {
                TaskType taskType = toEnum(task);
                switch (taskType) {
                    case TASK -> fileBackedTaskManager.createNewTask(task);

                    case EPIC -> fileBackedTaskManager.createNewEpic((Epic) task);

                    case SUBTASK -> fileBackedTaskManager.createNewSubtask((Subtask) task);

                    default -> throw new IllegalStateException("Неверное значение: " + taskType);
                }
            }

            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outPutFile.toURI()), StandardCharsets.UTF_8)) {
            writer.write(title);

            for (Task task : tasks.values()) {
                writer.write(String.format("\n%s", convertTaskToString(task)));
            }

            for (Epic epic : epics.values()) {
                writer.write(String.format("\n%s", convertTaskToString(epic)));
            }

            for (Subtask subTask : subtasks.values()) {
                writer.write(String.format("\n%s", convertTaskToString(subTask)));
            }

        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private static Task convertStringToTask(String value) {
        String[] items = value.split(",");

        TaskType taskType = TaskType.valueOf(items[1]);
        int id = Integer.parseInt(items[0]);
        String name = items[2];
        String description = items[4];
        Status status = stringToStatus(items[3]);

        LocalDateTime startDateTime = null;
        if (!items[6].isBlank()) {
            startDateTime = LocalDateTime.parse(items[6], DATE_TIME_FORMATTER);
        }

        Duration duration = Duration.ofMinutes(Integer.parseInt(items[7]));


        switch (taskType) {

            case TASK -> {
                Task task = new Task(name, description, startDateTime, duration);
                task.setTaskId(id);
                return task;
            }

            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setStatus(status);
                epic.setTaskId(id);
                return epic;
            }

            case SUBTASK -> {
                int epicId = Integer.parseInt(items[5]);
                Subtask subTask = new Subtask(name, description, startDateTime, duration, epicId);
                subTask.setTaskId(id);
                return subTask;
            }

            default -> {
                return null;
            }
        }
    }

    private String convertTaskToString(Task task) {
        TaskType taskType = toEnum(task);
        switch (taskType) {

            case EPIC, TASK -> {

                return String.format("%d,%s,%s,%s,%s,,%s,%d", task.getTaskId(), taskType, task.getTaskName(),
                        task.getStatus(), task.getTaskDescription(),
                        task.getStartTime() != null ? task.getStartTime().format(DATE_TIME_FORMATTER) : "",
                        task.getDuration() != null ? task.getDuration().toMinutes() : 0);
            }


            case SUBTASK -> {
                Subtask subTask = (Subtask) task;
                return String.format("%d,%s,%s,%s,%s,%d,%s,%d", subTask.getTaskId(), taskType, subTask.getTaskName(),
                        subTask.getStatus(), subTask.getTaskDescription(), subTask.getEpicId(),
                        subTask.getStartTime().format(DATE_TIME_FORMATTER), subTask.getDuration().toMinutes());


            }
        }

        throw new IllegalStateException("Неверное значение: " + taskType);
    }

    private static TaskType toEnum(Task task) {
        if (task instanceof Subtask) {
            return SUBTASK;
        } else if (task instanceof Epic) {
            return TaskType.EPIC;
        }

        return TaskType.TASK;
    }

    private static Status stringToStatus(String input) {
        Status status = Status.valueOf(input);

        switch (status) {
            case NEW, DONE, IN_PROGRESS -> {
                return status;
            }

            default -> {
                throw new IllegalStateException("Неверное значение: " + status);
            }
        }
    }
}