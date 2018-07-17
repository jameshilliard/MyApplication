package com.android.myapplication.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.util.Set;

/**
 * Created by liurenyi on 2018/7/12.
 */

public class BluetoothManager {

    private Context context;
    private BluetoothAdapter adapter;

    public BluetoothManager(Context context) {
        this.context = context;
    }

    /**
     * 蓝牙是否连接
     *
     * @return
     */
    public boolean bluetoothIsConnection() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled()) {
            int a2dp = adapter.getProfileConnectionState(BluetoothProfile.A2DP);//可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int headset = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);//蓝牙头戴式耳机，支持语音输入输出
            int health = adapter.getProfileConnectionState(BluetoothProfile.HEALTH);//蓝牙穿戴式设备
            int GATT = adapter.getProfileConnectionState(BluetoothProfile.GATT);
            Log.e("liu", "a2dp=" + a2dp + ",headset=" + headset + ",health=" + health + ",GATT=" + GATT);
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                Log.i("liu", "a2dp is connection!");
                return true;
            } else if (headset == BluetoothProfile.HEADSET) {
                Log.i("liu", "headset is connection!");
                return true;
            } else if (health == BluetoothProfile.HEALTH) {
                Log.i("liu", "health is connection!");
                return true;
            }
            Log.i("liu", "bluetooth is disconnection!");
        }
        return false;
    }

    public void bluetoothState() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
        for (BluetoothDevice device :bondedDevices) {
            int state = device.getBondState();
            Log.i("liu","state = " + state);
        }
    }

}
