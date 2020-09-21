package nergaltool.bean;

import com.mythicscape.batclient.interfaces.ClientGUI;

import java.util.regex.Matcher;

public class Minion {
    private ClientGUI clientGUI;
    private String name;
    private int hp;
    private int sp;
    private int ep;
    private int hp_max;
    private int sp_max;
    private int ep_max;
    private int vitae;
    private int potentia;
    private long lastFoodTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getEp() {
        return ep;
    }

    public void setEp(int ep) {
        this.ep = ep;
    }

    public int getHp_max() {
        return hp_max;
    }

    public void setHp_max(int hp_max) {
        this.hp_max = hp_max;
    }

    public int getSp_max() {
        return sp_max;
    }

    public void setSp_max(int sp_max) {
        this.sp_max = sp_max;
    }

    public int getEp_max() {
        return ep_max;
    }

    public void setEp_max(int ep_max) {
        this.ep_max = ep_max;
    }

    public int getVitae() {
        return vitae;
    }

    public void setVitae(int vitae) {
        this.vitae = vitae;
    }

    public int getPotentia() {
        return potentia;
    }

    public void setPotentia(int potentia) {
        this.potentia = potentia;
    }

    public long getLastFoodTime() {
        return lastFoodTime;
    }

    public void setLastFoodTime(long lastFoodTime) {
        this.lastFoodTime = lastFoodTime;
    }

    public Minion(ClientGUI clientGUI, String name, int hp, int hp_max, int sp, int sp_max, int ep, int ep_max) {
        this.clientGUI = clientGUI;
        this.name = name;
        this.hp = hp;
        this.hp_max = hp_max;
        this.sp = sp;
        this.sp_max = sp_max;
        this.ep = ep;
        this.ep_max = ep_max;
    }

    public Minion(ClientGUI clientGUI, Matcher matcher) {
        this(clientGUI
                , matcher.group(1)
                , Integer.parseInt(matcher.group(3))
                , Integer.parseInt(matcher.group(4))
                , Integer.parseInt(matcher.group(6))
                , Integer.parseInt(matcher.group(7))
                , Integer.parseInt(matcher.group(9))
                , Integer.parseInt(matcher.group(10)));
    }

    public void update(Matcher matcher) {
        this.hp = Integer.parseInt(matcher.group(3));
        this.hp_max = Integer.parseInt(matcher.group(4));
        this.sp = Integer.parseInt(matcher.group(6));
        this.sp_max = Integer.parseInt(matcher.group(7));
        this.ep = Integer.parseInt(matcher.group(9));
        this.ep_max = Integer.parseInt(matcher.group(10));
    }

    public void stats() {
        clientGUI.doCommand("@minion " + name + " stats");
    }

    public void cure_light_wounds() {
        clientGUI.doCommand("@cast cure light wounds at " + name);
    }

    public void nourish_enthralled(int size, String energy) {
        clientGUI.doCommand("@cast nourish enthralled at " + name + " consume " + size + " " + energy);
    }

    @Override
    public String toString() {
        return "Name:" + name + " HP:" + hp + " HPMAX:" + hp_max + " SP:" + sp + " SPMAX:" + sp_max + " EP:" + ep
                + " EPMAX:" + ep_max + " Vitae:" + vitae + " Potentia:" + potentia + "food time:" + ((System.currentTimeMillis() - lastFoodTime) / 1000) + "\n";
    }
}
