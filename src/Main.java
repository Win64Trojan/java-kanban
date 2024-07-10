import com.sun.net.httpserver.HttpServer;
import hundlers.HttpTaskServer;
import service.Managers;
import service.TaskManager;

public class Main {

    public static int PORT = 8080;

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(PORT, taskManager);
        httpTaskServer.start();

        System.out.printf("HTTP-сервер запущен на %d порту!\n", PORT);
    }
}
