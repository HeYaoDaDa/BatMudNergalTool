package nergaltool.utils;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.setting.SettingManager;

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
    public static void food(ClientGUI clientGUI,String traget,int size,String energy){
        clientGUI.doCommand("@cast nourish enthralled at "+ traget + " consume " + size + " " + energy);
    }
    public static void bell(ClientGUI clientGUI){
        clientGUI.doCommand("@bell " + SettingManager.getInstance().getSetting("playName").getValue());
    }
    public static void harvest(ClientGUI clientGUI,String monster){
        clientGUI.doCommand("@cast Harvest vitae at " + monster);
    }
    public static void reap(ClientGUI clientGUI,String monster){
        clientGUI.doCommand("@cast Reap potentia at " + monster);
    }
}
