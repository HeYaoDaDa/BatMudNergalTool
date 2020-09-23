package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Play;
import nergaltool.utils.SpellUtil;
import nergaltool.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static nergaltool.utils.Global.GENERIC;

public class HarvestAction extends MyAction {
    private final String monster;
    private Timer exit;

    public HarvestAction(ClientGUI clientGUI, String monster) {
        super(clientGUI);
        this.monster = monster;
    }

    @Override
    public void run() {
        if (play.getSp() >= Math.max(SpellUtil.hvSp, SpellUtil.rpSp)) {
            startSpell();
        }else {
            startSpr();
        }
    }

    private void startSpell() {
        if (play.getPotentia() > play.getVitae()) {
            SpellUtil.harvest(clientGUI,monster);
        } else {
            SpellUtil.reap(clientGUI,monster);
        }
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NoTraget");
        triggerList.add("NotSpClwAction");
        triggerList.add("SpellEndClwAction");
        triggerList.add("Movement");
        myTriggerManager.newTrigger("NoTraget",
                "Cast (Reap Potentia)|(Harvest Vitae) at what?",
                (batClientPlugin, matcher) -> {
                    exit.cancel();
                    offTrigger(triggerList);
                    clientGUI.printText(GENERIC, TextUtil.colorText(monster + " is no in here!\n", TextUtil.RED));
                    SpellUtil.bell(clientGUI);
                    super.run();
                },
                true, false,false);
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
                    exit = new Timer();
                    exit.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (!play.isCombat()) {
                                HarvestAction.this.run();
                            } else {
                                HarvestAction.super.run();
                            }
                        }
                    }, 1000);
                }, true, false, false);
        myTriggerManager.newTrigger("Movement",
                "^Your movement prevents you from casting the spell.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }

    /**
     * wait spr to hvsp
     */
    private void startSpr() {
        SprAction sprAction = new SprAction(clientGUI,Math.max(SpellUtil.hvSp, SpellUtil.rpSp));
        sprAction.decorate(this);
        sprAction.run();
    }
}
