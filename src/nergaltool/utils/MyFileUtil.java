package nergaltool.utils;

import java.io.File;

public class MyFileUtil {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
            return false;
        }
    }
}
