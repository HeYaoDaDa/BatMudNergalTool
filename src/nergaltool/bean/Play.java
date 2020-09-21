package nergaltool.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Play information
 */
public class Play {
    //Singleton,instance
    private static final Play play = new Play();
    private String name;
    private static List<Minion> minionList = new ArrayList<>();
    private int hp;
    private int sp;
    private int ep;
    private int hpMax;
    private int spMax;
    private int epMax;
    private int vitae;
    private int potentia;
    private long lastHibernatingTime;//direst use System.currentTimeMillis(),follow same
    private long lastSleepTime;

    //Singleton,private constructor
    private Play(){}

    //Singleton,get single instance
    public static Play getInstance(){
        return play;
    }

    public static Play getPlay() {
        return play;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Minion> getMinionList() {
        return minionList;
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

    public long getLastHibernatingTime() {
        return lastHibernatingTime;
    }

    public void setLastHibernatingTime(long lastHibernatingTime) {
        this.lastHibernatingTime = lastHibernatingTime;
    }

    public long getLastSleepTime() {
        return lastSleepTime;
    }

    public void setLastSleepTime(long lastSleepTime) {
        this.lastSleepTime = lastSleepTime;
    }
}
