package nergaltool.setting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberSettingTest {
    private NumberSetting numberSetting;
    @BeforeEach
    void setUp() {
        numberSetting = new NumberSetting("test","0","test");
    }

    @Test
    void testInterpreter() {
        assertTrue(numberSetting.interpreter("101"));
        assertEquals("101", numberSetting.getValue());
    }
    @Test
    void testInterpreterNoChanger() {
        assertTrue(numberSetting.interpreter("0"));
        assertEquals("0", numberSetting.getValue());
    }
    @Test
    void testInterpreterFail() {
        assertFalse(numberSetting.interpreter("-0"));
        assertEquals("0", numberSetting.getValue());
    }
    @Test
    void testInterpreterFailTwo() {
        assertFalse(numberSetting.interpreter("string"));
        assertEquals("0", numberSetting.getValue());
    }
    @Test
    void testInterpreterFailThree() {
        assertFalse(numberSetting.interpreter("0.0"));
        assertEquals("0", numberSetting.getValue());
    }
}