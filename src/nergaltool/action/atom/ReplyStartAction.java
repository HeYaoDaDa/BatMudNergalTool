package nergaltool.action.atom;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.PluginMain;
import nergaltool.action.base.MyAction;
import nergaltool.utils.TextUtil;

/**
 * reply start
 */
public class ReplyStartAction extends MyAction {
    public ReplyStartAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        clientGUI.printText(PluginMain.GENERIC, TextUtil.colorText("Start Reply Action\n",
                TextUtil.GREEN));
        super.run();
    }
}
