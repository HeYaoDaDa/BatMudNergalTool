package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.BatClientPlugin;
import com.mythicscape.batclient.interfaces.ParsedResult;
import nergaltool.trigger.bean.MyTrigger;

import java.util.regex.Matcher;

/**
 * match channel text Trigger Manager
 */
public class MyTriggerManager extends MyBaseTriggerManager {
    //Singleton,instance
    private static final MyTriggerManager myTriggerManager = new MyTriggerManager();

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
            if (!myTrigger.isAction()) continue;
            Matcher matcher = getMatcher(parsedResult, myTrigger);
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
     * use isExpand return text
     *
     * @param parsedResult text object
     * @param myTrigger    trigger
     * @return text
     */
    private Matcher getMatcher(ParsedResult parsedResult, MyTrigger myTrigger) {
        Matcher matcher;
        if (myTrigger.isExpand()) {
            matcher = myTrigger.matcher(parsedResult.getOriginalText());
        } else {
            matcher = myTrigger.matcher(parsedResult.getStrippedText());
        }
        return matcher;
    }
}
