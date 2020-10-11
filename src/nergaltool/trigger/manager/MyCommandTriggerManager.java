package nergaltool.trigger.manager;


import nergaltool.trigger.bean.MyTrigger;

import java.util.regex.Matcher;

/**
 * match play input Trigger Manager
 */
public class MyCommandTriggerManager extends MyBaseTriggerManager<String> {
    //Singleton,instance
    private static final MyCommandTriggerManager myCommandTriggerManager = new MyCommandTriggerManager();


    //Singleton,get single instance
    public static MyCommandTriggerManager getInstance() {
        return myCommandTriggerManager;
    }

    protected Matcher matcher(String content, MyTrigger myTrigger) {
        return myTrigger.matcher(content);
    }

    protected String setGagContent(String content) {
        return "";
    }
}
