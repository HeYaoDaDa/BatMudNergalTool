package nergaltool.action.base;

import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.bean.Play;
import nergaltool.setting.SettingManager;
import nergaltool.trigger.manager.MyCommandTriggerManager;
import nergaltool.trigger.manager.MyTriggerManager;

import java.util.List;

/**
 * action super class
 */
public class MyAction {
    protected MyAction myAction;
    protected ClientGUI clientGUI;
    protected Play play = Play.getInstance();
    protected SettingManager settingManager = SettingManager.getInstance();
    protected MyTriggerManager myTriggerManager = MyTriggerManager.getInstance();
    protected MyCommandTriggerManager myCommandTriggerManager = MyCommandTriggerManager.getInstance();

    public MyAction(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
    }

    /**
     * set next action
     * @param myAction next action
     */
    public void decorate(MyAction myAction) {
        this.myAction = myAction;
    }

    /**
     * over method
     */
    public void run() {
        if (myAction != null) {
            myAction.run();
        }
    }

    /**
     * clear all trigger
     * @param triggerList trigger name list
     */
    protected void offTrigger(List<String> triggerList){
        for (String s:triggerList){
            myTriggerManager.getMyTrigger(s).setAction(false);
        }
    }
}
