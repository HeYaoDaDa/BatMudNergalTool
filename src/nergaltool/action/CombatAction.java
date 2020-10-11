package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.atom.BellAction;
import nergaltool.action.atom.SprAction;
import nergaltool.action.base.MyAction;
import nergaltool.bean.Minion;
import nergaltool.bean.Play;
import nergaltool.utils.SpellUtil;

import java.util.List;

public class CombatAction extends MyAction {
    private final Play play;

    public CombatAction(ClientGUI clientGUI, Play play) {
        super(clientGUI);
        this.play = play;
    }

    @Override
    public void run() {
        List<Minion> minionList = play.getMinionList();
        boolean needHeal = false;
        int maxSp = Math.max(SpellUtil.hvSp, SpellUtil.rpSp);
        for (Minion minion : minionList) {
            if (minion.getHp() <= minion.getHpMax() * Integer.parseInt(settingManager.findSettingByName("battleEndStartHealHpRate").getValue()) * 0.01) {
                needHeal = true;
                break;
            }
        }
        if (needHeal && Boolean.parseBoolean(settingManager.findSettingByName("battleEndHeal").getValue())) {
            MyAction replyAction = new ReplyAction(clientGUI);
            replyAction.run();
        } else {
            if (play.getSp() < maxSp) {
                MyAction spr = new SprAction(clientGUI, maxSp);
                MyAction bell = new BellAction(clientGUI);
                spr.decorate(bell);
                spr.run();
            } else {
                new BellAction(clientGUI).run();
            }
        }
        super.run();
    }
}
