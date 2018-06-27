package com.android.myapplication.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaRecorder;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.myapplication.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    public Context mContext = TestActivity.this;

    public static final String BACKLIGHT_STATE = "/sys/class/disp/disp/attr/backlight";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.screen.on");
        filter.addAction("com.android.screen.off");
        mContext.registerReceiver(screenReceiver, filter);

        IntentFilter soundFilter = new IntentFilter();
        soundFilter.addAction("com.android.default.sound.mode");
        mContext.registerReceiver(soundReceiver, soundFilter);

        IntentFilter tpFilter = new IntentFilter();
        tpFilter.addAction("com.android.tp.on");
        tpFilter.addAction("com.android.tp.off");
        mContext.registerReceiver(changeTPStateReceiver, tpFilter);

    }

    /**
     * 控制背光开启关闭，0为关闭，1为开启
     *
     * @param i
     */
    private void setBacklightSwitch(int i) {
        if (!(new File(BACKLIGHT_STATE)).exists()) {
            Log.e("liurenyi", BACKLIGHT_STATE + " Non-existent.");
            return;
        }
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(BACKLIGHT_STATE));
            bufferedWriter.write(i == 0 ? "0" : "1");
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            Log.e("liurenyi", "can not write the " + BACKLIGHT_STATE);
            Log.e("liurenyi", e.toString());
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {

                }
            }
        }
        Settings.System.putInt(mContext.getContentResolver(), "backlight", i);
        Intent backlightIntent = new Intent("com.android.backlight.change");
        mContext.sendBroadcast(backlightIntent);
        Intent screenOnIntent = new Intent("com.android.screen.on");
        Intent screenOffIntent = new Intent("com.android.screen.off");
        mContext.sendBroadcast(i == 0 ? screenOffIntent : screenOnIntent);
    }

    /**
     * 获取背光文件节点此时状态
     *
     * @return 状态值
     */
    private String getBacklightState() {
        String state = "";
        if ((new File(BACKLIGHT_STATE)).exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(BACKLIGHT_STATE));
                state = bufferedReader.readLine();
            } catch (FileNotFoundException e) {
                Log.e("liurenyi", "FileNotFoundException: " + e.toString());
            } catch (IOException e) {
                Log.e("liurenyi", "IOException: " + e.toString());
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {

                }
            }
            Settings.System.putInt(mContext.getContentResolver(), "backlight", "1".equals(state) ? 1 : 0);
            Settings.System.getInt(mContext.getContentResolver(), "backlight", 0);
            return state;
        } else {
            Log.e("liurenyi", "file does not exist!");
        }
        return state;
    }

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("liu", "action-->" + action);
        }
    };

    private BroadcastReceiver controlBacklightReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("liu", "<PhoneWindowManager> --> action:" + action);
            String backlight = (String) intent.getExtras().get("backlight");
            setBacklightSwitch(backlight.equals("1") ? 1 : 0);
        }
    };

    /**
     * 监听设置声音的广播
     */
    private BroadcastReceiver soundReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("liu", "<soundReceiver> --> action:" + action);
        }
    };

    /**
     * Monitor the change of TP value
     */
    private BroadcastReceiver changeTPStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.android.tp.on")) {

            } else if (action.equals("com.android.tp.off")) {

            }
        }
    };

}
