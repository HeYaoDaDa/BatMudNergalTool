package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.action.*;
import nergaltool.action.base.InitStatsAction;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.Global;
import nergaltool.utils.MonsterInformation;
import nergaltool.utils.SpellUtil;
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
public class NergalToolPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {
    private ClientGUI myCLientGUI;
    private final Play play = Play.getInstance();
    private final List<Minion> minionList = play.getMinionList();
    private final SettingManager settingManager = SettingManager.getInstance();
    private final MyTriggerManager myTriggerManager = MyTriggerManager.getInstance();
    private final MyCommandTriggerManager myCommandTriggerManager = MyCommandTriggerManager.getInstance();
    private Timer combatTimer;
    private boolean needinit;
    private final List<String> mobs = new ArrayList<>();

    @Override
    public void loadPlugin() {
        settingManager.init();
        try {
            settingManager.read(getBaseDirectory());
            MonsterInformation.read(getBaseDirectory());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        new Timer().schedule(new TimerTask() {//each 5 minute
            @Override
            public void run() {
                needinit = true;
            }
        },0,5*60*1000);
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
            MonsterInformation.save(getBaseDirectory());
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
        //Combat and Scan is a pair
        myTriggerManager.newTrigger("Combat",
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
                        }, 1000, 2500);
                    }
                }, true, false, false);
        //Combat end
        myTriggerManager.newTrigger("Scan",
                "You are not in combat right now.",
                (batClientPlugin, matcher) -> {
                    if (play.isCombat()) {
                        play.setCombat(false);
                        combatTimer.cancel();
                        combatEnd();
                    }
                }, true, false, false);//Gag one scan
        //move to new room
        myTriggerManager.newTrigger("NewRoom", "^(Obvious exits are)|^(Obvious exit is)|(Exits?:  )",
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
        myTriggerManager.newTrigger("RoomMonster",
                "^\u001B\\[1;32m([A-Za-z,'\\s-]+)\u001B\\[0m$",
                (batClientPlugin, matcher) -> mobs.add(matcher.group(1)), true, false, true);
    }

    /**
     * set command trigger
     */
    private void loadCommandTrigger() {
        //debug info
        myCommandTriggerManager.newTrigger("nergaltoolDebug", "^nergaltool debug ([a-z]+)$",
                (batClientPlugin, matcher) -> {
                    switch (matcher.group(1)) {
                        case "minion":
                            StringBuilder minionInfo = new StringBuilder("Minions: ");
                            for (Minion name : minionList) {
                                minionInfo.append("\n");
                                minionInfo.append(name.toString());
                            }
                            myCLientGUI.printText(Global.PLUGIN_NAME, minionInfo.toString() + "\n");
                            break;
                        case "play":
                            myCLientGUI.printText(Global.PLUGIN_NAME, play.toString() + "\n");
                            break;
                    }
                }, true, true,false);
        //reply
        myCommandTriggerManager.newTrigger("nergaltoolReply", "^nergaltool reply$",
                (batClientPlugin, matcher) -> reply(), true, true,false);
        //set show
        myCommandTriggerManager.newTrigger("nergaltoolSet", "^nergaltool set ?([a-zA-Z]+)? ?([a-zA-Z0-9\\s]+)?",
                (batClientPlugin, matcher) -> settingManager.interpreter(myCLientGUI, matcher), true, true,false);
        //show all monster
        myCommandTriggerManager.newTrigger("nergaltoolMonster", "^nergaltool monster",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(myCLientGUI, matcher), true, true,false);
        //remove monster index
        myCommandTriggerManager.newTrigger("nergaltoolMonsterRemove", "^nergaltool monster remove ([0-9]+)",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(myCLientGUI, matcher), true, true,false);
        //harvest
        myCommandTriggerManager.newTrigger("nergaltoolharv", "^nergaltool harvest$",
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
                        getClientGUI().doCommand("@bell " + settingManager.getSetting("playName").getValue());
                        getClientGUI().printText(Global.GENERIC, TextUtil.colorText("no harvest here\n", TextUtil.RED));
                    }
                }, true, true,false);
    }

    /**
     * reply
     */
    private void reply() {
        MyAction start = new ReplyAction(myCLientGUI);
        MyAction init = new MyAction(myCLientGUI);
        if (SpellUtil.foodSp == 0 || needinit) {
            SpellUtil.hvSp = SpellUtil.rpSp = SpellUtil.foodSp = SpellUtil.clwSp = 0;
            needinit =false;
            init = new InitStatsAction(myCLientGUI);
        }
        MyAction food = new FoodAction(myCLientGUI);
        MyAction clw = new ClwAction(myCLientGUI);
        MyAction foodPotentia = new FoodPotentiaAction(myCLientGUI);
        MyAction spr = new SprAction(myCLientGUI, Math.max(SpellUtil.hvSp, SpellUtil.rpSp));
        MyAction bell = new BellAction(myCLientGUI);

        start.decorate(init);
        init.decorate(food);
        food.decorate(clw);
        clw.decorate(foodPotentia);
        foodPotentia.decorate(spr);
        spr.decorate(bell);


        start.run();
    }

    /**
     * combat end
     */
    private void combatEnd() {
        boolean needHeal = false;
        int maxSp = Math.max(SpellUtil.hvSp, SpellUtil.rpSp);
        for (Minion minion : minionList) {
            if (minion.getHp() <= minion.getHpMax() * Integer.parseInt(settingManager.getSetting("battleEndStartHealHpRate").getValue()) * 0.01) {
                needHeal = true;
                break;
            }
        }
        if (needHeal && Boolean.parseBoolean(settingManager.getSetting("battleEndHeal").getValue())) {
            reply();
        } else {
            if (play.getSp() < maxSp) {
                MyAction spr = new SprAction(myCLientGUI, maxSp);
                MyAction bell = new BellAction(myCLientGUI);
                spr.decorate(bell);
                spr.run();
            } else {
                new BellAction(myCLientGUI).run();
            }
        }
    }

}
