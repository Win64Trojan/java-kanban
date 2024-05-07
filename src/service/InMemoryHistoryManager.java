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
            Iterator<Task> iterator = history.iterator();
            while (iterator.hasNext()){
                Task existingTask = iterator.next();
                if (existingTask.equals(task)){
                    // Удаляем существующую задачу из истории
                    iterator.remove();
                }
            }
            history.add(task);
            if (history.size() > SIZE_HISTORY) {
                history.removeFirst();
            }
        }
    }
}

