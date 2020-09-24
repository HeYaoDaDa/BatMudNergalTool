package nergaltool.utils;

import com.mythicscape.batclient.interfaces.ClientGUI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * autom harvest monster information manager
 */
public class MonsterInformation {
    public static List<String> monsterList = new ArrayList<>();
    private static final String monsterInfoXmlFile = File.separator + "conf" + File.separator + Global.PLUGIN_NAME+File.separator+"monsters.xml";

    /**
     * save to xml
     * @param basePath base path
     * @throws ParserConfigurationException error
     * @throws TransformerException error
     */
    public static void save(String basePath) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();

        Element rootElement = document.createElement("MonsterInfo");
        for (String name : monsterList) {
            Element element = document.createElement("name");
            element.setTextContent(name);
            rootElement.appendChild(element);
        }
        document.appendChild(rootElement);
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
        tf.transform(new DOMSource(document), new StreamResult(new File(basePath+monsterInfoXmlFile)));
    }

    /**
     * read xml
     * @param basePath base path
     * @throws ParserConfigurationException error
     * @throws IOException error
     * @throws SAXException error
     */
    public static void read(String basePath) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(basePath + monsterInfoXmlFile);
        if (!file.exists()) {//if no xml file then exit
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(basePath +monsterInfoXmlFile);
        NodeList mobsNodeList = doc.getElementsByTagName("MonsterInfo");
        NodeList mobs = mobsNodeList.item(0).getChildNodes();
        for (int i=0;i<mobs.getLength();i++){
            monsterList.add(mobs.item(i).getTextContent());
        }
    }

    /**
     * show monster list
     * @return monster list
     */
    public static String printMonsters(){
        StringBuilder s = new StringBuilder("-------------Monsters--------------\n");
        for (int i = 0;i < monsterList.size();i++){
            s.append(i).append(": ").append(monsterList.get(i)).append("\n");
        }
        s.append("-------------Monsters End----------\n");
        return s.toString();
    }

    public static void interpreter(ClientGUI clientGUI, Matcher matcher){
//        if (matcher.group(1)==null){
            clientGUI.printText(Global.GENERIC,printMonsters());
//        }else {
//            Pattern pattern = Pattern.compile("^[\\d]*$");
//            if (pattern.matcher(matcher.group(1)).matches()) {
//                monsterList.remove(Integer.parseInt(matcher.group(1)));
//                clientGUI.printText(Global.GENERIC,printMonsters());
//            } else {
//                clientGUI.printText(Global.GENERIC,"Fail\n");
//            }
//        }
    }
}
