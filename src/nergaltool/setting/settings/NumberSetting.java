package nergaltool.setting.settings;

import nergaltool.setting.base.BaseSetting;
import nergaltool.setting.base.SettingType;
import org.w3c.dom.Node;

import java.util.regex.Pattern;

public class NumberSetting extends BaseSetting {
    public NumberSetting(String name, String value, String describe) {
        super(name, value, describe);
        setType(SettingType.INT);
    }

    public NumberSetting(Node node) {
        super(node);
    }

    @Override
    public boolean interpreter(String data) {
        Pattern pattern = Pattern.compile("^[\\d]*$");
        if (pattern.matcher(data).matches()) {
            setValue(data);
            return true;
        } else {
            return false;
        }
    }
}
