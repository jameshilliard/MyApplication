package x1.Studio.Core;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.guo.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity implements SurfaceHolder.Callback, PreviewCallback {

    private SurfaceView mSurfaceview = null; // SurfaceView对象：(视图组件)视频显示
    private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder对象：(抽象接口)SurfaceView支持类
    private Camera mCamera = null; // Camera对象，相机预览
    private String TAG = "x_Cameara";

    private int mPreviewWidth = 320, mPreviewHeight = 240;
    private byte[] buf = null;

    private TextView devIdText, pwdText, hostText;
    private Spinner qualitySp;
    private Button sureBtn;

    private String mDevid, mPwd, mHost;

    SharedPreferences parameterData = null;

    private MediaEncoder encoder;
    private OnlineService onlineService;

    private static final String[] quality = {"画质：好", "画质：中", "画质：差",};
    private int mRate = 15000, mResolution = 5, mQuality = 1;
    private ArrayAdapter<String> qualityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initView();
        initPushData();
        initEncode();
        initSurfaceView();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.i(TAG, "SurfaceHolder.Callback：Surface Changed");
        initCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Log.i(TAG, "SurfaceHolder.Callback：surface Created & sccInitJava");
        } catch (Exception ex) {
            if (null != mCamera) {
                mCamera.release();
                mCamera = null;
            }
            Log.i(TAG + "initCamera", ex.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.i(TAG, "SurfaceHolder.Callback：Surface Destroyed");
        if (null != mCamera) {
            mCamera.setPreviewCallback(null); // ！！这个必须在前，不然退出出错
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            System.exit(0);
        }
    }

    private void initView() {

        this.devIdText = (TextView) findViewById(R.id.devidText);
        this.pwdText = (TextView) findViewById(R.id.pwdText);
        this.hostText = (TextView) findViewById(R.id.hostText);
        this.qualitySp = (Spinner) findViewById(R.id.qualitySp);
        this.sureBtn = (Button) findViewById(R.id.sureBtn);

        qualityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quality);
        qualityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualitySp.setAdapter(qualityAdapter);
        qualitySp.setOnItemSelectedListener(new SpinnerSelectedListener());

        getData();

        sureBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveData();
                showRestratView();
            }
        });

    }

    // InitSurfaceView
    private void initSurfaceView() {
        mSurfaceview = (SurfaceView) this.findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceview.getHolder(); // 绑定SurfaceView，取得SurfaceHolder对象
        mSurfaceHolder.addCallback(MainActivity.this); // SurfaceHolder加入回调接口
        // mSurfaceHolder.setFixedSize(176, 144); // 预览大小設置
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 設置顯示器類型，setType必须设置
    }

    /* 【2】【相机预览】 */
    private void initCamera() {// surfaceChanged中调用
        try {
            mCamera = Camera.open(Camera.getNumberOfCameras() - 1);
            mCamera.setPreviewDisplay(mSurfaceHolder);// set the surface to be
            mCamera.setDisplayOrientation(180);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewFormat(ImageFormat.NV21);
            //这两个属性 如果这两个属性设置的和真实手机的不一样时，就会报错
            //parameters.setPictureSize(mPreviewWidth, mPreviewHeight);
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);
            /* 视频流编码处理 */
            // 添加对视频流处理函数
            // 设定配置参数并开启预览
            mCamera.setParameters(parameters); // 将Camera.Parameters设定予Camera
            int size = mPreviewWidth * mPreviewHeight;
            size = size * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())
                    / 8;
            buf = new byte[size]; // class variable
            mCamera.addCallbackBuffer(buf);
            mCamera.setPreviewCallbackWithBuffer(this);
            mCamera.startPreview(); // 打开预览画面
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPushData() {
        onlineService = new OnlineService();
        onlineService.sccInitJava(0, mDevid, mPwd, mHost, 0);
        Log.i("AvcEncoder", "sccInitJava initialized");
    }

    private void initEncode() {
        Log.v("renyi", mPreviewWidth + " " + mPreviewHeight + " " + mRate + " " + mResolution);
        encoder = new MediaEncoder(mPreviewWidth, mPreviewHeight, mRate, onlineService, mResolution);
    }

    private void getData() {
        this.parameterData = getSharedPreferences("parameterData", 0);
        mDevid = parameterData.getString("devid", "");
        mPwd = parameterData.getString("pwd", "");
        mHost = parameterData.getString("host", "");

        mRate = parameterData.getInt("rate", mRate);
        mResolution = parameterData.getInt("resolution", mResolution);
        mPreviewHeight = parameterData.getInt("height", mPreviewHeight);
        mPreviewWidth = parameterData.getInt("width", mPreviewWidth);
        mQuality = parameterData.getInt("quality", mQuality);

        this.qualitySp.setSelection(mQuality);

        devIdText.setText(mDevid);
        pwdText.setText(mPwd);
        hostText.setText(mHost);
    }

    private void saveData() {
        parameterData.edit().putString("devid", devIdText.getText() + "").commit();
        parameterData.edit().putString("pwd", pwdText.getText() + "").commit();
        parameterData.edit().putString("host", hostText.getText() + "").commit();

        parameterData.edit().putInt("width", mPreviewWidth).commit();
        parameterData.edit().putInt("height", mPreviewHeight).commit();
        parameterData.edit().putInt("rate", mRate).commit();
        parameterData.edit().putInt("resolution", mResolution).commit();
        parameterData.edit().putInt("quality", mQuality).commit();
    }

    /**
     * 重启提示对话框
     */
    private void showRestratView() {
        TextView tv = new TextView(MainActivity.this);
        tv.setTextSize(20);
        tv.setWidth(60);

        new AlertDialog.Builder(MainActivity.this).setTitle("设置成功，需要重启程序生效")
                .setView(tv)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    //使用数组形式操作
    class SpinnerSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int postion,
                                   long arg3) {
            switch (postion) {
                case 0:
                    mRate = 1000000;
                    mResolution = 5;
                    mPreviewWidth = 1080;
                    mPreviewHeight = 720;
                    mQuality = 0;
                    break;
                case 1:
                    mRate = 250000;
                    mResolution = 5;
                    mPreviewWidth = 640;
                    mPreviewHeight = 480;
                    mQuality = 1;
                    break;
                case 2:
                    mRate = 100000;
                    mResolution = 3;
                    mPreviewWidth = 320;
                    mPreviewHeight = 240;
                    mQuality = 2;
                    break;
                default:
                    break;
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    /**
     * 获取摄像头数据
     *
     * @param data
     * @param camera
     */
    public synchronized void onPreviewFrame(final byte[] data, Camera camera) {
        int width = camera.getParameters().getPreviewSize().width;
        int height = camera.getParameters().getPreviewSize().height;
        byte[] nv21toNV12 = toYUV420p(data, width, height);
        byte[] bytes = Yuv420spManager.rotateYUV420Degree180(data, width, height); // 图像本来是横屏倒立，选中180，在客户端能正常显示
        // Yuv420spManager.saveBitmap2(data, width, height);
        encoder.onFrame(bytes);
        mCamera.addCallbackBuffer(buf);
    }

    private byte[] swapNv21toNV12(byte[] NV21bytes) {
        byte tmp;
        for (int i = (NV21bytes.length * 2) / 3; i < NV21bytes.length - 1; i++) {
            tmp = NV21bytes[i];
            NV21bytes[i] = NV21bytes[i + 1];
            NV21bytes[i + 1] = tmp;
            i++;
        }
        return NV21bytes;
    }

    /**
     *  YUV420sp数据转为YUV420p的数据
     * @param NV21bytes
     * @param width
     * @param height
     * @return
     */
    private byte[] toYUV420p(byte[] NV21bytes, int width, int height) {
        byte[] YUV420p = new byte[width * height * 3 / 2];
        // 每一帧的大小
        int framesize = width * height;
        int i = 0, j = 0;
        // 这块没问题--Y
        for (i = 0; i < framesize; i++) {
            YUV420p[i] = NV21bytes[i];
        }
        // U
        i = 0;
        for (j = 0; j < framesize / 2; j += 2) {
            YUV420p[i + framesize * 5 / 4] = NV21bytes[j + framesize];
            i++;
        }
        i = 0;
        for (j = 1; j < framesize / 2; j += 2) {
            YUV420p[i + framesize] = NV21bytes[j + framesize];
            i++;
        }
        return YUV420p;
    }

}



