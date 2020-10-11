package nergaltool.setting;

import nergaltool.utils.XmlUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ListSettingTest {
private ListSetting listSetting;
    @BeforeEach
    void setUp() {
        listSetting = new ListSetting("test","","test");
        listSetting.getListValue().add("item1");
        listSetting.getListValue().add("item2");
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
        when(mockTypeNode.getNodeValue()).thenReturn(SettingType.LIST.name());
        NodeList mockNodeList = mock(NodeList.class);
        when(mockNode.getChildNodes()).thenReturn(mockNodeList);
        when(mockNodeList.getLength()).thenReturn(2);
        Node mockItemNode = mock(Node.class);
        when(mockItemNode.getTextContent()).thenReturn("item1");
        Node mockItemTwoNode = mock(Node.class);
        when(mockItemTwoNode.getTextContent()).thenReturn("item2");
        when(mockNodeList.item(0)).thenReturn(mockItemNode);
        when(mockNodeList.item(1)).thenReturn(mockItemTwoNode);


        ListSetting listSetting = new ListSetting(mockNode);
        assertEquals("test", listSetting.getName());
        assertEquals("test", listSetting.getValue());
        assertEquals("content", listSetting.getDescribe());
        assertEquals("item1", listSetting.getListValue().get(0));
        assertEquals("item2", listSetting.getListValue().get(1));
        assertEquals(SettingType.LIST, listSetting.getType());
    }

    @Test
    void testGetXml() throws ParserConfigurationException {
        Element element = listSetting.getXml(XmlUtil.newDocument());
        assertEquals("test", element.getTagName());
        assertEquals("", element.getAttributes().getNamedItem("value").getNodeValue());
        assertEquals("test", element.getAttributes().getNamedItem("describe").getNodeValue());
        assertEquals(SettingType.LIST.name(), element.getAttributes().getNamedItem("type").getNodeValue());
        assertEquals("item1", element.getChildNodes().item(0).getTextContent());
        assertEquals("item2", element.getChildNodes().item(1).getTextContent());
    }
    @Test
    void testInterpreter() {
        assertTrue(listSetting.interpreter("add item3"));
        assertEquals("item1", listSetting.getListValue().get(0));
        assertEquals("item2", listSetting.getListValue().get(1));
        assertEquals("item3", listSetting.getListValue().get(2));
        assertEquals(3, listSetting.getListValue().size());
    }
    @Test
    void testInterpreterRemove() {
        assertTrue(listSetting.interpreter("remove item2"));
        assertEquals("item1", listSetting.getListValue().get(0));
        assertEquals(1, listSetting.getListValue().size());
    }
    @Test
    void testInterpreterFail() {
        assertFalse(listSetting.interpreter("remve item2"));
        assertEquals("item1", listSetting.getListValue().get(0));
        assertEquals("item2", listSetting.getListValue().get(1));
        assertEquals(2, listSetting.getListValue().size());
    }
    @Test
    void testInterpreterFailTwo() {
        assertFalse(listSetting.interpreter("remove"));
        assertEquals("item1", listSetting.getListValue().get(0));
        assertEquals("item2", listSetting.getListValue().get(1));
        assertEquals(2, listSetting.getListValue().size());
    }
}