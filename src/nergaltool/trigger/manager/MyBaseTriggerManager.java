package nergaltool.trigger.manager;


import com.mythicscape.batclient.interfaces.BatClientPlugin;
import nergaltool.trigger.bean.MyTrigger;
import nergaltool.trigger.bean.TriggerBody;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public abstract class MyBaseTriggerManager<T> {
    protected final List<MyTrigger> myTriggerList = new ArrayList<>();

    public void appendTrigger(String name, String regexp, TriggerBody triggerBody, boolean isAction, boolean isGag, boolean isExpand) {
        appendTrigger(new MyTrigger(name, regexp, triggerBody, isAction, isGag, isExpand));
    }

    public void appendTrigger(MyTrigger myTrigger) {
        MyTrigger oldTrigger = findTriggerByName(myTrigger.getName());
        if (oldTrigger != null) {
            oldTrigger.setRegexp(myTrigger.getRegexp());
            oldTrigger.setTriggerBody(myTrigger.getTriggerBody());
            oldTrigger.setAction(myTrigger.isAction());
            oldTrigger.setGag(myTrigger.isGag());
        } else {
            myTriggerList.add(myTrigger);
        }
    }

    public T processAllTrigger(BatClientPlugin batClientPlugin, T content) {
        return new TriggerHander(batClientPlugin, content, myTriggerList).processAllTrigger();
    }

    abstract protected T setGagContent(T content);

    abstract protected Matcher matcher(T content, MyTrigger myTrigger);

    public MyTrigger findTriggerByName(String name) {
        for (MyTrigger myTrigger : myTriggerList) {
            if (myTrigger.getName().equals(name)) {
                return myTrigger;
            }
        }
        return null;
    }

    class TriggerHander {
        private T content;
        private final BatClientPlugin batClientPlugin;
        private final List<MyTrigger> myTriggerList;

        public TriggerHander(BatClientPlugin batClientPlugin, T content, List<MyTrigger> myTriggerList) {
            this.content = content;
            this.batClientPlugin = batClientPlugin;
            this.myTriggerList = myTriggerList;
        }

        public T processAllTrigger() {
            boolean isMatch = false;
            for (MyTrigger myTrigger : myTriggerList) {
                isMatch = process(myTrigger);
            }
            if (isMatch) {
                return this.content;
            } else {
                return null;
            }
        }

        private boolean process(MyTrigger myTrigger) {
            boolean isMatch = false;
            if (myTrigger.isAction()) {
                Matcher matcher = matcher(content, myTrigger);
                if (matcher.find()) {
                    isMatch = true;
                    myTrigger.getTriggerBody().body(batClientPlugin, matcher);
                    if (myTrigger.isGag()) {
                        content = setGagContent(content);
                    }
                }
            }
            return isMatch;
        }
    }
}
