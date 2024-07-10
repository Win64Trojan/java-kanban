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

public class SubTaskHandlersTest extends BaseHandlerTest {
    String basePath;

    public SubTaskHandlersTest() throws IOException {
        this.basePath = "subtasks";
    }


    @Test
    public void shouldNotAddIfIntersectsWithExisting() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });
        Subtask subTask = new Subtask("Test 2", "Testing subTask 2",
                LocalDateTime.now(), Duration.ofMinutes(5), epic.getTaskId());

        assertDoesNotThrow(() -> {
            Subtask subTaskClone = new Subtask(subTask);
            manager.createNewSubtask(subTaskClone);
        });

        String subTaskJson = gson.toJson(subTask);

        URI url = URI.create(String.format("http://localhost:8080/%s", basePath));
        HttpResponse<String> response = sendHttpRequest(url, "POST", subTaskJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });
        Subtask subTask = new Subtask("Test 1", "Testing subTask 1",
                LocalDateTime.now(), Duration.ofMinutes(5), epic.getTaskId());
        Subtask subTask2 = new Subtask("Test 1", "Testing subTask 1",
                LocalDateTime.now().plus(Duration.ofMinutes(10)), Duration.ofMinutes(5), epic.getTaskId());

        assertDoesNotThrow(() -> {
            manager.createNewSubtask(subTask);
            manager.createNewSubtask(subTask2);
        });


        URI url = URI.create(String.format("http://localhost:8080/%s", basePath));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Subtask> subTasksFromRequest = gson.fromJson(response.body(), new SubTaskListTypeToken().getType());

        assertArrayEquals(manager.getListAllSubtasks().toArray(), subTasksFromRequest.toArray(), "Списки подзадач не совпадают");
    }

    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });
        Subtask subTask = new Subtask("Test 2", "Testing subTask 2",
                LocalDateTime.now(), Duration.ofMinutes(5), epic.getTaskId());

        assertDoesNotThrow(() -> {
            manager.createNewSubtask(subTask);
        });

        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, subTask.getTaskId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        Subtask subTaskFromRequest = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subTask, subTaskFromRequest, "Подзадачи не совпадают");
    }

    @Test
    public void shouldBe404IfRequestedNotExisting() throws IOException, InterruptedException {
        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, 1));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(404, response.statusCode());
    }


    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        Epic epic = new Epic("test", "test");
        assertDoesNotThrow(() -> {
            manager.createNewEpic(epic);
        });
        Subtask subTask = new Subtask("Test 2", "Testing subTask 2",
                LocalDateTime.now(), Duration.ofMinutes(5), epic.getTaskId());

        assertDoesNotThrow(() -> {
            manager.createNewSubtask(subTask);
        });

        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, subTask.getTaskId()));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<Subtask> subTasks = manager.getListAllSubtasks();

        assertEquals(subTasks.size(), 0, "Подзадача не удалена");
    }

    class SubTaskListTypeToken extends TypeToken<List<Subtask>> {
        // здесь ничего не нужно реализовывать
    }
}