package nergaltool.setting.settings;

import nergaltool.setting.base.BaseSetting;
import nergaltool.setting.base.SettingType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ListSetting extends BaseSetting {

    public ListSetting(String name, String value, String describe) {
        super(name, value, describe);
        setType(SettingType.LIST);
    }

    public ListSetting(Node node) {
        super(node);
        NodeList valueNotelist = node.getChildNodes();
        for (int i = 0; i < valueNotelist.getLength(); i++) {
            listValue.add(valueNotelist.item(i).getTextContent());
        }
    }

    @Override
    public boolean interpreter(String data) {
        String[] strings = data.split(" ");
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
        return false;
    }

    @Override
    public Element getXml(Document document) {
        Element root = super.getXml(document);
        for (String name : listValue) {
            Element nameElement = document.createElement("ListItem");
            nameElement.setTextContent(name);
            root.appendChild(nameElement);
        }
        return root;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        for (String name : listValue) {
            stringBuilder.append("\n\t").append(name);
        }
        return stringBuilder.toString();
    }
}
