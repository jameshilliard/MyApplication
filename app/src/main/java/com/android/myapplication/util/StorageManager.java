package com.android.myapplication.util;

import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by liurenyi on 2018/7/14.
 */

public class StorageManager {

    /**
     * 获取存储设备的容量大小
     *
     * @return
     */
    public static long getSDTotalSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            Log.i("liu", "time1-->" + SystemClock.elapsedRealtime());
            long blockSize = stat.getBlockSize();
            Log.i("liu", "time2-->" + SystemClock.elapsedRealtime());
            long totalBlocks = stat.getBlockCount();
            Log.i("liu", "time3-->" + SystemClock.elapsedRealtime());
            return blockSize * totalBlocks;
        } catch (IllegalArgumentException e) {
            Log.e("guo", "getSDTotalSize Exception:" + e.getLocalizedMessage());
            return 0;
        }
    }

    public static long getSDAvailableSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            Log.i("liu", "time4-->" + SystemClock.elapsedRealtime());
            long blockSize = stat.getBlockSize();
            Log.i("liu", "time5-->" + SystemClock.elapsedRealtime());
            long availableBlocks = stat.getAvailableBlocks();
            Log.i("liu", "time6-->" + SystemClock.elapsedRealtime());
            return blockSize * availableBlocks;
        } catch (IllegalArgumentException e) {
            Log.e("guo", "getSDAvailableSize Exception:" + e.getLocalizedMessage());
            return 0;
        }
    }

}
