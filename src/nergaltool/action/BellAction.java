package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.base.MyAction;
import nergaltool.utils.Global;
import nergaltool.utils.SpellUtil;
import nergaltool.utils.TextUtil;

public class BellAction extends MyAction {
    public BellAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        SpellUtil.bell(clientGUI);
        clientGUI.printText(Global.GENERIC, TextUtil.colorText("*********************************\n" +
                "**********Action is end**********\n" +
                "*********************************\n",
                TextUtil.GREEN));
        super.run();
    }
}
