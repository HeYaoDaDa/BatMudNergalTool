package nergaltool.setting;

import org.w3c.dom.Node;

public class BooleanSetting extends BaseSetting {
    public BooleanSetting(String name, String value, String describe) {
        super(name, value, describe);
        setType(SettingType.BOOLEAN);
    }

    public BooleanSetting(Node node) {
        super(node);
    }

    @Override
    public boolean interpreter(String data) {
        if ("on".equals(data)||"off".equals(data)){
            setValue(data);
            return true;
        }else {
            return false;
        }
    }
}
