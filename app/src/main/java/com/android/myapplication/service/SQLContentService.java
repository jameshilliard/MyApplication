package com.android.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class SQLContentService extends Service {

    public static final String TAG = "SQLContentService";

    private Handler handler = new Handler();

    public SQLContentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "<onCreate>");
        registerObserver();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "<onStartCommand>");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    /**
     * 内容监听者，如果监听的内容发生改变，触发此方法
     */
    private ContentObserver contentObserver = new ContentObserver(handler) {

        @Override
        public void onChange(boolean selfChange) {
            int anInt = Settings.System.getInt(getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
            Log.i(TAG, "<onChange> -- selfChange:" + selfChange + " -- anInt: " + anInt);
            super.onChange(selfChange);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Log.i(TAG, "<onChange> -- selfChange:" + selfChange + " -- uri:" + uri);
            super.onChange(selfChange, uri);
        }
    };

    /**
     * 注册内容监听者，如果数据库某一个数据发生改变，则马上上报消息
     */
    private void registerObserver() {
        getContentResolver().registerContentObserver(Settings.System.getUriFor(Settings.System.AIRPLANE_MODE_ON), true, contentObserver);
    }
}
