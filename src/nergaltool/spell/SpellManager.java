package nergaltool.spell;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.trigger.manager.MyTriggerManager;
import nergaltool.utils.TextUtil;

import java.util.ArrayList;
import java.util.List;

import static nergaltool.PluginMain.PLUGIN_NAME;

public class SpellManager {
    private static final List<Spell> spellList = new ArrayList<>();

    private static final SpellManager SPELL_MANAGER = new SpellManager();

    public static SpellManager getInstance() {
        return SPELL_MANAGER;
    }

    public void appendSpell(String name, String command, int sp) {
        this.appendSpell(new Spell(name, command, sp));
    }

    public void appendSpell(Spell spell) {
        spellList.add(spell);
    }

    public void init() {
        appendSpell("clw", "cure light wounds", 0);
        appendSpell("food", "nourish enthralled", 0);
        appendSpell("hv", "Harvest vitae", 0);
        appendSpell("rp", "Reap potentia", 0);
    }

    public static Spell findSpellByName(String name) {
        for (Spell spell : spellList) {
            if (spell.getName().equals(name)) {
                return spell;
            }
        }
        return null;
    }

    public void initSpCost(ClientGUI clientGUI) {
        clearSpCost();
        appendSpellSpTrigger(clientGUI);
        StringBuilder stringBuilder = new StringBuilder();
        helpCommand(clientGUI, stringBuilder);
    }

    private void helpCommand(ClientGUI clientGUI, StringBuilder stringBuilder) {
        for (Spell spell : spellList) {
            stringBuilder.append("@help spell ");
            stringBuilder.append(spell.getCommand());
            stringBuilder.append(";");
        }
        clientGUI.doCommand(stringBuilder.toString());
    }

    private void appendSpellSpTrigger(ClientGUI clientGUI) {
        MyTriggerManager.getInstance().appendTrigger("SpellSp",
                "^Spell point cost: ([0-9]+)",
                (batClientPlugin, matcher) -> {
                    for (Spell spell : spellList) {
                        if (spell.getSp() == 0) {
                            spell.setSp(Integer.parseInt(matcher.group(1)));
                            clientGUI.printText(PLUGIN_NAME, "Now " + spell.getName() + " SP cost is " + TextUtil.colorText(String.valueOf(spell.getSp()), TextUtil.RED) + "\n");
                            break;
                        }
                    }
                }, true, false, false);
    }

    private void clearSpCost() {
        for (Spell spell : spellList) {
            spell.setSp(0);
        }
    }
}
