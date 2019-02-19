package cn.qssq666.hijick;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qssq666.hijick.R;

import java.util.List;

import eu.chainfire.libsuperuser.Shell.SU;

public class MainActivity extends Activity {
    private EditText edittext;
    private Intent logObserverIntent = null;
    private String TAG = "MainActivity";
    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            tvScanResult.setText("" + msg.obj);
            return false;
        }
    });
    private TextView tvScanResult;
    private boolean runnning;

    public void publishStatus(String str) {
        handler.obtainMessage(0, str).sendToTarget();
    }

    protected void onCreate(Bundle arg4) {
        super.onCreate(arg4);
        setContentView(R.layout.activity_main);
        tvScanResult = (TextView) findViewById(R.id.tv_scanresult);
        this.edittext = (EditText) findViewById(R.id.edittext);
        final EditText evTime = (EditText) findViewById(R.id.ev_time);
        final CheckBox cbCheckSuccStopScan = (CheckBox) findViewById(R.id.hijick_stop_scan);
        ((TextView) findViewById(R.id.tv_hass_root)).setText("是否root:" + CheckUtil.hasRoot());
        findViewById(R.id.btn_stop).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                runnning = false;
            }
        });
        String activity = PreferencesUtils.getPreferences(this, "activity");
        this.edittext.setText(activity);

        ((Button) findViewById(R.id.button)).setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                final String keyword = MainActivity.this.edittext.getText().toString();
                PreferencesUtils.save(MainActivity.this, "activity", keyword);
                final boolean foundStop = cbCheckSuccStopScan.isChecked();
                if (runnning) {
                    Toast.makeText(MainActivity.this, "has running!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(keyword)) {
                    Toast.makeText(MainActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String checkTime = evTime.getText().toString();
                new Thread(new Runnable() {
                    public void run() {
                        runnning = true;
                        while (true) {
                            if (!runnning) {
                                Log.w(TAG, "停止扫描...");
                                return;
                            }
                            try {
                                publishStatus("开始执行...");
                                String str = "";
                                if (VERSION.SDK_INT >= 26) {
                                    str = "dumpsys activity|grep  \"Hist.*#0:.*}\"";
                                } else {
                                    str = "dumpsys activity  |grep \"mFocusedActivity\" ";
                                }
                                Log.w(TAG, "执行dumpys 完毕!");
                                List<String> run = SU.run(str);
                                StringBuffer sb = new StringBuffer();
                                for (int i = 0; i < (run.size() >= 2 ? 2 : run.size()); i++) {
                                    sb.append(run.get(i) + "\n");
                                }
                                publishStatus("扫描结果\n:" + sb.toString());
                                String topActivity = (String) run.get(0);
                                if (topActivity.contains("/.")) {
                                    topActivity = topActivity.replace("/.", ".");
                                } else {
                                }
                      /*          for (int i = 0; i < run.size(); i++) {
                                    String currentLine = run.get(i);
                                    Log.w(TAG, (1 + i) + "." + currentLine);
                                }*/
                                try {
                                    Thread.sleep(Long.parseLong(checkTime));

                                } catch (Exception e) {

                                    Thread.sleep(600);
                                }
                                if (topActivity.contains(keyword)) {
                                    Intent intent = new Intent(MainActivity.this, UninstallActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        v2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        v2.addFlags(268435456);
                                    MainActivity.this.startActivity(intent);

                                    if (foundStop) {
                                        runnning = false;
                                        Log.w(TAG, "检测成功,停止");
                                    }


                                }
//                                startLogObserverService();
                            } catch (Throwable e) {
                                e.printStackTrace();
                                runnning = false;
                                publishStatus("执行出错 :" + Log.getStackTraceString(e));
                                Log.e(TAG, "执行失败,", e);
                                break;
                            }
                        }
                    }
                }).start();
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu arg3) {
        return true;
    }

    private void startLogObserverService() {
        PreferencesUtils.save(this, "activity", this.edittext.getText().toString().trim());
        this.logObserverIntent = new Intent(this, LogObserverService.class);
        startService(this.logObserverIntent);
        Toast.makeText(getApplicationContext(), "开启成功", Toast.LENGTH_LONG).show();
        finish();
    }
}