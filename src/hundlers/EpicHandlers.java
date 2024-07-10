package hundlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandlers extends BaseTaskHandlers {

    public EpicHandlers(Gson gson, TaskManager taskManager, String basePath) {
        super(gson, taskManager, basePath);
    }

    @Override
    void handleList(HttpExchange exchange) throws IOException {
        String responseString = gson.toJson(taskManager.getListAllEpics());
        sendText(exchange, 200, responseString);
    }

    @Override
    void handleById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idOpt = getIdFromPath(exchange);

            if (idOpt.isEmpty()) {
                sendText(exchange, 400, "Некорректный идентификатор эпика");
                return;
            }

            Task task = taskManager.getEpicById(idOpt.get());

            if (task == null) {
                sendText(exchange, 404, String.format("Эпик с идентификатором " + idOpt.get() + " не найден"));
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
        Optional<Epic> optionalEpic = parseEpic(exchange.getRequestBody());
        if (optionalEpic.isEmpty()) {
            sendText(exchange, 400, "Поля эпика не могут быть пустыми");
            return;
        }

        Epic epic = optionalEpic.get();

        if (epic.getTaskId() > 0) {
            taskManager.updateEpic(epic);
            sendText(exchange, 200, "Эпик успешно обновлен");
            return;
        }

        taskManager.createNewEpic(epic);

        sendText(exchange, 201, "Задача успешно создана");
    }

    @Override
    void handleDelete(HttpExchange exchange) throws IOException {
        Optional<Integer> idOpt = getIdFromPath(exchange);

        if (idOpt.isEmpty()) {
            sendText(exchange, 400, "Некорректный идентификатор эпика");
            return;
        }

        taskManager.removeEpicById(idOpt.get());
        sendText(exchange, 200, "Эпик успешно удален!");
    }

    @Override
    protected void handleEpicSubtasks(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> idOpt = getIdFromPath(exchange);
            if (idOpt.isEmpty()) {
                sendText(exchange, 400, "не указа id эпика");
                return;
            }

            Integer epicId = idOpt.get();


            String responseString = gson.toJson(taskManager.getAllSubtasksOfEpic(taskManager.getEpicById(epicId)));
            sendText(exchange, 200, responseString);
        } catch (Exception e) {
            sendText(exchange, 404, e.getMessage());
        }
    }


    private Optional<Epic> parseEpic(InputStream bodyInputStream) throws IOException {
        String body = new String(bodyInputStream.readAllBytes(), StandardCharsets.UTF_8);
        if (!body.contains("name") || !body.contains("description")) {
            return Optional.empty();
        }

        Epic epic = gson.fromJson(body, Epic.class);


        return Optional.of(epic);
    }

}