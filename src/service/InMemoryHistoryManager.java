package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> tasks = new HashMap<>();

    private Node<Task> head = null;
    private Node<Task> tail = null;

    private class Node<T> {

        private final T task;
        private Node<T> next;
        private Node<T> prev;

        public Node(Node<T> prev, T task, Node<T> next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }

        public T getTask() {
            return task;
        }

        public Node<T> getNext() {
            return next;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setPrev(Node<T> prev) {
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(task, node.task) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(task, next, prev);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (tasks.get(task.getTaskId()) != null) {
            remove(task.getTaskId());
        }

        if (task instanceof Epic) {
            Epic epicInsert = new Epic((Epic) task);
            linkLast(epicInsert);
            return;
        } else if (task instanceof Subtask) {
            Subtask subtaskInsert = new Subtask((Subtask) task);
            linkLast(subtaskInsert);
            return;
        }
        Task taskInsert = new Task(task);
        linkLast(task);
    }

    @Override
    public void remove(Integer id) {
        removeNode(tasks.get(id));
    }



    public void linkLast(Task task) {
        final Node<Task> newNode = new Node<>(null, task, null);

        if (isEmpty()){
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(head);
        }
        tail = newNode;
        tasks.put(tail.getTask().getTaskId(), tail);
    }

    private boolean isEmpty(){
        return head == null;
    }

    private void removeNode(Node<Task> node) {
        if (node == null) {
            return;
        }

        if (node == head) {
            head = node.getNext();
        } else {
            node.getPrev().setNext(node.getNext());
        }

        if (node == tail) {
            tail = node.getPrev();
        } else  {
            node.getNext().setPrev(node.getPrev());
        }

        tasks.remove(node.getTask().getTaskId());
    }

    private List<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        Node<Task> curNode = head;

        while (curNode != null) {
            tasksList.add(curNode.getTask());
            curNode = curNode.getNext();
        }

        return tasksList;
    }


}



