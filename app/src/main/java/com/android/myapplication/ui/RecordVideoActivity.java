package com.android.myapplication.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.myapplication.R;
import com.android.myapplication.util.Method;
import com.android.myapplication.util.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordVideoActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback {

    public static final String TAG = "RecordVideoActivity";

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Utils.KEY_HANDLE_MSG_CODE:
                    text.setText("0");
                    btnStartStop.setText(Utils.getString(mContext, R.string.app_start_record_video));
                    break;
            }
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer++;
            text.setText(timer + "");
            btnStartStop.setText("停止录制");
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //此方法已经失效
        setContentView(R.layout.activity_record_video);
        mediaPlayer = new MediaPlayer();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecordVideo();
        stopPlayVideo();
        if (camera != null) {
            camera.release();
        }
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!isRecordVideo) {
                    if (mediaRecorder == null) {
                        mediaRecorder = new MediaRecorder();
                    }

                    if (camera != null) {
                        camera.setDisplayOrientation(90);
                        camera.unlock();
                        mediaRecorder.setCamera(camera);
                    } else {
                        Log.e(TAG, "camera is null , open the camera fail");
                    }
                    // 这两项需要放在setOutputFormat之前
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
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
                        File file = new File(path + "/A_record");
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        path = file + "/" + Method.getDate() + ".mp4";
                        mediaRecorder.setOutputFile(path);
                        try {
                            mediaRecorder.prepare();
                            mediaRecorder.start();
                            isRecordVideo = true;
                            handler.postDelayed(runnable, 1000);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                    }
                } else {
                    stopRecordVideo();
                }
            }
        }).start();
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
            handler.removeCallbacks(runnable);
            timer = 0;
            Message message = new Message();
            message.what = Utils.KEY_HANDLE_MSG_CODE;
            handler.sendMessage(message);
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

    class CameraPreviewCallback implements Camera.PreviewCallback {

        public CameraPreviewCallback() {
            Log.i(TAG, "===CameraPreviewCallback===");
        }

        /**
         * 录出来的是yuv420sp 需要转成420p
         *
         * @param bytes
         * @param camera
         */
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;
            byte[] yuv420 = new byte[width * height * 3 / 2];
            YUV420SP2YUV420(bytes,yuv420,width,height);
            camera.addCallbackBuffer(bytes);
        }
    }

    /**
     * open camera
     */
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
            camera = Camera.open();    //opens first back-facing camera
            Log.d(TAG, "No front-facing camera found; opening default");
        }
        if (camera == null) {
            throw new RuntimeException("Unable to open camera");
        }
    }

    /**
     * Initializing the parameters of the camera
     */
    private void initCamera() {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setFlashMode("off");
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            // 场景模式：夜晚，沙滩，阳光等
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            parameters.setPreviewSize(480, 640);
            //parameters.setPreviewSize(1080, 1920);
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

    private void YUV420SP2YUV420(byte[] yuv420sp, byte[] yuv420, int width, int height) {
        if (yuv420sp == null || yuv420 == null) return;
        int framesize = width * height;
        int i = 0, j = 0;
        //copy y
        for (i = 0; i < framesize; i++) {
            yuv420[i] = yuv420sp[i];
        }
        i = 0;
        for (j = 0; j < framesize / 2; j += 2) {
            yuv420[i + framesize * 5 / 4] = yuv420sp[j + framesize];
            i++;
        }
        i = 0;
        for (j = 1; j < framesize / 2; j += 2) {
            yuv420[i + framesize] = yuv420sp[j + framesize];
            i++;
        }
    }
}
