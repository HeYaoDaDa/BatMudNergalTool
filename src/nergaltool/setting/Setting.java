package nergaltool.setting;

import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * setting
 */
public class Setting {
    private String name;
    private String value;
    private String describe;
    private SettingType type;
    private final List<String> listValue = new ArrayList<>();//if type is list

    public Setting(String name, String value, String describe, SettingType type) {
        this.name = name;
        this.value = value;
        this.describe = describe;
        this.type = type;
    }

    public Setting(Node node) {
        NamedNodeMap namedNodeMap = node.getAttributes();
        this.name = node.getNodeName();
        this.value = namedNodeMap.getNamedItem("value").getNodeValue();
        this.describe = namedNodeMap.getNamedItem("describe").getNodeValue();
        this.type = SettingType.valueOf(namedNodeMap.getNamedItem("type").getNodeValue());
        if (type == SettingType.LIST) {//if is list then fill listValue
            NodeList valueNotelist = node.getChildNodes();
            for (int i = 0; i < valueNotelist.getLength(); i++) {
                listValue.add(valueNotelist.item(i).getTextContent());
            }
        }
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
        String[] strings = data.split(" ");
        switch (type) {
            case STRING:
                value = data;
                break;
            case INT:
                Pattern pattern = Pattern.compile("^[\\d]*$");
                if (pattern.matcher(data).matches()) {
                    value = data;
                    return true;
                } else {
                    return false;
                }
            case BOOLEAN:
                if (strings.length < 2) {
                    return false;
                }
                if ("on".equals(strings[0])) {
                    value = "on";
                    return true;
                } else if ("off".equals(strings[0])) {
                    value = "off";
                    return true;
                }
                break;
            case LIST:
                if (strings.length < 2) {
                    return false;
                }
                String target = data.substring(data.indexOf(" ") + 1);
                if ("add".equals(strings[0])) {
                    listValue.add(target);
                    return true;
                } else if ("remove".equals(strings[0])) {
                    listValue.remove(target);
                    return true;
                }
                break;
        }
        return false;
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
        if (type == SettingType.LIST) {
            for (String name : listValue) {
                Element nameElement = document.createElement("ListItem");
                nameElement.setTextContent(name);
                root.appendChild(nameElement);
            }
        }
        return root;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", describe='" + describe + '\'' +
                ", type=" + type +
                ", listValue=" + listValue +
                '}';
    }
}
