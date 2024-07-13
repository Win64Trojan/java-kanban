package hundlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Subtask;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubTaskHandlers extends BaseTaskHandlers {

    public SubTaskHandlers(Gson gson, TaskManager taskManager, String basePath) {
        super(gson, taskManager, basePath);
    }

    @Override
    void handleList(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(taskManager.getListAllSubtasks());
        sendText(exchange, 200, responseString);
    }

    @Override
    void handleById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idOpt = getIdFromPath(exchange);

            if (idOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор подзадачи");
                return;
            }

            Task task = taskManager.getSubtaskById(idOpt.get());

            if (task == null) {
                sendText(exchange, 404, String.format("Подзадача с идентификатором " + idOpt.get() + " не найдена"));
                return;
            }

            String responseString = gson.toJson(task);
            sendText(exchange, 200, responseString);
        } catch (Exception e) {
            sendText(exchange, 404, e.getMessage());
        }
    }

    @Override
    void handleCreateOrUpdate(HttpExchange exchange) throws IOException {
        Optional<Subtask> optionalSubTask = parseSubTask(exchange.getRequestBody());
        if (optionalSubTask.isEmpty()) {
            sendText(exchange, 400, "Поля подзадачи не могут быть пустыми");
            return;
        }

        Subtask subTask = optionalSubTask.get();
        try {
            if (subTask.getTaskId() != 0) {
                taskManager.updateSubtask(subTask);
                sendText(exchange, 200, "Подзадача успешно обновлена");
                return;
            }

            taskManager.createNewSubtask(subTask);
            sendText(exchange, 201, "Подзадача успешно создана");
        } catch (Exception e) {
            sendText(exchange, 406, "Подзадача не может быть добавлена, так как не существеут эпика, в который вы ее добавляете");
        }
    }

    @Override
    void handleDelete(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPath(exchange);

        if (idOpt.isEmpty()) {
            sendText(exchange, 400, "Некорректный идентификатор задачи");
            return;
        }

        taskManager.removeSubtaskById(idOpt.get());
        sendText(exchange, 200, "Задача успешно удалена!");
    }

    private Optional<Subtask> parseSubTask(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (!body.contains("name") || !body.contains("description") || !body.contains("status") || !body.contains("startTime") || !body.contains("duration")) {
            return Optional.empty();
        }
        Subtask subTask = gson.fromJson(body, Subtask.class);

        return Optional.of(subTask);
    }

}