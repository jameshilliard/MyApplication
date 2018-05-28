package com.android.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.android.myapplication.ui.PortraitActivity;
import com.android.myapplication.ui.RecordVideoActivity;

import java.io.File;
import java.util.Calendar;

/**
 * Created by liurenyi on 2018/5/23.
 */

public class Method {

    private Intent intent;

    public void startOtherUI(Context mContext, int type) {
        intent = new Intent();
        if (type == 1) {
            intent.setClass(mContext, PortraitActivity.class);
            mContext.startActivity(intent);
        } else if (type == 2) {
            intent.setClass(mContext, RecordVideoActivity.class);
            mContext.startActivity(intent);
        }

    }

    /**
     * 获取系统时间
     *
     * @return
     */
    public static String getDate() {
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);           // 获取年份
        int month = ca.get(Calendar.MONTH);         // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE);       // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);       // 秒
        String months = month >= 9 ? (month + 1) + "" : "0" + (month + 1);
        String days = day >= 10 ? day + "" : "0" + day;
        String hours = hour >= 10 ? hour + "" : "0" + hour;
        String minutes = minute >= 10 ? minute + "" : "0" + minute;
        String seconds = second >= 10 ? second + "" : "0" + second;
        String date = "" + year + months + days + hours + minutes + seconds;
        return date;
    }

    /**
     * 获取SD path
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

}
