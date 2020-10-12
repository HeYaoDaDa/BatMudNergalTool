package nergaltool.trigger.manager;

import com.mythicscape.batclient.interfaces.ParsedResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MyTriggerManagerTest {
    MyTriggerManager instance = new MyTriggerManager();

    @Test
    void testProcessAllTrigger() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        ParsedResult parsedResult = instance.processAllTrigger(null, new ParsedResult("test"));
        assertEquals("test", parsedResult.getOriginalText());
    }

    @Test
    void testProcessAllTriggerNoAction() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, false, false);
        ParsedResult parsedResult = instance.processAllTrigger(null, new ParsedResult("test"));
        assertNull(parsedResult);
    }

    @Test
    void testProcessAllTriggerNoActionButGag() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, true, false);
        ParsedResult parsedResult = instance.processAllTrigger(null, new ParsedResult("test"));
        assertNull(parsedResult);
    }

    @Test
    void testProcessAllTriggerGag() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, true, false);
        ParsedResult parsedResult = instance.processAllTrigger(null, new ParsedResult("test"));
        assertEquals("", parsedResult.getOriginalText());
    }

    @Test
    void testProcessAllTriggerExpand() {
        instance.appendTrigger("test", "expandtest", (batClientPlugin, matcher) -> {
        }, true, false, true);
        ParsedResult parsedResult = instance.processAllTrigger(null, new ParsedResult("expandtest", "test", new ArrayList<>()));
        assertEquals("expandtest", parsedResult.getOriginalText());
    }

    @Test
    void testFindTriggerByName() {
        instance.appendTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        assertEquals("test", instance.findTriggerByName("test").getName());
        assertNull(instance.findTriggerByName("Notest"));
    }
}