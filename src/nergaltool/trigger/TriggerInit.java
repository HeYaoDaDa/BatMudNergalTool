package nergaltool.trigger;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import nergaltool.action.CombatAction;
import nergaltool.action.ReplyAction;
import nergaltool.action.atom.HarvestAction;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.MonsterInformation;
import nergaltool.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TriggerInit {
    private final Play play = Play.getInstance();
    private final List<Minion> minionList = play.getMinionList();
    private final SettingManager settingManager = SettingManager.getInstance();
    private final MyTriggerManager myTriggerManager = MyTriggerManager.getInstance();
    private final MyCommandTriggerManager myCommandTriggerManager = MyCommandTriggerManager.getInstance();
    private final List<String> mobs = new ArrayList<>();
    private final int automFoodPotentiaSize = 940;
    private final ClientGUI clientGUI;
    private Timer combatTimer;

    public TriggerInit(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
    }
    public void init(){
        loadTrigger();
        loadCommandTrigger();
    }
    /**
     * set Trigger
     */
    private void loadTrigger() {
        myTriggerManager.appendTrigger("NergalScoreMINIONS",
                " #\"\"\"\"_MINIONS_ \"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"###\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"######\"\"\" ",
                (batClientPlugin, matcher) ->
                        minionList.clear()
                , true, false, false);
        //nergal score,add minion to minionList
        myTriggerManager.appendTrigger("NergalScoreMinion",
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
        myTriggerManager.appendTrigger("NergalScoreVitae",
                "You have harvested ([0-9]+) vitae to empower you.",
                (batClientPlugin, matcher) ->
                        play.setVitae(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //nergal score,update Pontentia
        myTriggerManager.appendTrigger("NergalScorePotentia",
                "You have reaped ([0-9]+) potentia to empower you.",
                (batClientPlugin, matcher) ->
                        play.setPotentia(Integer.parseInt(matcher.group(1)))
                , true, false, false);
        //Sc,update paly stats
        myTriggerManager.appendTrigger("PlaySc",
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
        myTriggerManager.appendTrigger("NergalScVp",
                "::..:. \\[Vitae: ([0-9]+)/1000  Potentia: ([0-9]+)/1000,",
                (batClientPlugin, matcher) -> {
                    play.setVitae(Integer.parseInt(matcher.group(1)));
                    play.setPotentia(Integer.parseInt(matcher.group(2)));
                    if (Math.max(play.getVitae(), play.getPotentia()) >= automFoodPotentiaSize) {
                        clientGUI.printText(PluginMain.PLUGIN_NAME, TextUtil.colorText("!!!!HAVE A P/V IS FULL!!!!\n", TextUtil.RED));
                    }
                }, true, false, false);
        //nergal sc,update minion food time
        myTriggerManager.appendTrigger("NergalScFoodTime",
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
        myTriggerManager.appendTrigger("MinionSc",
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
        myTriggerManager.appendTrigger("HibernatingEnd",
                "^As the roots subside releasing you from their embrace you feel invigorated.",
                (batClientPlugin, matcher) ->
                        play.setLastHibernatingTime(System.currentTimeMillis()), true, false, false);
        //sleep awaken,update play last sleep time
        myTriggerManager.appendTrigger("SleepEnd",
                "^You awaken from your short rest, and feel slightly better.",
                (batClientPlugin, matcher) ->
                        play.setLastSleepTime(System.currentTimeMillis()), true, false, false);
        //food succes,update minion food time
        myTriggerManager.appendTrigger("FoodSuccess",
                "You raise your hand towards ([A-Za-z]+(\\s[a-z]+)*) and faint line of ethereal energy",
                (batClientPlugin, matcher) -> {
                    for (Minion minion : minionList) {
                        if (matcher.group(1).equals(minion.getName())) {
                            minion.setLastFoodTime(System.currentTimeMillis());
                        }
                    }
                }, true, false, false);
        //Combat and Scan is a pair
        myTriggerManager.appendTrigger("Combat",
                "\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\*\\* Round",
                (batClientPlugin, matcher) -> {
                    if (!play.isCombat()) {
                        play.setCombat(true);
                        combatTimer = new Timer();
                        combatTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                clientGUI.doCommand("@scan all");
                            }
                        }, 1, 2500);
                    }
                }, true, false, false);
        //Combat end
        myTriggerManager.appendTrigger("Scan",
                "You are not in combat right now.",
                (batClientPlugin, matcher) -> {
                    if (play.isCombat()) {
                        play.setCombat(false);
                        combatTimer.cancel();
                        MyAction replyAction = new ReplyAction(clientGUI);
                        replyAction.run();
                    }
                }, true, false, false);//Gag one scan
        //move to new room
        myTriggerManager.appendTrigger("NewRoom", "^(Obvious exits are)|^(Obvious exit is)|(Exits?:  )",
                (batClientPlugin, matcher) -> {
                    mobs.clear();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            clientGUI.printText(PluginMain.PLUGIN_NAME, "The room have :\n");
                            for (String s : mobs) {
                                clientGUI.printText(PluginMain.PLUGIN_NAME, "    " + s + "\n");
                            }
                        }
                    }, 50);
                }, true, false, false);
        //room monster,color code
        myTriggerManager.appendTrigger("RoomMonster",
                "^\u001B\\[1;32m([A-Za-z,'\\s-]+)\u001B\\[0m$",
                (batClientPlugin, matcher) -> mobs.add(matcher.group(1)), true, false, true);
    }

    /**
     * set command trigger
     */
    private void loadCommandTrigger() {
        //debug info
        myCommandTriggerManager.appendTrigger("nergaltoolDebug", "^nergaltool debug ([a-z]+)$",
                (batClientPlugin, matcher) -> {
                    switch (matcher.group(1)) {
                        case "minion":
                            StringBuilder minionInfo = new StringBuilder("Minions: ");
                            for (Minion name : minionList) {
                                minionInfo.append("\n");
                                minionInfo.append(name.toString());
                            }
                            clientGUI.printText(PluginMain.PLUGIN_NAME, minionInfo.toString() + "\n");
                            break;
                        case "play":
                            clientGUI.printText(PluginMain.PLUGIN_NAME, play.toString() + "\n");
                            break;
                    }
                }, true, true, false);
        //reply
        myCommandTriggerManager.appendTrigger("nergaltoolReply", "^nergaltool reply$",
                (batClientPlugin, matcher) -> {
                    MyAction combatAction = new CombatAction(clientGUI, play);
                    combatAction.run();
                }, true, true, false);
        //set show
        myCommandTriggerManager.appendTrigger("nergaltoolSet", "^nergaltool set ?([a-zA-Z]+)? ?([a-zA-Z0-9\\s]+)?",
                (batClientPlugin, matcher) -> settingManager.interpreter(clientGUI, matcher), true, true, false);
        //show all monster
        myCommandTriggerManager.appendTrigger("nergaltoolMonster", "^nergaltool monster",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(clientGUI, matcher), true, true, false);
        //remove monster index
        myCommandTriggerManager.appendTrigger("nergaltoolMonsterRemove", "^nergaltool monster remove ([0-9]+)",
                (batClientPlugin, matcher) -> MonsterInformation.interpreter(clientGUI, matcher), true, true, false);
        //harvest
        myCommandTriggerManager.appendTrigger("nergaltoolharv", "^nergaltool harvest$",
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
                        MyAction myAction = new HarvestAction(clientGUI, monsterName);
                        myAction.run();
                    } else {
                        clientGUI.doCommand("@bell " + settingManager.findSettingByName("playName").getValue());
                        clientGUI.printText(PluginMain.PLUGIN_NAME, TextUtil.colorText("no harvest here\n", TextUtil.RED));
                    }
                }, true, true, false);
    }

}
