package cn.qssq666.hijick;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CheckUtil {
    public static boolean hasRoot() {
        List<File> list = new ArrayList<>();
        list.add(new File("/system/bin/su"));
        list.add(new File("/system/bin"));
        list.add(new File("/system/xbin/su"));
        list.add(new File("/su/bin/"));
        list.add(new File("/system/sbin/su"));
        list.add(new File("/sbin/su"));
        list.add(new File("/vendor/bin/su"));
        for (File file : list) {
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }
}
