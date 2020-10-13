package nergaltool.action.atom;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.spell.SpellMananger;
import nergaltool.utils.CommandUtil;
import nergaltool.utils.TextUtil;

import java.util.*;

import static nergaltool.PluginMain.GENERIC;

public class HarvestAction extends MyAction {
    private final String monster;
    private Timer exit;

    public HarvestAction(ClientGUI clientGUI, String monster) {
        super(clientGUI);
        this.monster = monster;
    }

    @Override
    public void run() {
        if (play.getSp() >= Math.max(Objects.requireNonNull(SpellMananger.findSpellByName("hv")).getSp(),
                Objects.requireNonNull(SpellMananger.findSpellByName("hv")).getSp())) {
            startSpell();
        } else {
            startSpr();
        }
    }

    private void startSpell() {
        if (play.getPotentia() > play.getVitae()) {
            Objects.requireNonNull(SpellMananger.findSpellByName("hv")).use(clientGUI, monster);
        } else {
            Objects.requireNonNull(SpellMananger.findSpellByName("rp")).use(clientGUI, monster);
        }
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NoTraget");
        triggerList.add("NotSpHarvestAction");
        triggerList.add("SpellEndHarvestAction");
        triggerList.add("Movement");
        myTriggerManager.appendTrigger("NoTraget",
                "Cast (Reap Potentia)|(Harvest Vitae) at what?",
                (batClientPlugin, matcher) -> {
                    exit.cancel();
                    offTrigger(triggerList);
                    clientGUI.printText(GENERIC, TextUtil.colorText(monster + " is no in here!\n", TextUtil.RED));
                    CommandUtil.bell(clientGUI);
                    super.run();
                },
                true, false, false);
        myTriggerManager.appendTrigger("NotSpHarvestAction",
                "^You do not have enough spell points to cast the spell",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    startSpr();
                }, true, false, false);
        myTriggerManager.appendTrigger("SpellEndHarvestAction",
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
        myTriggerManager.appendTrigger("Movement",
                "^Your movement prevents you from casting the spell.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }

    /**
     * wait spr to hvsp
     */
    private void startSpr() {
        SprAction sprAction = new SprAction(clientGUI, Math.max(Objects.requireNonNull(SpellMananger.findSpellByName("hv")).getSp(),
                Objects.requireNonNull(SpellMananger.findSpellByName("hv")).getSp()));
        sprAction.decorate(this);
        sprAction.run();
    }
}
