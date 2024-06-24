package model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

class SubtaskTest {

    @Test
    void instancesOfTheSubtaskClassAreEqualToEachOtherById() {
        Subtask subtask1 = new Subtask("", "", LocalDateTime.of(2024, Month.JUNE,
                19, 10, 20), Duration.ofMinutes(1), 1);
        Subtask subtask2 = new Subtask("", "", LocalDateTime.of(2024, Month.JUNE,
                19, 10, 20), Duration.ofMinutes(1), 1);
        subtask1.setTaskId(1);
        subtask2.setTaskId(1);
        Assertions.assertEquals(subtask1, subtask2);
    }


}