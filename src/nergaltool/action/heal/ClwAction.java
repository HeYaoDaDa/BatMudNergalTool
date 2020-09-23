package nergaltool.action.heal;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.action.base.SprAction;
import nergaltool.bean.Minion;
import nergaltool.utils.Global;
import nergaltool.utils.SpellUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClwAction extends MyAction {


    public ClwAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        Minion target = getTraget();
        if (target != null) {
            if (play.getSp() < SpellUtil.clwSp) {//sp empty
                startSpr();
            } else {
                clientGUI.printText(Global.PLUGIN_NAME,"target:"+target.getName()+"\n");
                startClw(target);
            }
        }else {
            super.run();
        }
    }

    /**
     * find need clw minion
     *
     * @return need clw minion
     */
    private Minion getTraget() {
        Minion target = null;
        for (Minion minion : play.getMinionList()) {
            if (minion.getHp() <= minion.getHpMax() - Integer.parseInt(settingManager.getSetting("clwEndHpLoss").getValue()) &&
                    !settingManager.getSetting("clwBlackList").getListValue().contains(minion.getName())) {
                target = minion;
            }
        }
        return target;
    }

    /**
     * wait spr to clwsp
     */
    private void startSpr() {
        SprAction sprAction = new SprAction(clientGUI, SpellUtil.clwSp);
        sprAction.decorate(this);
        sprAction.run();
    }

    private void startClw(Minion target) {
        SpellUtil.clw(clientGUI, target.getName());
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NotSpClwAction");
        triggerList.add("SpellEndClwAction");
        myTriggerManager.newTrigger("NotSpClwAction",
                "^You do not have enough spell points to cast the spell",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    startSpr();
                }, true, false, false);
        myTriggerManager.newTrigger("SpellEndClwAction",
                "^You are done with the chant.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ClwAction.this.run();
                        }
                    }, 500);
                }, true, false, false);
    }
}
