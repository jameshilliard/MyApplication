package com.android.myapplication.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.myapplication.R;
import com.android.myapplication.util.AvcEncoder;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

public class RecordH264Activity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private SurfaceView surfaceview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Parameters parameters;
    int width = 1280;
    int height = 720;
    int framerate = 30;
    int biterate = 8500*1000;
    private static int yuvqueuesize = 10;
    public static ArrayBlockingQueue<byte[]> YUVQueue = new ArrayBlockingQueue<byte[]>(yuvqueuesize);
    private AvcEncoder avcCodec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        surfaceview = (SurfaceView)findViewById(R.id.surfaceview);
        surfaceHolder = surfaceview.getHolder();
        surfaceHolder.addCallback(this);
        SupportAvcCodec();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = getBackCamera();
        startcamera(camera);
        avcCodec = new AvcEncoder(width,height,framerate,biterate);
        avcCodec.StartEncoderThread();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null != camera) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
            avcCodec.StopThread();
        }
    }


    @Override
    public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
        // TODO Auto-generated method stub
        putYUVData(data,data.length);
    }

    public void putYUVData(byte[] buffer, int length) {
        if (YUVQueue.size() >= 10) {
            YUVQueue.poll();
        }
        YUVQueue.add(buffer);
    }

    private boolean SupportAvcCodec(){
        if(Build.VERSION.SDK_INT>=18){
            for(int j = MediaCodecList.getCodecCount() - 1; j >= 0; j--){
                MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(j);

                String[] types = codecInfo.getSupportedTypes();
                for (int i = 0; i < types.length; i++) {
                    if (types[i].equalsIgnoreCase("video/avc")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void startcamera(Camera mCamera){
        if(mCamera != null){
            try {
                mCamera.setPreviewCallback(this);
                mCamera.setDisplayOrientation(90);
                if(parameters == null){
                    parameters = mCamera.getParameters();
                }
                parameters = mCamera.getParameters();
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(width, height);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(surfaceHolder);
                mCamera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Camera getBackCamera() {
        Camera c = null;
        try {
            c = Camera.open(0); // attempt to get a Camera instance
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
