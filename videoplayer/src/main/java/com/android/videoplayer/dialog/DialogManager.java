package com.android.videoplayer.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.videoplayer.R;

/**
 * Created by liurenyi on 2018/7/11.
 */

public class DialogManager {

    public Context context;

    public DialogManager(Context context) {
        this.context = context;
    }

    /**
     * 弹出提示框，提醒用户在需要做些什么操作
     */
    public void createDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.alert_title));
        dialog.setMessage(context.getResources().getString(R.string.alert_message));
        dialog.setPositiveButton(context.getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.setCancelable(true);
            }
        });
        dialog.show();
    }
}
