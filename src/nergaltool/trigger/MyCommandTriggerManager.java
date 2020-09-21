package nergaltool.trigger;


import com.mythicscape.batclient.interfaces.BatClientPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * match play input Trigger Manager
 */
public class MyCommandTriggerManager {
    //Singleton,instance
    public static MyCommandTriggerManager myCommandTriggerManager = new MyCommandTriggerManager();

    //manager Trigger
    private final List<MyTrigger> myTriggerList = new ArrayList<>();

    //Singleton,private constructor
    private MyCommandTriggerManager() {
    }

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
            //continue no action trigger
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

    /**
     * create trigger method
     *
     * @param name        trigger name key
     * @param regexp      regular
     * @param triggerBody trigger match method
     * @param isAction    action
     * @param isGag       gag
     */
    public void newTrigger(String name, String regexp, TriggerBody triggerBody, boolean isAction, boolean isGag) {
        newTrigger(new MyTrigger(name, regexp, triggerBody, isAction, isGag));
    }

    /**
     * cteate trigger method
     *
     * @param myTrigger trigger
     */
    public void newTrigger(MyTrigger myTrigger) {
        //find trigger is existence
        MyTrigger oldTrigger = getMyTrigger(myTrigger.getName());
        if (oldTrigger != null) {
            //if existence update old trigger
            oldTrigger.setRegexp(myTrigger.getRegexp());
            oldTrigger.setTriggerBody(myTrigger.getTriggerBody());
            oldTrigger.setAction(myTrigger.isAction());
            oldTrigger.setGag(myTrigger.isGag());
        } else {
            myTriggerList.add(myTrigger);
        }
    }

    /**
     * use name find trigger on list
     *
     * @param name trigger name
     * @return trigger
     */
    public MyTrigger getMyTrigger(String name) {
        for (MyTrigger myTrigger : myTriggerList) {
            if (myTrigger.getName().equals(name)) {
                return myTrigger;
            }
        }
        return null;
    }
}
