package hundlers;

import com.google.gson.reflect.TypeToken;
import model.Task;
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

public class TaskHandlersTest extends BaseHandlerTest {
    String basePath;

    public TaskHandlersTest() throws IOException {
        this.basePath = "tasks";
    }


    @Test
    void shouldNotAddIfIntersectsWithExisting() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));

        assertDoesNotThrow(() -> {
            Task taskClone = new Task(task);
            manager.createNewTask(taskClone);
        });

        String taskJson = gson.toJson(task);

        URI url = URI.create(String.format("http://localhost:8080/%s", basePath));
        HttpResponse<String> response = sendHttpRequest(url, "POST", taskJson);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturnListFromManager() throws IOException, InterruptedException {
        Task task = new Task("Test 1", "Testing task 1",
                LocalDateTime.now(), Duration.ofMinutes(5));
        Task task2 = new Task("Test 1", "Testing task 1",
                LocalDateTime.now().plus(Duration.ofMinutes(10)), Duration.ofMinutes(5));

        assertDoesNotThrow(() -> {
            manager.createNewTask(task);
            manager.createNewTask(task2);
        });


        URI url = URI.create(String.format("http://localhost:8080/%s", basePath));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertArrayEquals(manager.getListAllTasks().toArray(), tasksFromRequest.toArray(), "Списки задач не совпадают");
    }

    @Test
    public void shouldGetById() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));

        assertDoesNotThrow(() -> {
            manager.createNewTask(task);
        });

        String taskJson = gson.toJson(task);

        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, task.getTaskId()));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        Task taskFromRequest = gson.fromJson(response.body(), Task.class);

        assertEquals(task, taskFromRequest, "Задачи не совпадают");
    }

    @Test
    public void shouldBe404IfRequestedNotExisting() throws IOException, InterruptedException {
        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, 1));
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(404, response.statusCode());
    }


    @Test
    public void shouldDeleteById() throws IOException, InterruptedException {
        Task task = new Task("Test 2", "Testing task 2",
                LocalDateTime.now(), Duration.ofMinutes(5));

        assertDoesNotThrow(() -> {
            manager.createNewTask(task);
        });

        URI url = URI.create(String.format("http://localhost:8080/%s/%d", basePath, 1));
        HttpResponse<String> response = sendHttpRequest(url, "DELETE", "");

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getListAllTasks();

        assertEquals(tasks.size(), 0, "Задачи не удалена");
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {
        // здесь ничего не нужно реализовывать
    }
}