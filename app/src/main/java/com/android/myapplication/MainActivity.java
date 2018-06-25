package com.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.myapplication.backlight.BrightnessControl;
import com.android.myapplication.service.WifiStateService;
import com.android.myapplication.util.Method;
import com.android.myapplication.util.Utils;

public class MainActivity extends Activity implements View.OnClickListener {

    private Context mContext = MainActivity.this;

    private Button btnScreenDirection;
    private Button btnRecordVideo, btnPackageInfo;
    private Button btnPrepare;
    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        method = new Method();
    }

    private void initView() {
        btnScreenDirection = (Button) this.findViewById(R.id.btn_screen_direction);
        btnRecordVideo = (Button) this.findViewById(R.id.btn_record_video);
        btnPackageInfo = (Button) this.findViewById(R.id.btn_package_info);
        btnPrepare = (Button) this.findViewById(R.id.btn_prepare);

        btnScreenDirection.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
        btnPackageInfo.setOnClickListener(this);
        btnPrepare.setOnClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_screen_direction:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_ONE);
                finish();
                break;
            case R.id.btn_record_video:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_TWO);
                //finish();
                break;
            case R.id.btn_package_info:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_THREE);
                break;
            case R.id.btn_prepare:
//                BrightnessControl.setScreenOff(mContext);
//                Window window = getWindow();
//                WindowManager.LayoutParams layoutParams = window.getAttributes();
//                layoutParams.screenBrightness = 0f;
//                window.setAttributes(layoutParams);
//                //开启顶部下拉菜单栏
//                Intent intent = new Intent("com.statusbar.SHOW_OR_HIDE");
//                intent.putExtra("mode", 0x00000000);
//                mContext.sendBroadcast(intent);
//                Intent intent1 = new Intent("com.android.backlight.control");
//                intent1.putExtra("backlight", "0");
//                mContext.sendBroadcast(intent1);
                Intent serviceIntent = new Intent(mContext, WifiStateService.class);
                mContext.startService(serviceIntent);
                break;
            default:
                break;
        }
    }
}
