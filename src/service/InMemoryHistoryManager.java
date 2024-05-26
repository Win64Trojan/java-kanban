package service;

import model.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int SIZE_HISTORY = 10;
    private List<Task> history = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(history);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.contains(task)) {
                history.remove(task);
            }
            history.add(task);
            if (history.size() > SIZE_HISTORY) {
                history.removeFirst();
            }
        }
    }
}


