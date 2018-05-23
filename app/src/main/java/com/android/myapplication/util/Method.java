package com.android.myapplication.util;

import android.content.Context;
import android.content.Intent;

import com.android.myapplication.ui.PortraitActivity;

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
        }

    }

}
