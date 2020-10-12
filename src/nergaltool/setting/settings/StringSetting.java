package nergaltool.setting.settings;

import nergaltool.setting.base.BaseSetting;
import nergaltool.setting.base.SettingType;
import org.w3c.dom.Node;

public class StringSetting extends BaseSetting {
    public StringSetting(String name, String value, String describe) {
        super(name, value, describe);
        setType(SettingType.STRING);
    }

    public StringSetting(Node node) {
        super(node);
    }
}
