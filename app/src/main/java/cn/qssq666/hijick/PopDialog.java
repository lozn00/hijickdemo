package cn.qssq666.hijick;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class PopDialog {
    public static void popDia(Context arg5) {
        Builder v1 = new Builder(arg5);
        v1.setMessage("--破解二次打包成功--\n--请您做好app防护！--").setCancelable(false).setPositiveButton("确定", new OnClickListener() {
            public void onClick(DialogInterface arg1, int arg2) {
                arg1.cancel();
            }
        }).setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface arg1, int arg2) {
                arg1.cancel();
            }
        });
        v1.create().show();
    }
}