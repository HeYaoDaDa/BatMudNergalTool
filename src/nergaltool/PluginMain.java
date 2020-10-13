package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.setting.SettingManager;
import nergaltool.spell.SpellMananger;
import nergaltool.trigger.TriggerInit;
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
    private final Timer automUpdateSpCostTimer = new Timer();

    @Override
    public void loadPlugin() {
        settingManager.init();
        useXmlUpdate();
        new TriggerInit(getClientGUI()).init();
        getClientGUI().printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));
    }

    private void useXmlUpdate() {
        try {
            settingManager.read(getBaseDirectory());
            MonsterInformation.read(getBaseDirectory());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
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
        saveDateToXml();
    }

    private void saveDateToXml() {
        try {
            settingManager.save(getBaseDirectory());
            MonsterInformation.save(getBaseDirectory());
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {
        automUpdateSpCost();
    }

    private void automUpdateSpCost() {
        final int upDateSpellCost = 5 * 60 * 1000;
        automUpdateSpCostTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SpellMananger.getInstance().initSpCost(getClientGUI());
            }
        }, 0, upDateSpellCost);
    }

    @Override
    public void disconnect() {
        automUpdateSpCostTimer.cancel();
    }
}
