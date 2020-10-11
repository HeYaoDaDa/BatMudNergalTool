package nergaltool.utils;

/**
 * Util text class
 */
public class TextUtil {
    // To use colors the general form is such:
    // BOLD+BACKGROUNDCOLOR+TEXTCOLOR+"Some text"+RESET
    // If you want bold text, BOLD must be first. BACKGROUNDCOLOR is also optional
    // HIGHLY recommended you end your string with RESET
    public static final String RESET = "\u001b[0m";
    public static final String BOLD = "\u001b[1m";
    public static final String BLACK = "\u001b[30m";
    public static final String RED = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE = "\u001b[34m";
    public static final String MAGENTA = "\u001b[35m";
    public static final String CYAN = "\u001b[36m";
    public static final String WHITE = "\u001b[37m";
    public static final String BGBLACK = "\u001b[40m";
    public static final String BGRED = "\u001b[41m";
    public static final String BGGREEN = "\u001b[42m";
    public static final String BGYELLOW = "\u001b[43m";
    public static final String BGBLUE = "\u001b[44m";
    public static final String BGMAGENTA = "\u001b[45m";
    public static final String BGCYAN = "\u001b[46m";
    public static final String BGWHITE = "\u001b[47m";

    /**
     * format color string
     * @param content need format string
     * @param color format color
     * @return formatted string
     */
    public static String colorText(String content,String color){
        return BOLD+color+content+RESET;
    }

    public static long formatTime(long time){
        return (System.currentTimeMillis()-time)/1000;
    }
}
