package nergaltool.bean;

/**
 * minion information
 */
public class Minion {
    private String name;
    private int hp;
    private int sp;
    private int ep;
    private int hpMax;
    private int spMax;
    private int epMax;
    private long lastFoodTime;//direst use System.currentTimeMillis(),Can use nergal sc update

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
}
