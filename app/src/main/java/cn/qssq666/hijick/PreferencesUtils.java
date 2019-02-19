package cn.qssq666.hijick;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import eu.chainfire.libsuperuser.BuildConfig;

public class PreferencesUtils {
    public static String getPreferences(Context context, String key
    ) {
        return context.getSharedPreferences("test", Context.MODE_PRIVATE).getString(key, "");
    }

    public static void save(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences("test", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
}