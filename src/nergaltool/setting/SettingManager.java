package nergaltool.setting;

import nergaltool.utils.Global;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

/**
 * setting manager
 */
public class SettingManager {
    //Singleton,instance
    public static SettingManager settingManager = new SettingManager();
    private final List<Setting> settingList = new ArrayList<>();

    private final String pluginDir = File.separator + "conf" + File.separator + Global.PLUGIN_NAME;
    private final String settingXmlFile = pluginDir + File.separator + "Setting.xml";

    //Singleton,private constructor
    private SettingManager() {
    }

    //Singleton,get single instance
    public static SettingManager getInstance() {
        return settingManager;
    }

    /**
     * add setting to list
     *
     * @param name     name
     * @param value    value
     * @param describe describe
     * @param type     type
     */
    public void newSetting(String name, String value, String describe, SettingType type) {
        newSetting(new Setting(name, value, describe, type));
    }

    /**
     * add setting to list
     *
     * @param setting new setting
     */
    public void newSetting(Setting setting) {
        //find setting is existence
        Setting oldSetting = getSetting(setting.getName());
        if (oldSetting != null) {
            oldSetting.setName(setting.getName());
            oldSetting.setValue(setting.getValue());
            oldSetting.setDescribe(setting.getDescribe());
            oldSetting.setType(setting.getType());
        } else {
            settingList.add(setting);
        }
    }

    /**
     * use name find setting on list
     *
     * @param name setting name
     * @return setting object
     */
    public Setting getSetting(String name) {
        for (Setting setting : settingList) {
            if (setting.getName().equals(name)) {
                return setting;
            }
        }
        return null;
    }

    public List<Setting> getSettingList() {
        return settingList;
    }

    /**
     * save setting to xml
     *
     * @param basepath batclient file path
     */
    public void save(String basepath) throws ParserConfigurationException, TransformerException {
        //if dir is no exists then mkdir
        File dir = new File(basepath + pluginDir);
        if (!dir.exists()) {
            if (!dir.mkdir()) {//mkdir fail then return
                return;
            }
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.newDocument();

        Element rootElement = document.createElement("Setting");
        document.appendChild(rootElement);

        for (Setting setting : settingList) {
            rootElement.appendChild(setting.getXml(document));
        }

        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
        tf.transform(new DOMSource(document), new StreamResult(new File(basepath + settingXmlFile)));
    }

    /**
     * read xml file
     *
     * @param basepath batclient file path
     * @throws ParserConfigurationException error
     * @throws IOException                  error
     * @throws SAXException                 error
     */
    public void read(String basepath) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(basepath + settingXmlFile);
        if (!file.exists()) {//if no xml file then exit
            return;
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = factory.newDocumentBuilder();
        Document doc = db.parse(basepath + settingXmlFile);

        Node rootElement = doc.getElementsByTagName("Setting").item(0);
        NodeList settingNodeList = rootElement.getChildNodes();
        for (int i = 0; i < settingNodeList.getLength(); i++) {
            newSetting(new Setting(settingNodeList.item(i)));
        }
    }

    public void init() {
        newSetting("playName", "NOSET", "@bell need use", SettingType.STRING);
//        newSetting("triggerDebug","on","trigger match print trigger name",SettingType.BOOLEAN);
        newSetting("battleEndClw", "on", "battle end use clw heal minions", SettingType.BOOLEAN);
        newSetting("battleEndFood", "on", "battle end use food vitae heal minions", SettingType.BOOLEAN);
        newSetting("battleEndWaitSpr", "on", "battle end wait sp to spell cost", SettingType.BOOLEAN);
        newSetting("foodPotentia", "on", "food potentia to target minion", SettingType.BOOLEAN);

        newSetting("battleEndStartHealHpRate", "60", "set battle end < X% hp start heal(clw and food)", SettingType.INT);
        newSetting("clwEndHpLoss", "40", "set hp>=hpmax-X end clw", SettingType.INT);
        newSetting("clwBlackList", "", "set clw blacklist(battleEnd and command)", SettingType.LIST);

        newSetting("foodHpLoss", "200", "set hp<=hpmax-X start food", SettingType.INT);
        newSetting("foodMaxSize", "50", "set food vitae max size", SettingType.INT);
        newSetting("eachVitaeHpr", "8", "set each vitae hp", SettingType.INT);
        newSetting("foodBlackList", "", "set food blacklist(battleEnd and command)", SettingType.LIST);

        newSetting("foodPotentiaTraget", "minion", "food potentia to target minion", SettingType.STRING);
    }

    @Override
    public String toString() {
        return "SettingManager{" +
                "settingList=" + settingList +
                '}';
    }
}
