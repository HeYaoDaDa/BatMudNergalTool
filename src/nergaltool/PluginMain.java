package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.bean.Room;
import nergaltool.setting.SettingManager;
import nergaltool.spell.SpellManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.MonsterManager;
import nergaltool.utils.TextUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class PluginMain extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil, BatClientPluginNetwork {
    public static final String PLUGIN_NAME = "NergalTool";
    public static final String GENERIC = "Generic";
    public final Room room = new Room();

    private final SettingManager settingManager = SettingManager.getInstance();
    private final MonsterManager monsterManager = MonsterManager.getInstance();
    private final Timer automUpdateSpCostTimer = new Timer();
    private final SpellManager spellManager = SpellManager.getInstance();

    @Override
    public void loadPlugin() {
        settingManager.init();
        useXmlUpdate();
        MyTriggerManager.getInstance().init(this);
        MyCommandTriggerManager.getInstance().init(this);
        spellManager.init();
        getClientGUI().printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));
    }

    private void useXmlUpdate() {
        try {
            settingManager.read(getBaseDirectory());
            monsterManager.read(getBaseDirectory());
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
            monsterManager.save(getBaseDirectory());
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
                spellManager.initSpCost(getClientGUI());
            }
        }, 0, upDateSpellCost);
    }

    @Override
    public void disconnect() {
        automUpdateSpCostTimer.cancel();
    }
}
