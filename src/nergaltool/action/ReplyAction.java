package nergaltool.action;

import com.mythicscape.batclient.interfaces.ClientGUI;
import nergaltool.action.atom.*;
import nergaltool.action.base.MyAction;
import nergaltool.spell.SpellManager;

import java.util.Objects;

public class ReplyAction extends MyAction {
    public ReplyAction(ClientGUI clientGUI) {
        super(clientGUI);
    }

    @Override
    public void run() {
        MyAction start = new ReplyStartAction(clientGUI);
        MyAction food = new FoodAction(clientGUI);
        MyAction clw = new ClwAction(clientGUI);
        MyAction foodPotentia = new FoodPotentiaAction(clientGUI);
        MyAction spr = new SprAction(clientGUI, Math.max(Objects.requireNonNull(SpellManager.findSpellByName("hv")).getSp(),
                Objects.requireNonNull(SpellManager.findSpellByName("rp")).getSp()));
        MyAction bell = new BellAction(clientGUI);

        start.decorate(food);
        food.decorate(clw);
        clw.decorate(foodPotentia);
        foodPotentia.decorate(spr);
        spr.decorate(bell);

        start.run();
        super.run();
    }
}
