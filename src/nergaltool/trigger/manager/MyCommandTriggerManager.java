package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.BatClientPlugin;
import nergaltool.trigger.bean.MyTrigger;

import java.util.regex.Matcher;

/**
 * match play input Trigger Manager
 */
public class MyCommandTriggerManager extends MyBaseTriggerManager {
    //Singleton,instance
    private static final MyCommandTriggerManager myCommandTriggerManager = new MyCommandTriggerManager();


    //Singleton,get single instance
    public static MyCommandTriggerManager getInstance() {
        return myCommandTriggerManager;
    }

    /**
     * process all trigger match and run
     *
     * @param batClientPlugin context
     * @param content         command content
     * @return content
     */
    public synchronized String process(BatClientPlugin batClientPlugin, String content) {
        boolean isMatch = false;
        for (MyTrigger myTrigger : myTriggerList) {
            if (!myTrigger.isAction()) continue;
            Matcher matcher = myTrigger.matcher(content);
            if (matcher.find()) {
                isMatch = true;
                myTrigger.getTriggerBody().body(batClientPlugin, matcher);
                //if is gag clear text
                if (myTrigger.isGag()) {
                    content = "";
                    break;
                }
            }
        }
        if (isMatch) {
            return content;
        } else {
            //if no match return null
            return null;
        }
    }
}
