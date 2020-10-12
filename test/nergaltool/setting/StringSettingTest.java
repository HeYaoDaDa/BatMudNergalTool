package nergaltool.setting;

import nergaltool.setting.base.SettingType;
import nergaltool.setting.settings.StringSetting;
import nergaltool.utils.XmlUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StringSettingTest {
    private StringSetting stringSetting;

    @BeforeEach
    void setUp() {
        stringSetting = new StringSetting("test", "content", "test");
    }

    @Test
    void testConstructorNode() {
        Node mockNode = mock(Node.class);
        NamedNodeMap mockNamedNodeMap = mock(NamedNodeMap.class);
        when(mockNode.getAttributes()).thenReturn(mockNamedNodeMap);
        when(mockNode.getNodeName()).thenReturn("test");
        Node mockValueNode = mock(Node.class);
        when(mockNamedNodeMap.getNamedItem("value")).thenReturn(mockValueNode);
        when(mockValueNode.getNodeValue()).thenReturn("test");
        Node mockDescribeNode = mock(Node.class);
        when(mockNamedNodeMap.getNamedItem("describe")).thenReturn(mockDescribeNode);
        when(mockDescribeNode.getNodeValue()).thenReturn("content");
        Node mockTypeNode = mock(Node.class);
        when(mockNamedNodeMap.getNamedItem("type")).thenReturn(mockTypeNode);
        when(mockTypeNode.getNodeValue()).thenReturn(SettingType.STRING.name());

        StringSetting stringSetting = new StringSetting(mockNode);
        assertEquals("test", stringSetting.getName());
        assertEquals("test", stringSetting.getValue());
        assertEquals("content", stringSetting.getDescribe());
        assertEquals(SettingType.STRING, stringSetting.getType());
    }

    @Test
    void testInterpreter() {
        stringSetting.interpreter("changer");
        assertEquals("changer", stringSetting.getValue());
    }

    @Test
    void testGetXml() throws ParserConfigurationException {
        Element element = stringSetting.getXml(XmlUtil.newDocument());
        assertEquals("test", element.getTagName());
        assertEquals("content", element.getAttributes().getNamedItem("value").getNodeValue());
        assertEquals("test", element.getAttributes().getNamedItem("describe").getNodeValue());
        assertEquals(SettingType.STRING.name(), element.getAttributes().getNamedItem("type").getNodeValue());
    }
}