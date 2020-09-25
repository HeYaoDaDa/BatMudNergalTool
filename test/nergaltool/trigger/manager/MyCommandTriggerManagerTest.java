package nergaltool.trigger.manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MyCommandTriggerManagerTest {
    MyCommandTriggerManager instance = new MyCommandTriggerManager();

    @Test
    void process() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        String result = instance.process(null, "test");
        assertEquals("test", result);
    }

    @Test
    void processNoAction() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, false, false);
        String result = instance.process(null, "test");
        assertNull(result);
    }

    @Test
    void processNoActionButGag() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, true, false);
        String result = instance.process(null, "test");
        assertNull(result);
    }

    @Test
    void processGag() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, true, false);
        String result = instance.process(null, "test");
        assertEquals("", result);
    }

    @Test
    void getMyTrigger() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        assertEquals("test", instance.getMyTrigger("test").getName());
        assertNull(instance.getMyTrigger("Notest"));
    }
}