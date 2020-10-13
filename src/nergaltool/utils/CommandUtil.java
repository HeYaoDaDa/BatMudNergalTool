package nergaltool.utils;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.setting.SettingManager;

/**
 * spell util
 */
public class CommandUtil {
    private CommandUtil() {
    }

    public static void hibernation(ClientGUI clientGUI) {
        clientGUI.doCommand("@use dreary hibernation");
    }

    public static void sleep(ClientGUI clientGUI) {
        clientGUI.doCommand("@sleep");
    }

    public static void bell(ClientGUI clientGUI) {
        clientGUI.doCommand("@bell " + SettingManager.getInstance().findSettingByName("playName").getValue());
    }
}
