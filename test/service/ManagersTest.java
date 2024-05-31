package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ManagersTest {

    @Test
    void checkGetDefaultNotNull() {
        Assertions.assertNotNull(Managers.getDefault());
    }

    @Test
    void checkGetDefaultHistoryNotNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory());
    }
}