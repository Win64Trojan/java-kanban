package hundlers;

import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicHandlersTest extends BaseHandlerTest {
    String basePath;

    public EpicHandlersTest() throws IOException {
        this.basePath = "epics";
    }


    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing epic 1");
        Epic epic2 = new Epic("Test 1", "Testing epic 1");

        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
            manager.createNewEpic(epic2);
        });


        URI url = URI.create(String.format("http://localhost:8080/%s", basePath));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Epic> epicsFromRequest = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertArrayEquals(manager.getListAllEpics().toArray(), epicsFromRequest.toArray(), "Списки эпиков не совпадают");
    }

    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2");

        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });


        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, epic.getTaskId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        Epic epicFromRequest = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic, epicFromRequest, "Эпики не совпадают");
    }

    @Test
    public void shouldBe404IfRequestedNotExisting() throws IOException, InterruptedException {
        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, 1));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(404, response.statusCode());
    }


    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 2", "Testing epic 2");

        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });

        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, 1));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<Epic> epics = manager.getListAllEpics();

        assertEquals(epics.size(), 0, "Задачи не удалена");
    }

    @Test
    void shouldReturnSubTasksListOfEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Test 1", "Testing epic 1");
        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);

            Subtask subTask = new Subtask("Test 1", "Testing subTask 1",
                    LocalDateTime.now(), Duration.ofMinutes(5), epic.getTaskId());
            Subtask subTask2 = new Subtask("Test 1", "Testing subTask 1",
                    LocalDateTime.now().plus(Duration.ofMinutes(10)), Duration.ofMinutes(5), epic.getTaskId());

            manager.createNewSubtask(subTask);
            manager.createNewSubtask(subTask2);
        });


        URI url = URI.create(String.format("http://localhost:8080/%s/%d/subtasks", basePath, epic.getTaskId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Epic> subTasksFromRequest = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());

        assertArrayEquals(manager.getListAllSubtasks().toArray(), subTasksFromRequest.toArray(), "Списки подзадача не совпадают");
    }

    class EpicListTypeToken extends TypeToken<List<Epic>> {
        // здесь ничего не нужно реализовывать
    }

    class SubTaskListTypeToken extends TypeToken<List<Subtask>> {
        // здесь ничего не нужно реализовывать
    }
}