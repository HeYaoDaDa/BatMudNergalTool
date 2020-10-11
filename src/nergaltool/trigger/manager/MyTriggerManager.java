package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.ParsedResult;
import nergaltool.trigger.bean.MyTrigger;

import java.util.regex.Matcher;

/**
 * match channel text Trigger Manager
 */
public class MyTriggerManager extends MyBaseTriggerManager<ParsedResult> {
    //Singleton,instance
    private static final MyTriggerManager myTriggerManager = new MyTriggerManager();

    //Singleton,get single instance
    public static MyTriggerManager getInstance() {
        return myTriggerManager;
    }

    @Override
    protected ParsedResult setGagContent(ParsedResult content) {
        content.setOriginalText("");
        return content;
    }

    @Override
    protected Matcher matcher(ParsedResult parsedResult, MyTrigger myTrigger) {
        Matcher matcher;
        if (myTrigger.isExpand()) {
            matcher = myTrigger.matcher(parsedResult.getOriginalText());
        } else {
            matcher = myTrigger.matcher(parsedResult.getStrippedText());
        }
        return matcher;
    }
}
