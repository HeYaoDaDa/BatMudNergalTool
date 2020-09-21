package nergaltool.trigger;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * use StrippedText matcher Trigger
 */
public class MyTrigger {
    private String name;
    private String regexp;
    private TriggerBody triggerBody;
    private boolean isAction;
    private boolean isGag;

    public MyTrigger(String name, String regexp, TriggerBody triggerBody, boolean isAction, boolean isGag) {
        this.name = name;
        this.regexp = regexp;
        this.triggerBody = triggerBody;
        this.isAction = isAction;
        this.isGag = isGag;
    }

    public Matcher matcher(String s) {
        return Pattern.compile(regexp).matcher(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public TriggerBody getTriggerBody() {
        return triggerBody;
    }

    public void setTriggerBody(TriggerBody triggerBody) {
        this.triggerBody = triggerBody;
    }

    public boolean isAction() {
        return isAction;
    }

    public void setAction(boolean action) {
        isAction = action;
    }

    public boolean isGag() {
        return isGag;
    }

    public void setGag(boolean gag) {
        isGag = gag;
    }
}
