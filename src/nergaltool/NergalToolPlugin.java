package nergaltool;

import com.mythicscape.batclient.interfaces.*;
import nergaltool.bean.Play;
import nergaltool.utils.Global;
import nergaltool.utils.TextUtil;

import static nergaltool.trigger.manager.MyCommandTriggerManager.myCommandTriggerManager;
import static nergaltool.trigger.manager.MyTriggerManager.myTriggerManager;

/**
 * plugin main
 */
public class NergalToolPlugin extends BatClientPlugin implements BatClientPluginTrigger, BatClientPluginCommandTrigger, BatClientPluginUtil {
    private ClientGUI myCLientGUI;
    private final Play play = Play.getInstance();

    @Override
    public void loadPlugin() {
        myCLientGUI = getClientGUI();
        myCLientGUI.printText(getName(), TextUtil.colorText("--- Loading " + getName() + " ---\n", TextUtil.GREEN));

        loadTrigger();
    }

    @Override
    public String getName() {
        return Global.PLUGIN_NAME;
    }

    @Override
    public String trigger(String s) {
        return myCommandTriggerManager.process(this, s);
    }

    @Override
    public ParsedResult trigger(ParsedResult parsedResult) {
        return myTriggerManager.process(this, parsedResult);
    }

    @Override
    public void clientExit() {

    }

    /**
     * set Trigger
     */
    private void loadTrigger() {
        myTriggerManager.newTrigger("PlaySc",
                "hp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "sp: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\] " +
                        "ep: ([0-9]+) \\(([0-9]+)\\) \\[(\\S[0-9]+)*\\]",
                (batClientPlugin, matcher) ->
                        myCLientGUI.printText(Global.GENERIC, matcher.group(1) + "\n")
                , true, false, false);
    }
}
