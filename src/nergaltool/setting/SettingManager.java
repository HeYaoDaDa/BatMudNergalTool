package nergaltool.setting;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import nergaltool.setting.base.BaseSetting;
import nergaltool.setting.base.SettingFactory;
import nergaltool.setting.base.SettingType;
import nergaltool.utils.TextUtil;
import nergaltool.utils.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * setting manager
 */
public class SettingManager {
    //Singleton,instance
    public static SettingManager settingManager = new SettingManager();
    private final List<BaseSetting> baseSettingList = new ArrayList<>();

    private final String pluginDir = File.separator + "conf" + File.separator + PluginMain.PLUGIN_NAME;
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
    public void appendSetting(String name, String value, String describe, SettingType type) {
        appendSetting(SettingFactory.newSetting(name, value, describe, type));
    }

    public void appendSetting(Node node) {
        appendSetting(SettingFactory.newSetting(node));
    }

    /**
     * add setting to list
     *
     * @param baseSetting new setting
     */
    public void appendSetting(BaseSetting baseSetting) {
        //find setting is existence
        BaseSetting oldBaseSetting = findSettingByName(baseSetting.getName());
        if (oldBaseSetting != null) {
            oldBaseSetting.setName(baseSetting.getName());
            oldBaseSetting.setValue(baseSetting.getValue());
            oldBaseSetting.setDescribe(baseSetting.getDescribe());
            oldBaseSetting.setType(baseSetting.getType());
        } else {
            baseSettingList.add(baseSetting);
        }
    }

    /**
     * use name find setting on list
     *
     * @param name setting name
     * @return setting object
     */
    public BaseSetting findSettingByName(String name) {
        for (BaseSetting baseSetting : baseSettingList) {
            if (baseSetting.getName().equals(name)) {
                return baseSetting;
            }
        }
        return null;
    }

    public List<BaseSetting> getBaseSettingList() {
        return baseSettingList;
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

        Document document = XmlUtil.newDocument();

        Element rootElement = document.createElement("Setting");
        document.appendChild(rootElement);

        for (BaseSetting baseSetting : baseSettingList) {
            rootElement.appendChild(baseSetting.getXml(document));
        }

        XmlUtil.saveDocumentToFile(basepath + settingXmlFile, document);
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
        Document doc = XmlUtil.readFileGetDocument(basepath + settingXmlFile);

        Node rootElement = doc.getElementsByTagName("Setting").item(0);
        NodeList settingNodeList = rootElement.getChildNodes();
        for (int i = 0; i < settingNodeList.getLength(); i++) {
            appendSetting(settingNodeList.item(i));
        }
    }

    public void init() {
        appendSetting("playName", "NOSET", "@bell need use", SettingType.STRING);
//        newSetting("triggerDebug","on","trigger match print trigger name",SettingType.BOOLEAN);
        appendSetting("battleEndHeal", "true", "battle end use reply", SettingType.BOOLEAN);

        appendSetting("battleEndStartHealHpRate", "60", "set battle end < X% hp start heal(clw and food)", SettingType.INT);
        appendSetting("clwEndHpLoss", "40", "set hp>=hpmax-X end clw", SettingType.INT);
        appendSetting("clwBlackList", "", "set clw blacklist(battleEnd and command)", SettingType.LIST);

        appendSetting("foodHpLoss", "200", "set hp<=hpmax-X start food", SettingType.INT);
        appendSetting("foodMaxSize", "50", "set food vitae max size", SettingType.INT);
        appendSetting("eachVitaeHpr", "8", "set each vitae hp", SettingType.INT);
        appendSetting("foodBlackList", "", "set food blacklist(battleEnd and command)", SettingType.LIST);

        appendSetting("foodPotentia", "true", "food potentia to target minion", SettingType.BOOLEAN);
        appendSetting("foodPotentiaTraget", "minion", "food potentia to target minion", SettingType.STRING);
        appendSetting("foodPotentiaSize", "800", "have XX potentia food potentia", SettingType.INT);
    }

    /**
     * match show set and set value
     *
     * @param clientGUI context
     * @param matcher   matcher
     */
    public void interpreter(ClientGUI clientGUI, Matcher matcher) {
        if (matcher.group(1) == null) {//show set
            clientGUI.printText(PluginMain.GENERIC, settingManager.toString());
        } else {
            for (BaseSetting baseSetting : baseSettingList) {
                if (baseSetting.getName().equals(matcher.group(1))) {
                    if (!baseSetting.interpreter(matcher.group(2))) {//if fail print info
                        clientGUI.printText(PluginMain.GENERIC, TextUtil.colorText("Set fail\n", TextUtil.RED));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("--------------Setting--------------\n");
        for (BaseSetting baseSetting : baseSettingList) {
            s.append(baseSetting.toString()).append("\n");
        }
        s.append("--------------Setting End----------\n");
        return s.toString();
    }
}
