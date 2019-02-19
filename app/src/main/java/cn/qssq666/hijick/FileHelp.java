package cn.qssq666.hijick;

import android.os.Environment;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class FileHelp {
    private static String SdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String Sd_Dir = new StringBuilder(String.valueOf(String.valueOf(SdPath))).append("/guangda/").toString();
    public static String fileName = "guangda.txt";

    public static String msToDate(long arg6) throws Exception {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(arg6));
    }
}