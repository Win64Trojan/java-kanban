import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    private static final TaskManager manager = Managers.getDefault();


    public static void main(String[] args) {
//        System.out.println("* Создание объектов *");
//
//        Task task1 = manager.createNewTask(new Task("Task1", "Description"));
//        Task task2 = manager.createNewTask(new Task("Task2", "Description"));
//
//        Epic epic1 = manager.createNewEpic(new Epic("Epic1", "Description"));
//        Epic epic2 = manager.createNewEpic(new Epic("Epic2", "Description"));
//
//        Subtask subtask1 = manager.createNewSubtask(new Subtask("Subtask1",
//                "Description", epic1.getTaskId()));
//        Subtask subtask2 = manager.createNewSubtask(new Subtask("Subtask2",
//                "Description", epic1.getTaskId()));
//
//        Subtask subtask3 = manager.createNewSubtask(new Subtask("Subtask",
//                "Description", epic2.getTaskId()));
//
//        printTasks();
//
//
//        System.out.println("* Изменение статусов *");
//
//        task1.setStatus(Status.IN_PROGRESS);
//        manager.updateTask(task1);
//        task2.setStatus(Status.DONE);
//        manager.updateTask(task2);
//
//        subtask1.setStatus(Status.DONE);
//        manager.updateSubtask(subtask1);
//        subtask2.setStatus(Status.NEW);
//        manager.updateSubtask(subtask2);
//        subtask3.setStatus(Status.DONE);
//        manager.updateSubtask(subtask3);
//
//        printTasks();
//        // ********************************************
//        System.out.println("* Удаление задач *");
//
//        manager.removeEpicById(epic1.getTaskId());
//
//        manager.getTaskById(task1.getTaskId());
//        manager.getTaskById(task2.getTaskId());
//
//        printTasks();
//    }
//
//    private static void printTasks() {
//        System.out.println("Задачи:");
//        for (Task task : manager.getListAllTasks()) {
//            System.out.println(task);
//        }
//        System.out.println("Эпики:");
//        for (Epic epic : manager.getListAllEpics()) {
//            System.out.println(epic);
//
//            for (Subtask task : epic.getSubtasks()) {
//                System.out.println("--> " + task);
//            }
//        }
//        System.out.println("Подзадачи:");
//        for (Subtask subtask : manager.getListAllSubtasks()) {
//            System.out.println(subtask);
//        }
//
//
//        Task task3 = manager.createNewTask(new Task("Task3", "Description"));
//        manager.getTaskById(8);
//        Task task4 = manager.createNewTask(new Task("Task4", "Description"));
//        manager.getTaskById(9);
//
//        manager.removeTaskById(9);
//        System.out.println("История:");
//        for (Task task : manager.getHistory()) {
//            System.out.println(task);
//        }
//
//
//
//
//
//    }
    }
}