package hundlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

abstract class BaseHttpHandler implements HttpHandler {
    protected final Gson gson;

    protected TaskManager taskManager;

    public BaseHttpHandler(Gson gson) {
        this.gson = gson;
    }

    public BaseHttpHandler(Gson gson, TaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    protected void sendText(HttpExchange h, int responseCode, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(responseCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendStatusOnly(HttpExchange h, int responseCode) throws IOException {
        h.sendResponseHeaders(responseCode, 0);
        h.close();
    }

    protected Optional<Integer> getIdFromPath(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

}