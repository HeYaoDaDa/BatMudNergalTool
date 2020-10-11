package nergaltool.utils;

import nergaltool.setting.StringSetting;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlUtilTest {

    @Test
    void testReadXmlAndSaveXml() throws ParserConfigurationException, TransformerException, IOException, SAXException {
        String path = System.getProperty("user.dir") + File.separator + "testresources" + File.separator + "test.xml";
        Document document = XmlUtil.newDocument();
        StringSetting stringSetting = new StringSetting("name", "content", "test");

        Element rootElement = document.createElement("Setting");
        document.appendChild(rootElement);
        rootElement.appendChild(stringSetting.getXml(document));
        XmlUtil.saveDocumentToFile(path, document);

        Document newDocument = XmlUtil.readFileGetDocument(path);
        Node node = newDocument.getElementsByTagName("Setting").item(0);
        NodeList settingNodeList = node.getChildNodes();
        StringSetting newStringSetting = new StringSetting(settingNodeList.item(0));
        assertEquals(stringSetting.getName(), newStringSetting.getName());
        assertEquals(stringSetting.getValue(), newStringSetting.getValue());
        assertEquals(stringSetting.getDescribe(), newStringSetting.getDescribe());
    }
}