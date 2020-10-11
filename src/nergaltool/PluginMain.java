package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.action.atom.InitStatsAction;
import nergaltool.action.base.MyAction;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.MonsterInformation;
import nergaltool.utils.TextUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * plugin main
 */
public class PluginMain extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil, BatClientPluginNetwork {
    public static final String PLUGIN_NAME = "NergalTool";
    public static final String GENERIC = "Generic";

    private final SettingManager settingManager = SettingManager.getInstance();
    private final int upDateSpellCost = 5 * 60 * 1000;

    @Override
    public void loadPlugin() {
        settingManager.init();
        try {
            settingManager.read(getBaseDirectory());
            MonsterInformation.read(getBaseDirectory());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        TriggerInit triggerInit = new TriggerInit(getClientGUI());
        triggerInit.init();
        getClientGUI().printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String trigger(String s) {
        return MyCommandTriggerManager.getInstance().processAllTrigger(this, s);
    }

    @Override
    public ParsedResult trigger(ParsedResult parsedResult) {
        return MyTriggerManager.getInstance().processAllTrigger(this, parsedResult);
    }

    @Override
    public void clientExit() {
        try {
            settingManager.save(getBaseDirectory());
            MonsterInformation.save(getBaseDirectory());
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {
        automupDateSpCost();
    }

    private void automupDateSpCost() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MyAction initAction = new InitStatsAction(getClientGUI());
                initAction.run();
            }
        }, 0, upDateSpellCost);
    }

    @Override
    public void disconnect() {

    }
}
