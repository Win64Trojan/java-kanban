package model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubtaskTest {

    @Test
    void instancesOfTheSubtaskClassAreEqualToEachOtherById() {
        Subtask subtask1 = new Subtask("", "", 0);
        Subtask subtask2 = new Subtask("", "", 0);
        subtask1.setTaskId(1);
        subtask2.setTaskId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }


}