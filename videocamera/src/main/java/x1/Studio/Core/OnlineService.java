package x1.Studio.Core;

import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class OnlineService {

    private SurfaceView mSurfaceview = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Camera mCamera = null;
    private String TAG = "x_Cameara";
    private boolean bIfPreview;
    private int mPreviewWidth = 640, mPreviewHeight = 480;
    private byte[] buf = null;
    private MediaCodec mediaCodec;
    private int mCount;
    private int FRAME_RATE = 15;
    private int I_FRAME_INTERVAL = 1;
    private String MIME_TYPE = "video/avc";

    boolean isInitPush = false;

    static {
        System.loadLibrary("chinalinksipc");
        System.loadLibrary("system");
        System.loadLibrary("scctv");
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


    public void pushData(byte[] outData, byte[] output, byte[] m_info, int mResolution) {
        int frameType = 0;
        if (isInitPush) {
            if (outData[4] == 0x25 || outData[4] == 0x65) {
                System.arraycopy(m_info, 0, output, 0, m_info.length);
                System.arraycopy(outData, 0, output, m_info.length, outData.length);
                frameType = 1;
                Log.i("Push..I", outData.length + "");
            } else {
                frameType = 0;
            }
            sccPushData(output, output.length, 0, frameType, mResolution, 4);
        }
    }


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

    public native int sccInitJava(int argc, String jsdevid, String jspwd, String jshost, int nLang);

    public native int sccPushData(byte[] data, int langth, int pStream, int iPofType, int iResType, int iStreamType);

    byte[] m_info = null;

    public void CallbackFunForDataServer(String arg1, int arg2, int arg3, int arg4) {
        if (arg2 == 101) {
            System.out.println("request video");
            isInitPush = true;

        }
        if (arg2 == 102) {
            System.out.println("close video");
            isInitPush = false;
        }
    }
}



