package nergaltool.action.atom;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.utils.SpellUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * clw action
 */
public class ClwAction extends MyAction {


    public ClwAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        Minion target = getTarget();
        if (target != null) {
            if (play.getSp() < SpellUtil.clwSp) {//sp empty
                startSpr();
            } else {
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
    private Minion getTarget() {
        Minion target = null;
        for (Minion minion : play.getMinionList()) {
            if (minion.getHp() <= minion.getHpMax() - Integer.parseInt(settingManager.findSettingByName("clwEndHpLoss").getValue()) &&
                    !settingManager.findSettingByName("clwBlackList").getListValue().contains(minion.getName())) {
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

    /**
     * clw
     * @param target clw target
     */
    private void startClw(Minion target) {
        SpellUtil.clw(clientGUI, target.getName());
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NotSpClwAction");
        triggerList.add("SpellEndClwAction");
        triggerList.add("Movement");
        myTriggerManager.appendTrigger("NotSpClwAction",
                "^You do not have enough spell points to cast the spell",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    startSpr();
                }, true, false, false);
        myTriggerManager.appendTrigger("SpellEndClwAction",
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
        myTriggerManager.appendTrigger("Movement",
                "^Your movement prevents you from casting the spell.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }
}
