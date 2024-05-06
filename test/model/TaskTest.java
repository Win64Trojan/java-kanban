package model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void instancesOfTheTaskClassAreEqualToEachOtherById() {
        Task task1 = new Task("", "");
        Task task2 = new Task("", "");
        task1.setTaskId(1);
        task2.setTaskId(1);
        Assertions.assertEquals(task1, task2);
    }
}