package com.android.myapplication.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

public class WifiStateService extends Service {

    private Context mContext;
    private String TAG = "WifiStateService";
    private IntentFilter wifiFilter;

    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";

    public static boolean isRunning = false;

    public WifiStateService() {
        this.mContext = WifiStateService.this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        Log.i(TAG, "WifiStateService is onCreate");
        initWifiState();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "WifiStateService is onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "WifiStateService is onDestroy");
        mContext.unregisterReceiver(wifiReceiver);
        isRunning = false;
    }

    private void initWifiState() {
        wifiFilter = new IntentFilter();
        wifiFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        wifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //监听wifi的打开与关闭，与wifi的连接无关
        wifiFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        wifiFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(wifiReceiver, wifiFilter);
    }

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WIFI_STATE_CHANGED_ACTION.equals(action)) {
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.i(TAG, "wifiState:" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLING: // WIFI_STATE_DISABLING --> 0
                        break;
                    case WifiManager.WIFI_STATE_DISABLED: // WIFI_STATE_DISABLED --> 1
                        break;
                    case WifiManager.WIFI_STATE_ENABLING: // WIFI_STATE_ENABLING --> 2
                        break;
                    case WifiManager.WIFI_STATE_ENABLED: // WIFI_STATE_ENABLED --> 3
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN: // WIFI_STATE_UNKNOWN --> 4
                        break;
                    default:
                        break;
                }
            } else if (NETWORK_STATE_CHANGED_ACTION.equals(action)) { // 监听wifi的连接状态即是否连上了一个有效无线路由
                Parcelable parcelableExtra = intent
                        .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (null != parcelableExtra) {
                    // 获取联网状态的NetWorkInfo对象
                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                    //获取的State对象则代表着连接成功与否等状态
                    NetworkInfo.State state = networkInfo.getState();
                    //判断网络是否已经连接
                    boolean isConnected = state == NetworkInfo.State.CONNECTED;
                    Log.e(TAG, "isConnected:" + isConnected);
                }
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) { // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
                //获取联网状态的NetworkInfo对象
                NetworkInfo info = intent
                        .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI
                                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            Log.i(TAG, getConnectionType(info.getType()) + "连上");
                        }
                    } else {
                        Log.i(TAG, getConnectionType(info.getType()) + "断开");
                    }
                }
            }
        }
    };

    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }
}
