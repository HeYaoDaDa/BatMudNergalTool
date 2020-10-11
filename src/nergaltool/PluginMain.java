package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.action.CombatAction;
import nergaltool.action.ReplyAction;
import nergaltool.action.atom.HarvestAction;
import nergaltool.action.atom.InitStatsAction;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.MonsterInformation;
import nergaltool.utils.TextUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * plugin main
 */
public class PluginMain extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil, BatClientPluginNetwork {
    public static final String PLUGIN_NAME = "NergalTool";
    public static final String GENERIC = "Generic";

    private ClientGUI myCLientGUI;
    private final Play play = Play.getInstance();
    private final List<Minion> minionList = play.getMinionList();
    private final SettingManager settingManager = SettingManager.getInstance();
    private final MyTriggerManager myTriggerManager = MyTriggerManager.getInstance();
    private final MyCommandTriggerManager myCommandTriggerManager = MyCommandTriggerManager.getInstance();
    private Timer combatTimer;
    private final List<String> mobs = new ArrayList<>();
    private final int automFoodPotentiaSize = 940;
    private final int upDateSpellCost = 5 * 60 * 1000;

    @Override
    public void loadPlugin() {
        settingManager.init();
        myCLientGUI = getClientGUI();
        try {
            settingManager.read(getBaseDirectory());
            MonsterInformation.read(getBaseDirectory());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        loadTrigger();
        loadCommandTrigger();
        myCLientGUI.printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));
    }

    @Override
    public String getName() {
        return PLUGIN_NAME;
    }

    @Override
    public String trigger(String s) {
        return myCommandTriggerManager.processAllTrigger(this, s);
    }

    @Override
    public ParsedResult trigger(ParsedResult parsedResult) {
        return myTriggerManager.processAllTrigger(this, parsedResult);
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

    /**
     * set Trigger
     */
    private void loadTrigger() {
        myTriggerManager.addTrigger("NergalScoreMINIONS",
                " #\"\"\"\"_MINIONS_ \"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"###\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"######\"\"\" ",
                (batClientPlugin, matcher) ->
                        minionList.clear()
                , true, false, false);
        //nergal score,add minion to minionList
        myTriggerManager.addTrigger("NergalScoreMinion",
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
        myTriggerManager.addTrigger("NergalScoreVitae",
                "You have harvested ([0-9]+) vitae to empower you.",
                (batClientPlugin, matcher) ->
                        play.setVitae(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //nergal score,update Pontentia
        myTriggerManager.addTrigger("NergalScorePotentia",
                "You have reaped ([0-9]+) potentia to empower you.",
                (batClientPlugin, matcher) ->
                        play.setPotentia(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //Sc,update paly stats
        myTriggerManager.addTrigger("PlaySc",
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
        myTriggerManager.addTrigger("NergalScVp",
                "::..:. \\[Vitae: ([0-9]+)/1000  Potentia: ([0-9]+)/1000,",
                (batClientPlugin, matcher) -> {
                    play.setVitae(Integer.parseInt(matcher.group(1)));
                    play.setPotentia(Integer.parseInt(matcher.group(2)));
                    if (Math.max(play.getVitae(), play.getPotentia()) >= automFoodPotentiaSize) {
                        getClientGUI().printText(getName(), TextUtil.colorText("!!!!HAVE A P/V IS FULL!!!!\n", TextUtil.RED));
                    }
                }, true, false, false);
        //nergal sc,update minion food time
        myTriggerManager.addTrigger("NergalScFoodTime",
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
        myTriggerManager.addTrigger("MinionSc",
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
        myTriggerManager.addTrigger("HibernatingEnd",
                "^As the roots subside releasing you from their embrace you feel invigorated.",
                (batClientPlugin, matcher) ->
                        play.setLastHibernatingTime(System.currentTimeMillis()), true, false, false);
        //sleep awaken,update play last sleep time
        myTriggerManager.addTrigger("SleepEnd",
                "^You awaken from your short rest, and feel slightly better.",
                (batClientPlugin, matcher) ->
                        play.setLastSleepTime(System.currentTimeMillis()), true, false, false);
        //food succes,update minion food time
        myTriggerManager.addTrigger("FoodSuccess",
                "You raise your hand towards ([A-Za-z]+(\\s[a-z]+)*) and faint line of ethereal energy",
                (batClientPlugin, matcher) -> {
                    for (Minion minion : minionList) {
                        if (matcher.group(1).equals(minion.getName())) {
                            minion.setLastFoodTime(System.currentTimeMillis());
                        }
                    }
                }, true, false, false);
        //Combat and Scan is a pair
        myTriggerManager.addTrigger("Combat",
                "\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\* Round",
                (batClientPlugin, matcher) -> {
                    if (!play.isCombat()) {
                        play.setCombat(true);
                        combatTimer = new Timer();
                        combatTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getClientGUI().doCommand("@scan all");
                            }
                        }, 1, 2500);
                    }
                }, true, false, false);
        //Combat end
        myTriggerManager.addTrigger("Scan",
                "You are not in combat right now.",
                (batClientPlugin, matcher) -> {
                    if (play.isCombat()) {
                        play.setCombat(false);
                        combatTimer.cancel();
                        combatEnd();
                    }
                }, true, false, false);//Gag one scan
        //move to new room
        myTriggerManager.addTrigger("NewRoom", "^(Obvious exits are)|^(Obvious exit is)|(Exits?:  )",
                (batClientPlugin, matcher) -> {
                    mobs.clear();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            myCLientGUI.printText(getName(), "The room have :\n");
                            for (String s : mobs) {
                                getClientGUI().printText(getName(), "    " + s + "\n");
                            }
                        }
                    }, 50);
                }, true, false, false);
        //room monster,color code
        myTriggerManager.addTrigger("RoomMonster",
                "^\u001B\\[1;32m([A-Za-z,'\\s-]+)\u001B\\[0m$",
                (batClientPlugin, matcher) -> mobs.add(matcher.group(1)), true, false, true);
    }

    /**
     * set command trigger
     */
    private void loadCommandTrigger() {
        //debug info
        myCommandTriggerManager.addTrigger("nergaltoolDebug", "^nergaltool debug ([a-z]+)$",
                (batClientPlugin, matcher) -> {
                    switch (matcher.group(1)) {
                        case "minion":
                            StringBuilder minionInfo = new StringBuilder("Minions: ");
                            for (Minion name : minionList) {
                                minionInfo.append("\n");
                                minionInfo.append(name.toString());
                            }
                            myCLientGUI.printText(PLUGIN_NAME, minionInfo.toString() + "\n");
                            break;
                        case "play":
                            myCLientGUI.printText(PLUGIN_NAME, play.toString() + "\n");
                            break;
                    }
                }, true, true, false);
        //reply
        myCommandTriggerManager.addTrigger("nergaltoolReply", "^nergaltool reply$",
                (batClientPlugin, matcher) -> reply(), true, true, false);
        //set show
        myCommandTriggerManager.addTrigger("nergaltoolSet", "^nergaltool set ?([a-zA-Z]+)? ?([a-zA-Z0-9\\s]+)?",
                (batClientPlugin, matcher) -> settingManager.interpreter(myCLientGUI, matcher), true, true, false);
        //show all monster
        myCommandTriggerManager.addTrigger("nergaltoolMonster", "^nergaltool monster",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(myCLientGUI, matcher), true, true, false);
        //remove monster index
        myCommandTriggerManager.addTrigger("nergaltoolMonsterRemove", "^nergaltool monster remove ([0-9]+)",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(myCLientGUI, matcher), true, true, false);
        //harvest
        myCommandTriggerManager.addTrigger("nergaltoolharv", "^nergaltool harvest$",
                (batClientPlugin, matcher) -> {
                    if (mobs.size() >= 1) {
                        boolean isTwo = false;
                        String monsterName = mobs.get(0);
                        for (String s : MonsterInformation.monsterList) {
                            if (monsterName.equals(s)) {
                                isTwo = true;
                                break;
                            }
                        }
                        if (!isTwo)
                            MonsterInformation.monsterList.add(monsterName);
                        MyAction myAction = new HarvestAction(myCLientGUI, monsterName);
                        myAction.run();
                    } else {
                        getClientGUI().doCommand("@bell " + settingManager.findSettingByName("playName").getValue());
                        getClientGUI().printText(GENERIC, TextUtil.colorText("no harvest here\n", TextUtil.RED));
                    }
                }, true, true, false);
    }

    /**
     * reply
     */
    private void reply() {
        MyAction combatAction = new CombatAction(myCLientGUI, play);
        combatAction.run();
    }

    /**
     * combat end
     */
    private void combatEnd() {
        MyAction replyAction = new ReplyAction(myCLientGUI);
        replyAction.run();
    }

    @Override
    public void connect() {
        automupDateSpCost();
    }

    private void automupDateSpCost() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                MyAction initAction = new InitStatsAction(myCLientGUI);
                initAction.run();
            }
        }, 0, upDateSpellCost);
    }

    @Override
    public void disconnect() {

    }
}
