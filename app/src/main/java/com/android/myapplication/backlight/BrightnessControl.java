package com.android.myapplication.backlight;

import android.content.Context;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;

/**
 * Created by liurenyi on 2018/6/12.
 */

public class BrightnessControl {

    public static final String TAG_G = "liu";
    public static final String TAG = "BrightnessControl";

    private static PowerManager powerManager;
    private static PowerManager.WakeLock newWakeLock;

    /**
     * PARTIAL_WAKE_LOCK:保持CPU 运转，屏幕和键盘灯有可能是关闭的。
     * SCREEN_DIM_WAKE_LOCK：保持CPU 运转，允许保持屏幕显示但有可能是灰的，允许关闭键盘灯
     * SCREEN_BRIGHT_WAKE_LOCK：保持CPU 运转，允许保持屏幕高亮显示，允许关闭键盘灯
     * FULL_WAKE_LOCK：保持CPU 运转，保持屏幕高亮显示，键盘灯也保持亮度
     * ACQUIRE_CAUSES_WAKEUP：强制使屏幕亮起，这种锁主要针对一些必须通知用户的操作.
     * ON_AFTER_RELEASE：当锁被释放时，保持屏幕亮起一段时间
     */
    public static void setScreenOff(Context mContext) {
        powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            boolean screenOn = powerManager.isScreenOn();
            Log.i("liu", "screenOn is " + screenOn);
            if (screenOn) {
                newWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "liu");
                newWakeLock.acquire();
                newWakeLock.release();

            }
        }
    }



}
