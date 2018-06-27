package com.android.myapplication;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
//import android.media.AudioManagerEx;

import java.util.ArrayList;

/**
 * 用来做为H6 打开默认声音通道的测试service
 */
public class AudioControlService extends Service {

    public static final String TAG = "AudioControlService";

    private IntentFilter filter;
    private Context context;
    //private AudioManagerEx mAudioManagerEx;
    private ArrayList<String> mChannels;
    private ArrayList<String> defaultChannels;

    public AudioControlService() {
        context = AudioControlService.this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "===onCreate===");
        filter = new IntentFilter();
        filter.addAction("com.android.audio.all.open");
        context.registerReceiver(audioReceiver, filter);
        //mAudioManagerEx = new AudioManagerEx(context);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "===onStartCommand===");
        setAudioChannels(1);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "===onDestroy===");
        context.unregisterReceiver(audioReceiver);
    }

    private BroadcastReceiver audioReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "action:" + intent.getAction());
        }
    };

    /**
     * Type equals 1, indicating that the default channels is set. If it is equal to 2, the current channels is set.
     *
     * @param type
     */
    private void setAudioChannels(int type) {
        // Get the current selected channels
        // mChannels = mAudioManagerEx.getActiveAudioDevices(AudioManagerEx.AUDIO_OUTPUT_ACTIVE);
        // Get the default channels
        //defaultChannels = mAudioManagerEx.getAudioDevices(AudioManagerEx.AUDIO_OUTPUT_TYPE);
        if (type == 1) {
            //  mAudioManagerEx.setAudioDeviceActive(defaultChannels, AudioManagerEx.AUDIO_OUTPUT_ACTIVE);
        } else if (type == 2) {
            //mAudioManagerEx.setAudioDeviceActive(mChannels, AudioManagerEx.AUDIO_OUTPUT_ACTIVE);
        }
    }
}
