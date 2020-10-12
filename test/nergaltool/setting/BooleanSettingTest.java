package nergaltool.setting;

import nergaltool.setting.settings.BooleanSetting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooleanSettingTest {
    private BooleanSetting booleanSetting;

    @BeforeEach
    void setUp() {
        booleanSetting = new BooleanSetting("test", "on", "test");
    }

    @Test
    void testInterpreter() {
        assertTrue(booleanSetting.interpreter("off"));
        assertEquals("off", booleanSetting.getValue());
    }
    @Test
    void testInterpreterNoChanger() {
        assertTrue(booleanSetting.interpreter("on"));
        assertEquals("on", booleanSetting.getValue());
    }
    @Test
    void testInterpreterFail() {
        assertFalse(booleanSetting.interpreter("onoff"));
        assertEquals("on", booleanSetting.getValue());
    }
    @Test
    void testInterpreterFailTwo() {
        assertFalse(booleanSetting.interpreter("12"));
        assertEquals("on", booleanSetting.getValue());
    }
}