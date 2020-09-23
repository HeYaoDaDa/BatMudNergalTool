package nergaltool.utils;

import com.mythicscape.batclient.interfaces.ClientGUI;

/**
 * spell util
 */
public class SpellUtil {
    private SpellUtil(){}

    public static int hvSp = 0;
    public static int rpSp = 0;
    public static int clwSp = 0;
    public static int foodSp = 0;

    public static void hibernation(ClientGUI clientGUI){
        clientGUI.doCommand("@use dreary hibernation");
    }
    public static void sleep(ClientGUI clientGUI){
        clientGUI.doCommand("@sleep");
    }
    public static void clw(ClientGUI clientGUI,String traget){
        clientGUI.doCommand("@cast cure light wounds at "+traget);
    }
}
