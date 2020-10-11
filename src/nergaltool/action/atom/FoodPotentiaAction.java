package nergaltool.action.atom;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.utils.SpellUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * food potentia action
 */
public class FoodPotentiaAction extends MyAction {
    public FoodPotentiaAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        if (play.getPotentia() > Integer.parseInt(settingManager.findSettingByName("foodPotentiaSize").getValue())){
            startFood();
        }else {
            super.run();
        }
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
     */
    private void startFood() {
        SpellUtil.food(clientGUI,
                settingManager.findSettingByName("foodPotentiaTraget").getValue(),
                play.getPotentia(),
                "potentia");
        List<String> triggerList = new ArrayList<>();
        triggerList.add("NotSpFoodPotentiaAction");
        triggerList.add("SpellEndFoodPotentiaAction");
        triggerList.add("Movement");
        myTriggerManager.addTrigger("NotSpFoodPotentiaAction",
                "^You do not have enough spell points to cast the spell",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    startSpr();
                }, true, false, false);
        myTriggerManager.addTrigger("SpellEndFoodPotentiaAction",
                "^You are done with the chant.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            FoodPotentiaAction.this.run();
                        }
                    }, 500);
                }, true, false, false);
        myTriggerManager.addTrigger("Movement",
                "^Your movement prevents you from casting the spell.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }
}
