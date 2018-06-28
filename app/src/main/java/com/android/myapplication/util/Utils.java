package com.android.myapplication.util;

import android.content.Context;

/**
 * Created by liurenyi on 2018/5/23.
 */

public class Utils {

    public static final int KEY_ACTIVITY_CODE_ONE = 1;
    public static final int KEY_ACTIVITY_CODE_TWO = 2;
    public static final int KEY_ACTIVITY_CODE_THREE = 3;

    public static final int KEY_HANDLE_MSG_CODE = 4;

    /**
     * Obtaining character resources
     * @param context
     * @param resId
     * @return
     */
    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }
}
