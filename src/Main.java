import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // создаем задачи
        Task task1 = new Task("Взять ложку", "Взять руку, поднести к ложке и схватить её");
        Task task2 = new Task("Насыпать сахар", "Взять пакет, надорвать и высыпать сахар в тару");
        taskManager.createNewTask(task1);
        taskManager.createNewTask(task2);
        // создаем эпики
        Epic epic1 = new Epic("Искать ложку", "открыть глаза и искать");
        Epic epic2 = new Epic("Купить сахар", "Пойти в магазин и купить сахар");
        taskManager.createNewEpic(epic1);
        taskManager.createNewEpic(epic2);
        // создаем подзадачи
        Subtask subtask1 = new Subtask("Подумать, что нужна ложка", "Начать думать", 3);
        Subtask subtask2 = new Subtask("Проснуться", "Открыть глаза и начинать думать", 3);
        Subtask subtask3 = new Subtask("Найти деньги на сахар", "Идти на работу", 4);
        taskManager.createNewSubtask(subtask1);
        taskManager.createNewSubtask(subtask2);
        taskManager.createNewSubtask(subtask3);
        // получаем все задачи, эпики и подзадачи
        System.out.println(taskManager.getListAllTasks());
        System.out.println();
        System.out.println(taskManager.getListAllEpics());
        System.out.println();
        System.out.println(taskManager.getListAllSubtasks());
        System.out.println();
        // Обновляем задачи
        taskManager.updateTheTask(new Task("Закопать картошку", "Взять лопату",
                task1.getTaskId()));
                task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTheEpic(new Epic("Искать клад","Досать карту", epic1.getTaskId(),
                epic1.getStatus(), epic1.getSubtaskIds()));
        taskManager.updateTheSubtask(new Subtask("Перестать думать","Отключиться",
                subtask1.getTaskId(), Status.IN_PROGRESS, 3));
        taskManager.updateTheSubtask(new Subtask("Найти деньги на сахар","Идти на работу",
                subtask3.getTaskId(), Status.DONE, 4));
        System.out.println(taskManager.getListAllTasks());
        System.out.println();
        System.out.println(taskManager.getListAllEpics());
        System.out.println();
        System.out.println(taskManager.getListAllSubtasks());
        System.out.println();
        // удаляем одну задачу и одну подзадачу
        taskManager.removeTaskById(task2.getTaskId());
        taskManager.removeSubtaskById(subtask1.getTaskId());
        System.out.println(taskManager.getListAllTasks());
        System.out.println();
        System.out.println(taskManager.getListAllEpics());
        System.out.println();
        System.out.println(taskManager.getListAllSubtasks());
        System.out.println();
    }
}