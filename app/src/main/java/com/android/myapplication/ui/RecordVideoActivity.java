package com.android.myapplication.ui;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myapplication.R;
import com.android.myapplication.util.Method;

import java.io.File;
import java.io.IOException;

public class RecordVideoActivity extends AppCompatActivity implements View.OnClickListener,SurfaceHolder.Callback {

    public static final String TAG = "liu-RecordVideoActivity";

    public Context mContext = RecordVideoActivity.this;

    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Button btnStartStop;
    private Button btnPlayVideo;
    private TextView text;
    private ImageView imageview;
    private boolean isPlay = false;
    private boolean isRecordVideo = false;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private Camera camera;
    private String path = null;
    private int timer = 0;
    private byte[] preBuffer = new byte[640 * 480 * 3 / 2];

    private CameraPreviewCallback cameraPreviewCallback;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer++;
            text.setText(timer + "");
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //此方法已经失效
        setContentView(R.layout.activity_record_video);
        initView();
        mediaPlayer = new MediaPlayer();
    }

    private void initView() {
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceview);
        btnStartStop = (Button) this.findViewById(R.id.btnStartStop);
        btnPlayVideo = (Button) this.findViewById(R.id.btnPlayVideo);
        text = (TextView) this.findViewById(R.id.text);
        imageview = (ImageView) this.findViewById(R.id.imageview);

        btnStartStop.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);
        surfaceHolder = mSurfaceView.getHolder();
        // 预览，打开摄像头
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartStop:
                startRecordVideo();
                break;
            case R.id.btnPlayVideo:
                break;
            default:
                break;
        }
    }

    // 开始录制
    private void startRecordVideo() {
        // 是否正在播放视频
        stopPlayVideo();
        // 是否已经在录制
        if (!isRecordVideo) {
            imageview.setVisibility(View.GONE);
            if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
            }

            if (camera == null) {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK); // 打开后置摄像头
            }

            cameraPreviewCallback = new CameraPreviewCallback();
            camera.setPreviewCallback(cameraPreviewCallback);
            camera.addCallbackBuffer(preBuffer);
            camera.setPreviewCallbackWithBuffer(cameraPreviewCallback);

            if (camera != null) {
                camera.setDisplayOrientation(90);
                camera.unlock();
                mediaRecorder.setCamera(camera);
            } else {
                Log.e(TAG, "camera is null , open the camera fail");
            }
            // 这两项需要放在setOutputFormat之前
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // 这两项需要放在setOutputFormat之后
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

            mediaRecorder.setVideoSize(640, 480);
            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
            mediaRecorder.setOrientationHint(90);

            //设置记录会话的最大持续时间（毫秒）
            mediaRecorder.setMaxDuration(30 * 1000);
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());

            path = Method.getSDPath();
            if (path != null) {
                File file = new File(path + "/recordtest");
                if (!file.exists()) {
                    file.mkdir();
                }
                path = file + "/" + Method.getDate() + ".mp4";
                mediaRecorder.setOutputFile(path);
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    isRecordVideo = true;
                    btnStartStop.setText(mContext.getResources().getString(R.string.app_stop_record_video));
                    handler.postDelayed(runnable, 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }
            }
//            doOpenCamera();
//            initCamera();
        } else {
            stopRecordVideo();
        }
    }

    // 停止录制
    private void stopRecordVideo() {
        if (isRecordVideo) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            if (camera != null) {
                camera.release();
                camera = null;
            }
            isRecordVideo = false;
            btnStartStop.setText(mContext.getResources().getString(R.string.app_start_record_video));
            handler.removeCallbacks(runnable);
        }
    }

    // 停止播放视频
    private void stopPlayVideo() {
        if (isPlay) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecordVideo();
        stopPlayVideo();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        doOpenCamera();
        initCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        imageview.setVisibility(View.GONE);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "===surfaceDestroyed()===");
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    class CameraPreviewCallback implements Camera.PreviewCallback {

        public CameraPreviewCallback() {
            Log.i(TAG, "===CameraPreviewCallback===");
        }

        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            Log.i(TAG,"===onPreviewFrame===");
            camera.addCallbackBuffer(bytes);
            int bytesToInt = bytesToInt2(bytes, 1);
            Log.e("liu", "bytesToInt: " + bytesToInt);
        }
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在前，高位在后)的顺序，和和intToBytes（）配套使用
     *
     * @param src    byte数组
     * @param offset 从数组的第offset位开始
     * @return int数值
     */
    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16)
                | ((src[offset + 3] & 0xFF) << 24));
        return value;
    }

    /**
     * byte数组中取int数值，本方法适用于(低位在后，高位在前)的顺序。和intToBytes2（）配套使用
     */
    public static int bytesToInt2(byte[] src, int offset) {
        int value;
        value = (int) (((src[offset] & 0xFF) << 24)
                | ((src[offset + 1] & 0xFF) << 16)
                | ((src[offset + 2] & 0xFF) << 8)
                | (src[offset + 3] & 0xFF));
        return value;
    }

    //去打开摄像头
    public void doOpenCamera() {
        Log.i(TAG, "Camera open....");
        int numCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < numCameras; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                camera = Camera.open(i);
                break;
            }
        }
        if (camera == null) {
            Log.d(TAG, "No front-facing camera found; opening default");
            camera = Camera.open();    // opens first back-facing camera
        }
        if (camera == null) {
            throw new RuntimeException("Unable to open camera");
        }
        Log.i(TAG, "Camera open over....");
    }

    private void initCamera() {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setFlashMode("off");
        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        // 场景模式：夜晚，沙滩，阳光等
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        parameters.setPreviewSize(480, 640);
        parameters.setPreviewFpsRange(30000, 30000);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);

            cameraPreviewCallback = new CameraPreviewCallback();
            camera.addCallbackBuffer(preBuffer);
            camera.setPreviewCallback(cameraPreviewCallback);
            camera.setParameters(parameters);
            camera.startPreview();//该方法只有相机开启后才能调用
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }
}
