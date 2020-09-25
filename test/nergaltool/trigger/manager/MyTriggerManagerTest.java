package nergaltool.trigger.manager;

import com.mythicscape.batclient.interfaces.ParsedResult;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MyTriggerManagerTest {
    MyTriggerManager instance = new MyTriggerManager();

    @Test
    void process() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        ParsedResult parsedResult = instance.process(null, new ParsedResult("test"));
        assertEquals("test", parsedResult.getOriginalText());
    }

    @Test
    void processNoAction() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, false, false);
        ParsedResult parsedResult = instance.process(null, new ParsedResult("test"));
        assertNull(parsedResult);
    }

    @Test
    void processNoActionButGag() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, false, true, false);
        ParsedResult parsedResult = instance.process(null, new ParsedResult("test"));
        assertNull(parsedResult);
    }

    @Test
    void processGag() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, true, false);
        ParsedResult parsedResult = instance.process(null, new ParsedResult("test"));
        assertEquals("", parsedResult.getOriginalText());
    }

    @Test
    void processExpand() {
        instance.newTrigger("test", "expandtest", (batClientPlugin, matcher) -> {
        }, true, false, true);
        ParsedResult parsedResult = instance.process(null, new ParsedResult("expandtest", "test", new ArrayList<>()));
        assertEquals("expandtest", parsedResult.getOriginalText());
    }

    @Test
    void getMyTrigger() {
        instance.newTrigger("test", "test", (batClientPlugin, matcher) -> {
        }, true, false, false);
        assertEquals("test", instance.getMyTrigger("test").getName());
        assertNull(instance.getMyTrigger("Notest"));
    }
}