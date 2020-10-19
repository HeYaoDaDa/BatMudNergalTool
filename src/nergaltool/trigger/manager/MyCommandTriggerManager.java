package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import nergaltool.action.CombatAction;
import nergaltool.action.ReplyAction;
import nergaltool.action.atom.HarvestAction;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.bean.MyTrigger;
import nergaltool.utils.MonsterManager;
import nergaltool.utils.TextUtil;

import java.util.List;
import java.util.regex.Matcher;

public class MyCommandTriggerManager extends MyBaseTriggerManager<String> {
    private static final MyCommandTriggerManager myCommandTriggerManager = new MyCommandTriggerManager();


    public static MyCommandTriggerManager getInstance() {
        return myCommandTriggerManager;
    }

    protected Matcher matcher(String content, MyTrigger myTrigger) {
        return myTrigger.matcher(content);
    }

    protected String setGagContent(String content) {
        return "";
    }

    @Override
    public void init(PluginMain pluginMain) {
        final Play play = Play.getInstance();
        final List<Minion> minionList = play.getMinionList();
        final ClientGUI clientGUI = pluginMain.getClientGUI();
        final List<String> mobs = pluginMain.room.getMonsterList();
        final SettingManager settingManager = SettingManager.getInstance();
        final MonsterManager monsterManager = MonsterManager.getInstance();
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
                    MyAction replyAction = new ReplyAction(clientGUI);
                    replyAction.run();
                }, true, true, false);
        //set show
        myCommandTriggerManager.appendTrigger("nergaltoolSet", "^nergaltool set ?([a-zA-Z]+)? ?([a-zA-Z0-9\\s]+)?",
                (batClientPlugin, matcher) -> settingManager.interpreter(clientGUI, matcher), true, true, false);
        //show all monster
        myCommandTriggerManager.appendTrigger("nergaltoolMonster", "^nergaltool monster",
                (batClientPlugin, matcher) -> monsterManager.interpreter(clientGUI), true, true, false);
        //remove monster index
        myCommandTriggerManager.appendTrigger("nergaltoolMonsterRemove", "^nergaltool monster remove ([0-9]+)",
                (batClientPlugin, matcher) -> monsterManager.interpreter(clientGUI), true, true, false);
        //harvest
        myCommandTriggerManager.appendTrigger("nergaltoolharv", "^nergaltool harvest$",
                (batClientPlugin, matcher) -> {
                    if (mobs.size() >= 1) {
                        boolean isTwo = false;
                        String monsterName = mobs.get(0);
                        for (String s : MonsterManager.monsterList) {
                            if (monsterName.equals(s)) {
                                isTwo = true;
                                break;
                            }
                        }
                        if (!isTwo)
                            MonsterManager.monsterList.add(monsterName);
                        MyAction myAction = new HarvestAction(clientGUI, monsterName);
                        myAction.run();
                    } else {
                        clientGUI.doCommand("@bell " + settingManager.findSettingByName("playName").getValue());
                        clientGUI.printText(PluginMain.PLUGIN_NAME, TextUtil.colorText("no harvest here\n", TextUtil.RED));
                    }
                }, true, true, false);
    }
}
