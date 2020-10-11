package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.trigger.bean.MyTrigger;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.SpellUtil;
import nergaltool.utils.TextUtil;

import static nergaltool.PluginMain.PLUGIN_NAME;

public class InitStatsAction extends MyAction {
    public InitStatsAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        MyTrigger spellSp = myTriggerManager.findTriggerByName("SpellSp");
        if (spellSp == null || spellSp.isAction()) {
            myTriggerManager.addTrigger("SpellSp",
                    "^Spell point cost: ([0-9]+)",
                    (batClientPlugin, matcher) -> {
                        if (SpellUtil.hvSp == 0) {
                            SpellUtil.hvSp = Integer.parseInt(matcher.group(1));
                            clientGUI.printText(PLUGIN_NAME, "Now hv SP cost is " + TextUtil.colorText(String.valueOf(SpellUtil.hvSp), TextUtil.RED) + "\n");
                        } else if (SpellUtil.rpSp == 0) {
                            SpellUtil.rpSp = Integer.parseInt(matcher.group(1));
                            clientGUI.printText(PLUGIN_NAME, "Now rp SP cost is " + TextUtil.colorText(String.valueOf(SpellUtil.rpSp), TextUtil.RED) + "\n");
                        } else if (SpellUtil.clwSp == 0) {
                            SpellUtil.clwSp = Integer.parseInt(matcher.group(1));
                            clientGUI.printText(PLUGIN_NAME, "Now clw SP cost is " + TextUtil.colorText(String.valueOf(SpellUtil.clwSp), TextUtil.RED) + "\n");
                        } else if (SpellUtil.foodSp == 0) {
                            MyTriggerManager.getInstance().findTriggerByName("SpellSp").setAction(false);
                            SpellUtil.foodSp = Integer.parseInt(matcher.group(1));
                            clientGUI.printText(PLUGIN_NAME, "Now food SP cost is " + TextUtil.colorText(String.valueOf(SpellUtil.foodSp), TextUtil.RED) + "\n");
                            //exit begin next action
                            super.run();
                        }
                    }, true, false, false);
        } else {
            spellSp.setAction(true);
        }
        clientGUI.doCommand("@help spell harvest vitae;" +
                "@help spell reap potentia;" +
                "@help spell cure light wounds;" +
                "@help spell nourish enthralled;" +
                "sc;" +
                "nergal sc;");
    }
}
