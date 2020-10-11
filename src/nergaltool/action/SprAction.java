package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.SpellUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * spr action
 */
public class SprAction extends MyAction {
    public static final int SLEEP_FAIL_ADD_INTERVAL = 60 * 1000;
    public static final int SLEEP_CD = 5 * 60 * 1000;
    private final int number;//sp to number then exit

    public SprAction(ClientGUI clientGUI, int number) {
        super(clientGUI);
        this.number = number;
    }

    @Override
    public void run() {
        if (play.getSp() >= number) {
            super.run();
        } else {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - SLEEP_CD) > play.getLastHibernatingTime()) {
                startHibernation();
            } else if ((currentTime - SLEEP_CD) > play.getLastSleepTime()) {
                startSleep();
            } else {
                startWait();
            }
        }
    }

    /**
     * use hibernation spr
     */
    private void startHibernation() {
        SpellUtil.hibernation(clientGUI);
        List<String> triggerList = new ArrayList<>();
        triggerList.add("Movement");
        triggerList.add("HibernatingEndSprAction");
        triggerList.add("HibernationFail");
        myTriggerManager.addTrigger("HibernatingEndSprAction",
                "^As the roots subside releasing you from their embrace you feel invigorated.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    run();
                }, true, false, false);
        myTriggerManager.addTrigger("HibernationFail",
                "(You don't feel like hibernating yet.)|(You fail to fall into dreary hibernation.)",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    play.setLastHibernatingTime(play.getLastHibernatingTime() + SLEEP_FAIL_ADD_INTERVAL);
                    run();
                },
                true, false, false);

        movementTrigger(triggerList);
    }


    /**
     * use sleep spr
     */
    private void startSleep() {
        SpellUtil.sleep(clientGUI);
        List<String> triggerList = new ArrayList<>();
        triggerList.add("Movement");
        triggerList.add("SleepEndSprAction");
        triggerList.add("SleepFail");
        myTriggerManager.addTrigger("SleepEndSprAction",
                "^You awaken from your short rest, and feel slightly better.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    run();
                }, true, false, false);

        myTriggerManager.addTrigger("SleepFail",
                "You don't quite feel like camping at the moment.",
                (batClientPlugin, matcher) -> {
                    offTrigger(triggerList);
                    run();
                },
                true, false, false);

        movementTrigger(triggerList);
    }

    /**
     * wait spr
     */
    private void startWait() {
        myTriggerManager.addTrigger("PlayScSprAction",
                "hp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "sp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "ep: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\]",
                (batClientPlugin, matcher) -> {//beacause have(+/-number) so 3,6,9 no use
                    if (play.getSp() >= number) {
                        MyTriggerManager.getInstance().findTriggerByName("PlayScSprAction").setAction(false);
                        super.run();
                    }
                }, true, false, false);
    }

    /**
     * move stop skill
     */
    private void movementTrigger(List<String> triggerList) {
        myTriggerManager.addTrigger("Movement",
                "^Your movement prevents you from doing the skill.",
                (batClientPlugin, matcher) ->
                        offTrigger(triggerList),
                true, false, false);
    }
}
