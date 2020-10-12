package nergaltool.trigger.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MyCommandTriggerManagerTest {
    MyCommandTriggerManager instance = new MyCommandTriggerManager();

    @Test
    void testProcessAllTrigger() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        String result = instance.processAllTrigger(null, "test");
        assertEquals("test", result);
    }

    @Test
    void testProcessAllTriggerNoAction() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, false, false);
        String result = instance.processAllTrigger(null, "test");
        assertNull(result);
    }

    @Test
    void testProcessAllTriggerNoActionButGag() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, true, false);
        String result = instance.processAllTrigger(null, "test");
        assertNull(result);
    }

    @Test
    void testProcessAllTriggerGag() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, true, false);
        String result = instance.processAllTrigger(null, "test");
        assertEquals("", result);
    }

    @Test
    void testFindTriggerByName() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        assertEquals("test", instance.findTriggerByName("test").getName());
        assertNull(instance.findTriggerByName("Notest"));
    }
}