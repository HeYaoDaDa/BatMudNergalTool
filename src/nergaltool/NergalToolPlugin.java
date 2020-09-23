package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.action.heal.ClwAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.Global;
import nergaltool.utils.TextUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * plugin main
 */
public class NergalToolPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {
    private ClientGUI myCLientGUI;
    private final Play play = Play.getInstance();
    private final List<Minion> minionList = play.getMinionList();
    private final SettingManager settingManager = SettingManager.getInstance();
    private final MyTriggerManager myTriggerManager = MyTriggerManager.getInstance();
    private final MyCommandTriggerManager myCommandTriggerManager = MyCommandTriggerManager.getInstance();

    @Override
    public void loadPlugin() {
        settingManager.init();
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
        myTriggerManager.newTrigger("NergalScoreMINIONS",
                " #\"\"\"\"_MINIONS_ \"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"###\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"######\"\"\" ",
                (batClientPlugin, matcher) ->
                        minionList.clear()
                , true, false, false);
        //nergal score,add minion to minionList
        myTriggerManager.newTrigger("NergalScoreMinion",
                " #\\s+([A-Za-z]+(\\s[a-z]+)*)\\s+" +
                        "HP: ([0-9]+) \\(([0-9]+)\\)\\s+" +
                        "SP: ([0-9]+) \\(([0-9]+)\\)\\s+" +
                        "EP: ([0-9]+) \\(([0-9]+)\\)",
                (batClientPlugin, matcher) ->
                {
                    if (minionList.size() > 0) {//if minion existence
                        for (Minion minion : minionList) {
                            if (minion.getName().equals(matcher.group(1))) {
                                minion.setName(matcher.group(1));
                                minion.setHp(Integer.parseInt(matcher.group(3)));
                                minion.setHpMax(Integer.parseInt(matcher.group(4)));
                                minion.setSp(Integer.parseInt(matcher.group(5)));
                                minion.setSpMax(Integer.parseInt(matcher.group(6)));
                                minion.setEp(Integer.parseInt(matcher.group(7)));
                                minion.setEpMax(Integer.parseInt(matcher.group(8)));
                                return;
                            }
                        }
                    }
                    minionList.add(new Minion(matcher.group(1),
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4)),
                            Integer.parseInt(matcher.group(5)),
                            Integer.parseInt(matcher.group(6)),
                            Integer.parseInt(matcher.group(7)),
                            Integer.parseInt(matcher.group(8))));

                }
                , true, false, false);
        //nergal score,update Vitae
        myTriggerManager.newTrigger("NergalScoreVitae",
                "You have harvested ([0-9]+) vitae to empower you.",
                (batClientPlugin, matcher) ->
                        play.setVitae(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //nergal score,update Pontentia
        myTriggerManager.newTrigger("NergalScorePotentia",
                "You have reaped ([0-9]+) potentia to empower you.",
                (batClientPlugin, matcher) ->
                        play.setPotentia(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //Sc,update paly stats
        myTriggerManager.newTrigger("PlaySc",
                "hp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "sp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "ep: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\]",
                (batClientPlugin, matcher) -> {//beacause have(+/-number) so 3,6,9 no use
                    play.setHp(Integer.parseInt(matcher.group(1)));
                    play.setHpMax(Integer.parseInt(matcher.group(2)));
                    play.setSp(Integer.parseInt(matcher.group(4)));
                    play.setSpMax(Integer.parseInt(matcher.group(5)));
                    play.setEp(Integer.parseInt(matcher.group(7)));
                    play.setEpMax(Integer.parseInt(matcher.group(8)));
                }, true, false, false);
        //nergal sc,update play enemy
        myTriggerManager.newTrigger("NergalScVp",
                "::..:. \\[Vitae: ([0-9]+)/1000  Potentia: ([0-9]+)/1000,",
                (batClientPlugin, matcher) -> {
                    play.setVitae(Integer.parseInt(matcher.group(1)));
                    play.setPotentia(Integer.parseInt(matcher.group(2)));
                    if (Math.max(play.getVitae(), play.getPotentia()) >= 950) {
                        getClientGUI().printText(getName(), TextUtil.colorText("!!!!HAVE A P/V IS FULL!!!!\n", TextUtil.RED));
                    }
                }, true, false, false);
        //nergal sc,update minion food time
        myTriggerManager.newTrigger("NergalScFoodTime",
                "::..:. \\[Invigorated: ([A-Za-z]+(\\s[a-z]+)*) \\(([0-9]+)s\\)," +
                        " ([A-Za-z]+(\\s[a-z]+)*) \\(([0-9]+)s\\)," +
                        " ([A-Za-z]+(\\s[a-z]+)*) \\(([0-9]+)s\\)\\]",
                (batClientPlugin, matcher) -> {
                    for (int i = 1; i < 7; i += 3) {
                        if (matcher.group(i).equals("null")) {
                            break;
                        } else {
                            for (Minion minion : minionList) {
                                if (matcher.group(i).equals(minion.getName())) {
                                    minion.setLastFoodTime(System.currentTimeMillis() - ((180 - Integer.parseInt(matcher.group(i + 2))) * 1000));
                                    return;
                                }
                            }
                        }
                    }
                }, true, false, false);
        //minion XX sc,update minion stats
        myTriggerManager.newTrigger("MinionSc",
                "::..:. ([A-Za-z]+(\\s[a-z]+)*) " +
                        "\\[Hp: ([0-9]+) \\(([0-9]+)\\)( \\(\\S[0-9]+\\))*, " +
                        "Sp: ([0-9]+) \\(([0-9]+)\\)( \\(\\S[0-9]+\\))*, " +
                        "Ep: ([0-9]+) \\(([0-9]+)\\)( \\(\\S[0-9]+\\))*\\]",
                (batClientPlugin, matcher) -> {
                    if (minionList.size() > 0) {//if minion existence
                        for (Minion minion : minionList) {
                            if (minion.getName().equals(matcher.group(1))) {
                                minion.setName(matcher.group(1));
                                minion.setHp(Integer.parseInt(matcher.group(3)));
                                minion.setHpMax(Integer.parseInt(matcher.group(4)));
                                minion.setSp(Integer.parseInt(matcher.group(6)));
                                minion.setSpMax(Integer.parseInt(matcher.group(7)));
                                minion.setEp(Integer.parseInt(matcher.group(9)));
                                minion.setEpMax(Integer.parseInt(matcher.group(10)));
                                return;
                            }
                        }
                    }
                    minionList.add(new Minion(matcher.group(1),
                            Integer.parseInt(matcher.group(3)),
                            Integer.parseInt(matcher.group(4)),
                            Integer.parseInt(matcher.group(6)),
                            Integer.parseInt(matcher.group(7)),
                            Integer.parseInt(matcher.group(9)),
                            Integer.parseInt(matcher.group(10))));
                }, true, false, false);
        //hibernating end,update play last hibernating time
        myTriggerManager.newTrigger("HibernatingEnd",
                "^As the roots subside releasing you from their embrace you feel invigorated.",
                (batClientPlugin, matcher) ->
                        play.setLastHibernatingTime(System.currentTimeMillis()), true, false, false);
        //sleep awaken,update play last sleep time
        myTriggerManager.newTrigger("SleepEnd",
                "^You awaken from your short rest, and feel slightly better.",
                (batClientPlugin, matcher) ->
                        play.setLastSleepTime(System.currentTimeMillis()), true, false, false);
        //food succes,update minion food time
        myTriggerManager.newTrigger("FoodSuccess",
                "You raise your hand towards ([A-Za-z]+(\\s[a-z]+)*) and faint line of ethereal energy",
                (batClientPlugin, matcher) -> {
                    for (Minion minion : minionList) {
                        if (matcher.group(1).equals(minion.getName())) {
                            minion.setLastFoodTime(System.currentTimeMillis());
                        }
                    }
                }, true, false, false);
    }

    private void loadCommandTrigger() {
        //debug info
        myCommandTriggerManager.newTrigger("nergaltoolDebug", "^nergaltool debug ([a-z]+)$",
                (batClientPlugin, matcher) -> {
                    switch (matcher.group(1)) {
                        case "setting":
                            myCLientGUI.printText(Global.PLUGIN_NAME, settingManager.toString() + "\n");
                            break;
                        case "minion":
                            myCLientGUI.printText(Global.PLUGIN_NAME, minionList.toString() + "\n");
                            break;
                        case "play":
                            myCLientGUI.printText(Global.PLUGIN_NAME, play.toString() + "\n");
                            break;
                    }
                }, true, true);
        //clw all minions
        myCommandTriggerManager.newTrigger("nergaltoolClw", "^nergaltool clw$",
                (batClientPlugin, matcher) -> {
                    myCLientGUI.printText(getName(), "clw start\n");
                    ClwAction clwAction = new ClwAction(myCLientGUI);
                    clwAction.run();
                    myCLientGUI.printText(getName(), "clw end\n");
                }, true, true);
    }

}
