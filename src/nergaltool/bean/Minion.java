package nergaltool.bean;

import com.mythicscape.batclient.interfaces.ClientGUI;

import java.util.regex.Matcher;

/**
 * minion information
 */
public class Minion {
    private String name;
    private int hp;
    private int hpMax;
    private int sp;
    private int spMax;
    private int ep;
    private int epMax;
    private long lastFoodTime;//direst use System.currentTimeMillis(),Can use nergal sc update

    public Minion(String name, int hp, int hpMax, int sp, int spMax, int ep, int epMax) {
        this.name = name;
        this.hp = hp;
        this.sp = sp;
        this.ep = ep;
        this.hpMax = hpMax;
        this.spMax = spMax;
        this.epMax = epMax;
    }

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

    public int getHpMax() {
        return hpMax;
    }

    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }

    public int getSpMax() {
        return spMax;
    }

    public void setSpMax(int spMax) {
        this.spMax = spMax;
    }

    public int getEpMax() {
        return epMax;
    }

    public void setEpMax(int epMax) {
        this.epMax = epMax;
    }

    public long getLastFoodTime() {
        return lastFoodTime;
    }

    public void setLastFoodTime(long lastFoodTime) {
        this.lastFoodTime = lastFoodTime;
    }

    @Override
    public String toString() {
        return "Minion{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", hpMax=" + hpMax +
                ", sp=" + sp +
                ", spMax=" + spMax +
                ", ep=" + ep +
                ", epMax=" + epMax +
                ", lastFoodTime=" + ((System.currentTimeMillis() - lastFoodTime) / 1000) +
                '}';
    }
}
