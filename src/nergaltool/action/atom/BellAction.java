package nergaltool.action.atom;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import nergaltool.action.base.MyAction;
import nergaltool.utils.CommandUtil;
import nergaltool.utils.TextUtil;

public class BellAction extends MyAction {
    public BellAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        CommandUtil.bell(clientGUI);
        clientGUI.printText(PluginMain.GENERIC, TextUtil.colorText("*********************************\n" +
                "**********Action is end**********\n" +
                "*********************************\n",
                TextUtil.GREEN));
        super.run();
    }
}
