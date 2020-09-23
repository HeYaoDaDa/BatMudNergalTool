package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.ParsedResult;
import nergaltool.trigger.bean.MyTrigger;
import nergaltool.trigger.bean.TriggerBody;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * match channel text Trigger Manager
 */
public class MyTriggerManager {
    //Singleton,instance
    private static MyTriggerManager myTriggerManager = new MyTriggerManager();

    //manager Trigger
    private final List<MyTrigger> myTriggerList = new ArrayList<>();

    //Singleton,private constructor
    private MyTriggerManager() {
    }

    //Singleton,get single instance
    public static MyTriggerManager getInstance() {
        return myTriggerManager;
    }

    /**
     * process all trigger match and run
     *
     * @param batClientPlugin context
     * @param parsedResult    channel text object
     * @return channel text object
     */
    public synchronized ParsedResult process(BatClientPlugin batClientPlugin, ParsedResult parsedResult) {
        boolean isMatch = false;
        for (MyTrigger myTrigger : myTriggerList) {
            //continue no action trigger
            if (!myTrigger.isAction()) continue;
            Matcher matcher;
            //if is expand,then use OriginalText match
            if (myTrigger.isExpand()) {
                matcher = myTrigger.matcher(parsedResult.getOriginalText());
            } else {
                matcher = myTrigger.matcher(parsedResult.getStrippedText());
            }
            if (matcher.find()) {
                isMatch = true;
                myTrigger.getTriggerBody().body(batClientPlugin, matcher);
                //if is gag clear text
                if (myTrigger.isGag()) {
                    parsedResult.setOriginalText("");
                    break;
                }
            }
        }
        if (isMatch) {
            return parsedResult;
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
     * @param isExpand    expand
     */
    public void newTrigger(String name, String regexp, TriggerBody triggerBody, boolean isAction, boolean isGag, boolean isExpand) {
        newTrigger(new MyTrigger(name, regexp, triggerBody, isAction, isGag, isExpand));
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
