package nergaltool.utils;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MonsterManager {
    private static final MonsterManager monsterManager = new MonsterManager();
    public static List<String> monsterList = new ArrayList<>();
    private static final String monsterInfoXmlFile = File.separator + "conf" + File.separator + PluginMain.PLUGIN_NAME + File.separator + "monsters.xml";

    public static MonsterManager getInstance() {
        return monsterManager;
    }

    public void save(String basePath) throws ParserConfigurationException, TransformerException {
        Document document = XmlUtil.newDocument();
        saveMobsterToDocument(document);
        XmlUtil.saveDocumentToFile(basePath + monsterInfoXmlFile, document);
    }

    private void saveMobsterToDocument(Document document) {
        Element rootElement = document.createElement("MonsterInfo");
        for (String name : monsterList) {
            Element element = document.createElement("name");
            element.setTextContent(name);
            rootElement.appendChild(element);
        }
    }

    public void read(String basePath) throws ParserConfigurationException, IOException, SAXException {
        String path = basePath + monsterInfoXmlFile;
        if (!MyFileUtil.isExists(path)) {
            return;
        }
        readMobsterFromDocument(path);
    }

    private void readMobsterFromDocument(String path) throws IOException, SAXException, ParserConfigurationException {
        Document doc = XmlUtil.readFileGetDocument(path);
        NodeList mobsNodeList = doc.getElementsByTagName("MonsterInfo");
        NodeList mobs = mobsNodeList.item(0).getChildNodes();
        for (int i = 0; i < mobs.getLength(); i++) {
            monsterList.add(mobs.item(i).getTextContent());
        }
    }

    public String printMonsters() {
        StringBuilder s = new StringBuilder("-------------Monsters--------------\n");
        for (int i = 0; i < monsterList.size(); i++) {
            s.append(i).append(": ").append(monsterList.get(i)).append("\n");
        }
        s.append("-------------Monsters End----------\n");
        return s.toString();
    }

    public void interpreter(ClientGUI clientGUI) {
        clientGUI.printText(PluginMain.GENERIC, printMonsters());
    }
}
