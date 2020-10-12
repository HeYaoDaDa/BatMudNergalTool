package nergaltool.setting.base;

import nergaltool.setting.base.BaseSetting;
import nergaltool.setting.base.SettingType;
import nergaltool.setting.settings.BooleanSetting;
import nergaltool.setting.settings.ListSetting;
import nergaltool.setting.settings.NumberSetting;
import nergaltool.setting.settings.StringSetting;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class SettingFactory {
    public static BaseSetting newSetting(String name, String value, String describe, SettingType type) {
        BaseSetting baseSetting = null;
        switch (type) {
            case STRING:
                baseSetting = new StringSetting(name, value, describe);
                break;
            case INT:
                baseSetting = new NumberSetting(name, value, describe);
                break;
            case BOOLEAN:
                baseSetting = new BooleanSetting(name, value, describe);
                break;
            case LIST:
                baseSetting = new ListSetting(name, value, describe);
                break;
        }
        return baseSetting;
    }

    public static BaseSetting newSetting(Node node) {
        NamedNodeMap namedNodeMap = node.getAttributes();
        SettingType type = SettingType.valueOf(namedNodeMap.getNamedItem("type").getNodeValue());
        BaseSetting baseSetting = null;
        switch (type) {
            case STRING:
                baseSetting = new StringSetting(node);
                break;
            case INT:
                baseSetting = new NumberSetting(node);
                break;
            case BOOLEAN:
                baseSetting = new BooleanSetting(node);
                break;
            case LIST:
                baseSetting = new ListSetting(node);
                break;
        }
        return baseSetting;
    }
}
