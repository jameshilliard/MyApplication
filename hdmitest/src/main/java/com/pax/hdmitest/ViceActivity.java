package com.pax.hdmitest;

import android.app.Activity;
import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;


/**
 * 1.  显示logo，显示三原色
 * 2.  播放视频
 * 3.  显示字符，全屏显示字符
 * 4.  显示触摸报点
 * 5.  实现画图或签名demo
 * 6.  实现拖动，点击控件
 */
public class ViceActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "liu-ViceActivity";
    private Presentation mCurrentPresentation;
    private MediaRouter mMediaRouter;
    private DisplayManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vice);

        findViewById(R.id.btn_sample).setOnClickListener(this);
        findViewById(R.id.btn_color).setOnClickListener(this);
        findViewById(R.id.btn_string).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        findViewById(R.id.btn_touch).setOnClickListener(this);
        findViewById(R.id.btn_draw).setOnClickListener(this);
        findViewById(R.id.btn_click).setOnClickListener(this);
        setDisplayScreen(0);
    }

    private void setDisplayScreen(int type) {
        Log.i(TAG, "<setDisplayScreen> -->");
        manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        if (manager != null) {
            Display[] displays = manager.getDisplays();
            if (type == 1) {
                mCurrentPresentation = new ColorPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 2) {
                mCurrentPresentation = new StringPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 3) {
                mCurrentPresentation = new VideoPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 4) {
                mCurrentPresentation = new TouchPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 5) {
                mCurrentPresentation = new DrawPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 6) {
                mCurrentPresentation = new ButtonPresentation(ViceActivity.this, displays[1]);
                mCurrentPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mCurrentPresentation.show();
            } else if (type == 0) {

            }
        } else {
            Log.i(TAG, "<setDisplayScreen> --> DisplayManager is null!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        if (manager == null) {
            manager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurrentPresentation != null) {
            mCurrentPresentation.dismiss();
            mCurrentPresentation = null;
        }

        Log.e(TAG, "onDestroy");
    }

    @Override
    public void onClick(View v) {
        int type = 0;
        switch (v.getId()) {
            case R.id.btn_sample:
                type = 0;
                releasePresentation();
                setDisplayScreen(type);
                break;
            case R.id.btn_color:
                type = 1;
                releasePresentation();
                setDisplayScreen(type);
                break;
            case R.id.btn_string:
                releasePresentation();
                type = 2;
                setDisplayScreen(type);
                break;
            case R.id.btn_video:
                releasePresentation();
                type = 3;
                setDisplayScreen(type);
                break;
            case R.id.btn_touch:
                type = 4;
                releasePresentation();
                setDisplayScreen(type);
                break;
            case R.id.btn_draw:
                type = 5;
                releasePresentation();
                setDisplayScreen(type);
                break;
            case R.id.btn_click:
                type = 6;
                releasePresentation();
                setDisplayScreen(type);
                break;
        }
    }

    private void releasePresentation() {
        if (mCurrentPresentation != null) {
            mCurrentPresentation.dismiss();
            mCurrentPresentation = null;
        }
    }

    private final MediaRouter.SimpleCallback mediaRouterCallback = new MediaRouter.SimpleCallback() {
        @Override
        public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            super.onRouteSelected(router, type, info);
            mMediaRouter = router;
            Log.e(TAG, "onRouteSelected");
        }

        @Override
        public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            super.onRouteUnselected(router, type, info);
            Log.e(TAG, "onRouteUnselected");
        }

        @Override
        public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info) {
            super.onRoutePresentationDisplayChanged(router, info);
            Log.e(TAG, "onRoutePresentationDisplayChanged");
        }
    };
}
