package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.utils.Global;
import nergaltool.utils.SpellUtil;
import nergaltool.utils.TextUtil;

/**
 * reply start
 */
public class ReplyAction extends MyAction {
    public ReplyAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        clientGUI.printText(Global.GENERIC, TextUtil.colorText("Start Reply Action\n",
                TextUtil.GREEN));
        super.run();
    }
}
