package nergaltool.setting.base;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * setting
 */
public abstract class BaseSetting {
    private String name;
    private String value;
    private String describe;
    private SettingType type;
    protected List<String> listValue = new ArrayList<>();

    public BaseSetting(String name, String value, String describe) {
        this.name = name;
        this.value = value;
        this.describe = describe;
    }

    public BaseSetting(Node node) {
        NamedNodeMap namedNodeMap = node.getAttributes();
        this.name = node.getNodeName();
        this.value = namedNodeMap.getNamedItem("value").getNodeValue();
        this.describe = namedNodeMap.getNamedItem("describe").getNodeValue();
        this.type = SettingType.valueOf(namedNodeMap.getNamedItem("type").getNodeValue());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public SettingType getType() {
        return type;
    }

    public void setType(SettingType type) {
        this.type = type;
    }

    public List<String> getListValue() {
        return listValue;
    }

    /**
     * use commandline update setting
     *
     * @param data command
     * @return success or fail
     */
    public boolean interpreter(String data) {
        value = data;
        return true;
    }

    /**
     * return xml element
     *
     * @param document doc
     * @return element
     */
    public Element getXml(Document document) {
        Element root = document.createElement(name);
        root.setAttribute("value", value);
        root.setAttribute("describe", describe);
        root.setAttribute("type", type.name());
        return root;
    }

    @Override
    public String toString() {
        return "BaseSetting{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", describe='" + describe + '\'' +
                ", type=" + type +
                ", listValue=" + listValue +
                '}';
    }
}
