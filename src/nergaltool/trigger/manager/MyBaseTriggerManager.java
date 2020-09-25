package nergaltool.trigger.manager;


import nergaltool.trigger.bean.MyTrigger;
import nergaltool.trigger.bean.TriggerBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Trigger manager supper class
 */
public class MyBaseTriggerManager {
    //manager Trigger
    protected final List<MyTrigger> myTriggerList = new ArrayList<>();

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
