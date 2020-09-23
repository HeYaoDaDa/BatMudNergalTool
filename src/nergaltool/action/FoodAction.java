package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.utils.SpellUtil;
import nergaltool.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static nergaltool.utils.Global.FOOD_CD;
import static nergaltool.utils.Global.PLUGIN_NAME;

/**
 * food heal action
 */
public class FoodAction extends MyAction {
    public FoodAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        Minion target = getTraget();

        if (target != null) {
            if (play.getVitae() < Integer.parseInt(settingManager.getSetting("foodMaxSize").getValue())) {
                clientGUI.printText(PLUGIN_NAME, TextUtil.colorText("!!!!!YOU VITAE IS NO ENOUGH!!!!!\n", TextUtil.RED));
                super.run();
            } else if (play.getSp() < SpellUtil.foodSp) {//sp empty
                clientGUI.printText(PLUGIN_NAME, TextUtil.colorText("NOSP\n", TextUtil.RED));
                startSpr();
            } else {
                startFood(target);
            }
        } else {
            super.run();
        }
    }

    /**
     * find need food minion
     *
     * @return food target
     */
    private Minion getTraget() {
        Minion target = null;
        for (Minion minion : play.getMinionList()) {
            if (minion.getHp() <= minion.getHpMax() - Integer.parseInt(settingManager.getSetting("foodHpLoss").getValue())
                    && System.currentTimeMillis() - minion.getLastFoodTime() > FOOD_CD
                    && !settingManager.getSetting("foodBlackList").getListValue().contains(minion.getName())
            ) {
                target = minion;
            }
        }
        return target;
    }

    /**
     * wait spr to foodsp
     */
    private void startSpr() {
        SprAction sprAction = new SprAction(clientGUI, SpellUtil.foodSp);
        sprAction.decorate(this);
        sprAction.run();
    }

    /**
     * food
     *
     * @param target food target
     */
    private void startFood(Minion target) {
        SpellUtil.food(clientGUI,
                target.getName(),
                Math.min((target.getHpMax() - target.getHp()) / Integer.parseInt(settingManager.getSetting("eachVitaeHpr").getValue()),
                        Integer.parseInt(settingManager.getSetting("foodMaxSize").getValue())),
                "vitae");
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NotSpFoodAction");
        triggerList.add("SpellEndFoodAction");
        triggerList.add("Movement");
        myTriggerManager.newTrigger("NotSpFoodAction",
                "^You do not have enough spell points to cast the spell",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    startSpr();
                }, true, false, false);
        myTriggerManager.newTrigger("SpellEndFoodAction",
                "^You are done with the chant.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            FoodAction.this.run();
                        }
                    }, 500);
                }, true, false, false);
        myTriggerManager.newTrigger("Movement",
                "^Your movement prevents you from casting the spell.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }
}