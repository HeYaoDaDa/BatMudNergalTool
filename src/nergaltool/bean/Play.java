package nergaltool.bean;

/**
 * Play information
 */
public class Play {
    private static final Play play = new Play();
    private String name;
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
}
