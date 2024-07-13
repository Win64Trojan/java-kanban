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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserHandlerTest extends BaseHandlerTest {

    @Test
    public void shoulReturnHistory() throws IOException, InterruptedException {


        assertDoesNotThrow(() -> {
            Task task = new Task("Test 2", "Testing task 2",
                    LocalDateTime.now(), Duration.ofMinutes(5));
            Task task2 = new Task("Test 2", "Testing task 2",
                    LocalDateTime.now().plus(Duration.ofMinutes(20)), Duration.ofMinutes(5));
            manager.createNewTask(task);
            manager.createNewTask(task2);

            manager.getTaskById(task.getTaskId());
            manager.getTaskById(task2.getTaskId());
        });


        URI url = URI.create("http://localhost:8080/history");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasksFromRequest, "Задачи не возвращаются");
        assertEquals(2, tasksFromRequest.size(), "Некорректное количество задач в истории");
    }

    @Test
    public void shoulReturnPrioritized() throws IOException, InterruptedException {


        assertDoesNotThrow(() -> {
            Task task2 = new Task("Test 2", "Testing task 2",
                    LocalDateTime.now().plus(Duration.ofMinutes(20)), Duration.ofMinutes(5));
            Task task = new Task("Test 2", "Testing task 2",
                    LocalDateTime.now(), Duration.ofMinutes(5));
            manager.createNewTask(task);
            manager.createNewTask(task2);

            manager.getTaskById(task.getTaskId());
            manager.getTaskById(task2.getTaskId());
        });


        URI url = URI.create("http://localhost:8080/prioritized");
        HttpResponse<String> response = sendHttpRequest(url, "GET", "");

        assertEquals(200, response.statusCode());

        List<Task> tasksFromRequest = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertNotNull(tasksFromRequest, "Задачи не возвращаются");
        assertEquals(2, tasksFromRequest.size(), "Некорректное количество задач в истории");
    }

    class TaskListTypeToken extends TypeToken<List<Task>> {
        // здесь ничего не нужно реализовывать
    }
}