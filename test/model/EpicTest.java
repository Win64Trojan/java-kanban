package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    void instancesOfTheEpicClassAreEqualToEachOtherById() {
        Epic epic1 = new Epic("", "");
        Epic epic2 = new Epic("", "");
        epic1.setTaskId(1);
        epic2.setTaskId(1);
        Assertions.assertEquals(epic1, epic2);
    }
}
