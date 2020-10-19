package nergaltool.spell;

import com.mythicscape.batclient.interfaces.ClientGUI;

public class Spell {
    private String name;
    private String command;
    private int sp;

    public Spell(String name, String command, int sp) {
        this.name = name;
        this.command = command;
        this.sp = sp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public void use(ClientGUI clientGUI, String parameter) {
        clientGUI.doCommand("cast " + command + " at " + parameter);
    }
}
