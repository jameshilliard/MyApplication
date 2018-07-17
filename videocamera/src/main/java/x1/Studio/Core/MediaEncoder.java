package x1.Studio.Core;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import android.test.AndroidTestCase;
import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Environment;
import android.util.Log;

public class MediaEncoder {
    private MediaCodec encoder;
    private IFrameListener frameListener;
    // size of a frame, in pixels
    private int mWidth = -1;
    private int mHeight = -1;
    // bit rate, in bits per second
    private int mBitRate = -1;
    private byte[] m_info;
    private OnlineService onlineService;

    private static final String MIME_TYPE = "video/avc";
    private static final boolean VERBOSE = true;
    private static final String TAG = "MediaEncoder";
    private static final int FRAME_RATE = 15; // 15fps
    private static final int IFRAME_INTERVAL = 1; // 10 seconds between//
    private static int mResolution = 3;                                        // I-frames

    MediaEncoder(int video_w, int video_h, int bitRate, OnlineService onlineService, int mResolution) {
        setParameters(video_w, video_h, bitRate);
        this.onlineService = onlineService;
        this.mResolution = mResolution;
        MediaCodecInfo codecInfo = selectCodec(MIME_TYPE);
        try {
            encoder = MediaCodec.createEncoderByType(MIME_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, video_w, video_h);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, CodecCapabilities.COLOR_FormatYUV420SemiPlanar);// selectColorFormat(codecInfo, "video/avc"));
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
        encoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encoder.start();
    }

    void close() {
        if (encoder != null) {
            encoder.stop();
            encoder.release();
            frameListener = null;
            encoder = null;
        }
    }

    public boolean setFrameListener(IFrameListener listener) {
        if (listener != null) {
            frameListener = listener;
            return true;
        } else {
            Log.e("MediaEncoder", "the input, FrameLister is null");
            return false;
        }
    }

    HashMap<String, CodecCapabilities> mEncoderInfos;

    public void onFrame(byte[] input) {

        byte[] output = null;

        try {
            ByteBuffer[] inputBuffers = encoder.getInputBuffers();
            ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
            int inputBufferIndex = encoder.dequeueInputBuffer(-1);
            if (inputBufferIndex >= 0) {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input);
                encoder.queueInputBuffer(inputBufferIndex, 0, input.length, 0, 0);
            }
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
            while (outputBufferIndex >= 0) {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);

                output = new byte[outData.length + 40];

                if (m_info == null) {
                    ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData);
                    if (spsPpsBuffer.getInt() == 0x00000001) {
                        m_info = new byte[outData.length];
                        System.arraycopy(outData, 0, m_info, 0, outData.length);
                    }
                } else {
                    System.arraycopy(outData, 0, output, 0, outData.length);
                }

                onlineService.pushData(outData, output, m_info, mResolution);

                encoder.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }


    private static int selectColorFormat(MediaCodecInfo codecInfo,
                                         String mimeType) {
        CodecCapabilities capabilities = codecInfo
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

    /**
     * Returns true if this is a color format that this test code understands
     * (i.e. we know how to read and generate frames in this format).
     */
    private static boolean isRecognizedFormat(int colorFormat) {
        switch (colorFormat) {
            // these are the formats we know how to handle for this test
            case CodecCapabilities.COLOR_FormatYUV420Planar:
            case CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
            case CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
            case CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
            case CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the first codec capable of encoding the specified MIME type, or
     * null if no match was found.
     */
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

    private void setParameters(int width, int height, int bitRate) {
        if ((width % 16) != 0 || (height % 16) != 0) {
            Log.w(TAG, "WARNING: width or height not multiple of 16");
        }
        mWidth = width;
        mHeight = height;
        mBitRate = bitRate;
    }
}