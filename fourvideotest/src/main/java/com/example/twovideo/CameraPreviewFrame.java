package com.example.twovideo;

import android.graphics.Canvas;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by liurenyi on 2018/6/6.
 */

public class CameraPreviewFrame implements Camera.PreviewCallback {

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Parameters ps = camera.getParameters();
        int[] imgs = new int[ps.getPreviewSize().width * ps.getPreviewSize().height];
        try {
            Log.i("liu", ps.getPreviewSize().width + "=====" + ps.getPreviewSize().height);
        } catch (Exception e) {
            Log.i("liu", "Exception new bmp" + e.toString());
            return;
        }
        camera.addCallbackBuffer(bytes);
        Log.i("liu", "回调");
    }
}
