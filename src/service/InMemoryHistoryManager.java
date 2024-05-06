package service;

import model.Task;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_HISTORY = 10;
    private List<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return copyViewHistory();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            history.add(new Task(task));
            if (history.size() > SIZE_HISTORY) {
                history.removeFirst();
            }
        }
    }

    private List<Task> copyViewHistory() {
        List<Task> list = new ArrayList<>();

        for (Task task : history) {
            list.add(new Task(task));
        }

        return list;
    }
}

