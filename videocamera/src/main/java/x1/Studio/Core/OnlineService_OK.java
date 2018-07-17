package x1.Studio.Core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.guo.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class OnlineService_OK extends Activity implements SurfaceHolder.Callback, PreviewCallback {

    private SurfaceView mSurfaceview = null; // SurfaceView����(��ͼ���)��Ƶ��ʾ
    private SurfaceHolder mSurfaceHolder = null; // SurfaceHolder����(����ӿ�)SurfaceView֧����
    private Camera mCamera = null; // Camera�������Ԥ��
    private String TAG = "x_Cameara";
    private boolean bIfPreview;
    private int mPreviewWidth = 320, mPreviewHeight = 240;
    private byte[] buf = null;
    private MediaCodec mediaCodec;
    private int mCount;
    private int FRAME_RATE = 15;
    private int I_FRAME_INTERVAL = 1;
    private String MIME_TYPE = "video/avc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initSurfaceView();

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
        Log.i(TAG, "SurfaceHolder.Callback��Surface Changed");
        // mPreviewHeight = height;
        // mPreviewWidth = width;
        initCamera();
        initCode();

    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        //sccInitJava(0, "mi", "pwd", "shor+++t", 0);
        //System.out.println(Camera.getNumberOfCameras()+"..........");
        mCamera = Camera.open(Camera.getNumberOfCameras() - 1);// ��������ͷ��2.3�汾��֧�ֶ�����ͷ,�贫�������
        try {
            Log.i(TAG, "SurfaceHolder.Callback��surface Created & sccInitJava");
            mCamera.setPreviewDisplay(mSurfaceHolder);// set the surface to be
            // used for live preview


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
        Log.i(TAG, "SurfaceHolder.Callback��Surface Destroyed");
        if (null != mCamera) {
            mCamera.setPreviewCallback(null); // �������������ǰ����Ȼ�˳�����
            mCamera.stopPreview();
            bIfPreview = false;
            mCamera.release();
            mCamera = null;
        }
    }


    // InitSurfaceView
    private void initSurfaceView() {
        mSurfaceview = (SurfaceView) this.findViewById(R.id.surface_camera);
        mSurfaceHolder = mSurfaceview.getHolder(); // ��SurfaceView��ȡ��SurfaceHolder����
        mSurfaceHolder.addCallback(OnlineService_OK.this); // SurfaceHolder����ص��ӿ�
        // mSurfaceHolder.setFixedSize(176, 144); // Ԥ����С�O��
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// �O���@ʾ����ͣ�setType��������
    }

    /* ��2�������Ԥ���� */
    private void initCamera() {// surfaceChanged�е���

        try {
            //	mCamera = Camera.open();
            /*cam.setPreviewDisplay(holder);	*/
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode("off"); // �������
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            parameters.setPreviewFormat(ImageFormat.NV21);

            List<Size> camList = parameters.getSupportedPictureSizes();

            //֧�ֱַ���
            for (int i = 0; i < camList.size(); i++) {
                System.out.println(camList.get(i).height + " x " + camList.get(i).width);
            }


            //���������� ����������������õĺ���ʵ�ֻ��Ĳ�һ��ʱ���ͻᱨ��
            parameters.setPictureSize(mPreviewWidth, mPreviewHeight);
            parameters.setPreviewSize(mPreviewWidth, mPreviewHeight);

			
			/* ��Ƶ�����봦�� */
            // ��Ӷ���Ƶ��������
            // �趨���ò���������Ԥ��
            //	mCamera.setParameters(parameters); // ��Camera.Parameters�趨��Camera


            int size = mPreviewWidth * mPreviewHeight;
            size = size * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat())
                    / 8;
            buf = new byte[size]; // class variable
            mCamera.addCallbackBuffer(buf);
            mCamera.setPreviewCallbackWithBuffer(this);

            mCamera.startPreview(); // ��Ԥ������


            bIfPreview = true;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("InlinedApi")
    private void initCode() {


        File f = new File(Environment.getExternalStorageDirectory(), "video_encoded.264");
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(f));
            Log.i("AvcEncoder", "outputStream initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, mPreviewWidth, mPreviewHeight);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1000000);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);// selectColorFormat(codecInfo, "video/avc"));
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, I_FRAME_INTERVAL);

        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();

    }


    public synchronized void onPreviewFrame(final byte[] data, Camera camera) {


        //byte[] i420bytes=new byte[data.length];
        //if (frameListener != null) {
        //   onFrame(data, 0, data.length, 0);
        //  swapYV12toNV12(data);
        swapNv21toNV12(data);
        //   swapYV12toI420(data, i420bytes, mPreviewWidth, mPreviewHeight);
        onFrame(data);

        mCamera.addCallbackBuffer(buf);

    }

    OutputStream outputStream = null;

    private static int selectColorFormat(MediaCodecInfo codecInfo,
                                         String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo
                .getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (isRecognizedFormat(colorFormat)) {
                return colorFormat;
            }
        }
        Log.e("MediaEncoder", "couldn't find a good color format for "
                + codecInfo.getName() + " / " + mimeType);
        return 0; // not reached
    }


    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {

            // these are the formats we know how to handle for this test
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:

                return true;
            default:
                return false;
        }
    }

    private static MediaCodecInfo selectCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);

            if (!codecInfo.isEncoder()) {
                continue;
            }

            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    int count = 0;

    public void onFrame(byte[] input) {

        byte[] output = null;
        int frameType = 0;
        try {
            ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
            ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();

            int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input);
                mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, 0, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
            while (outputBufferIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                outputStream.write(outData, 0, outData.length);

                output = new byte[outData.length + 40]; //����ֽ�������SPS��PPS��Ϣ

                if (m_info == null) {    //����pps sps ֻ�п�ʼʱ ��һ��֡���У� ��������������
                    ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData);
                    if (spsPpsBuffer.getInt() == 0x00000001) {
                        m_info = new byte[outData.length];
                        System.arraycopy(outData, 0, m_info, 0, outData.length);
                    }
					 /* m_info = new byte[35];  
					  System.arraycopy(outData, 0, m_info, 0, m_info.length); */

                } else {
                    System.arraycopy(outData, 0, output, 0, outData.length);
                }
                if (isInitPush) {
					/*if(isFirstfream){
						isFirstfream = false;
						System.arraycopy(m_info, 0,  output, 0, m_info.length);  
						System.arraycopy(outData, 0,  output, m_info.length, outData.length);						
						sccPushData(m_info, m_info.length, 0,1, 5, 4);
					}else*/
                    if (outData[4] == 0x25 || outData[4] == 0x65) {    //key frame   ���������ɹؼ�֡ʱֻ�� 00 00 00 01 65 û��pps sps�� Ҫ����
                        System.arraycopy(m_info, 0, output, 0, m_info.length);
                        System.arraycopy(outData, 0, output, m_info.length, outData.length);
                        frameType = 1;
                        Log.i("Push..I", outData.length + "");
                    } else {

                        frameType = 0;
                        //	Log.i("Push..P", outData.length+"");

                    }
                    //sccPushData(output, output.length, 0,frameType, 5, 4);
                }

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    //yv12 ת yuv420p  yvu -> yuv
    private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height) {
        System.arraycopy(yv12bytes, 0, i420bytes, 0, width * height);
        System.arraycopy(yv12bytes, width * height + width * height / 4, i420bytes, width * height, width * height / 4);
        System.arraycopy(yv12bytes, width * height, i420bytes, width * height + width * height / 4, width * height / 4);
    }


    private byte[] swapYV12toNV12(byte[] NV21bytes) {
        byte[] tmp = new byte[NV21bytes.length / 3];
        int startPos = (NV21bytes.length * 2) / 3;
        System.arraycopy(NV21bytes, (NV21bytes.length * 2) / 3, tmp, 0, NV21bytes.length / 3);
        for (int i = 0; i < NV21bytes.length / 6; i++) {
            NV21bytes[i + startPos] = tmp[i];
            NV21bytes[i + 1 + startPos] = tmp[i + NV21bytes.length / 6];
        }
        return NV21bytes;
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
  

	/*public native int sccPushData(byte[] data, int langth, int pStream, int iPofType,int iResType, int iStreamType );	
	public native int sccInitJava(int argc, String jsdevid, String jspwd, String jshost, int nLang );*/

    boolean isInitPush = false;
    boolean isFirstfream = true;
    byte[] m_info = null;

    public void CallbackFunForDataServer(String arg1, int arg2, int arg3, int arg4) {
        if (arg2 == 101) {
            System.out.println("������Ƶ");
            isInitPush = true;

        }
        if (arg2 == 102) {
            System.out.println("�ر���Ƶ");
            isInitPush = false;

        }
    }

    static {

        System.loadLibrary("chinalinksipc");
        System.loadLibrary("system");
        System.loadLibrary("scctv");

    }


}
	


