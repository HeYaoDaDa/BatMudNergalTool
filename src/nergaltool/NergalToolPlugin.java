package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.bean.Play;
import nergaltool.setting.Setting;
import nergaltool.setting.SettingManager;
import nergaltool.setting.SettingType;
import nergaltool.utils.Global;
import nergaltool.utils.TextUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

import static nergaltool.trigger.manager.MyCommandTriggerManager.myCommandTriggerManager;
import static nergaltool.trigger.manager.MyTriggerManager.myTriggerManager;

/**
 * plugin main
 */
public class NergalToolPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {
    private ClientGUI myCLientGUI;
    private final Play play = Play.getInstance();
    private final SettingManager settingManager = SettingManager.getInstance();

    @Override
    public void loadPlugin() {
        try {
            settingManager.read(getBaseDirectory());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        myCLientGUI = getClientGUI();
        myCLientGUI.printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));

        loadTrigger();
        loadCommandTrigger();
    }

    @Override
    public String getName() {
        return Global.PLUGIN_NAME;
    }

    @Override
    public String trigger(String s) {
        return myCommandTriggerManager.process(this, s);
    }

    @Override
    public ParsedResult trigger(ParsedResult parsedResult) {
        return myTriggerManager.process(this, parsedResult);
    }

    @Override
    public void clientExit() {
        try {
            settingManager.save(getBaseDirectory());
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * set Trigger
     */
    private void loadTrigger() {
        myTriggerManager.newTrigger("PlaySc",
                "hp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "sp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "ep: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\]",
                (batClientPlugin, matcher) ->
                        myCLientGUI.printText(Global.GENERIC, matcher.group(1) + "\n")
                , true, false, false);
    }

    private void loadCommandTrigger() {
        myCommandTriggerManager.newTrigger("nergaltoolDebug", "^nergaltool debug$",
                (batClientPlugin, matcher) -> {
                    for (Setting setting : settingManager.getSettingList()) {
                        myCLientGUI.printText(Global.PLUGIN_NAME, setting.toString() + "\n");
                    }
                }, true, true);
    }
}
