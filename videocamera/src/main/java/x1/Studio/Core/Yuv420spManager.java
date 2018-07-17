package x1.Studio.Core;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 负责把摄像头预览的YUV420sp数据保存为图片
 */

public class Yuv420spManager {

    private static String saveImagePath = "mnt/sdcard/A_test/";
    private static String saveImagePath2 = "mnt/sdcard/B_test/";

    /**
     * 把YUV420sp数据先转化为ARGB数据，在保存为图片
     *
     * @param data
     * @param width
     * @param height
     */
    public static void saveBitmap(final byte[] data, final int width, final int height) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String fileName = System.currentTimeMillis() + ".jpg";
                String filePath = saveImagePath + fileName;
                File path = new File(filePath);
                if (!path.getParentFile().exists()) {
                    path.getParentFile().mkdirs();
                } else {
                    path.delete();
                }
                int[] argb = I420toARGB(data, width, height);
                Bitmap bitmap = Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    fileOutputStream.close();
                } catch (IOException e) {

                }
            }
        }).start();
    }

    /**
     * 直接把YUV420sp数据保存为图片
     *
     * @param data
     * @param width
     * @param height
     */
    public static void saveBitmap2(final byte[] data, final int width, final int height) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream outStream = null;
                File file = new File(saveImagePath2);
                if (!file.exists()) {
                    file.mkdirs();
                }
                YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
                try {
                    outStream = new FileOutputStream(saveImagePath2 + System.currentTimeMillis() + ".jpg");
                    outStream.write(baos.toByteArray());
                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 把YUV420sp转为ARGB数据，保存的图片会变为黑白色
     *
     * @param yuv
     * @param width
     * @param height
     * @return
     */
    private static int[] I420toARGB(byte[] yuv, int width, int height) {

        boolean invertHeight = false;
        if (height < 0) {
            height = -height;
            invertHeight = true;
        }

        boolean invertWidth = false;
        if (width < 0) {
            width = -width;
            invertWidth = true;
        }

        int iterations = width * height;
        int[] rgb = new int[iterations];

        for (int i = 0; i < iterations; i++) {
            int nearest = (i / width) / 2 * (width / 2) + (i % width) / 2;

            int y = yuv[i] & 0x000000ff;
            int u = yuv[iterations + nearest] & 0x000000ff;

            int v = yuv[iterations + iterations / 4 + nearest] & 0x000000ff;

            int b = (int) (y + 1.8556 * (u - 128));
            int g = (int) (y - (0.4681 * (v - 128) + 0.1872 * (u - 128)));
            int r = (int) (y + 1.5748 * (v - 128));

            if (b > 255) {
                b = 255;
            } else if (b < 0) {
                b = 0;
            }
            if (g > 255) {
                g = 255;
            } else if (g < 0) {
                g = 0;
            }
            if (r > 255) {
                r = 255;
            } else if (r < 0) {
                r = 0;
            }
            int targetPosition = i;

            if (invertHeight) {
                targetPosition = ((height - 1) - targetPosition / width) * width + (targetPosition % width);
            }
            if (invertWidth) {
                targetPosition = (targetPosition / width) * width + (width - 1) - (targetPosition % width);
            }
            rgb[targetPosition] = (0xff000000) | (0x00ff0000 & r << 16) | (0x0000ff00 & g << 8) | (0x000000ff & b);
        }
        return rgb;
    }

    /**
     * 数据旋转90度
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
        // Rotate the U and V color components
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i--;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i--;
            }
        }
        return yuv;
    }

    /**
     * 数据旋转180度
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUV420Degree180(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth
                * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }

    /**
     * 数据旋转270度
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUV420Degree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int nWidth = 0, nHeight = 0;
        int wh = 0;
        int uvHeight = 0;
        if (imageWidth != nWidth || imageHeight != nHeight) {
            nWidth = imageWidth;
            nHeight = imageHeight;
            wh = imageWidth * imageHeight;
            uvHeight = imageHeight >> 1;// uvHeight = height / 2
        }
        // ??Y
        int k = 0;
        for (int i = 0; i < imageWidth; i++) {
            int nPos = 0;
            for (int j = 0; j < imageHeight; j++) {
                yuv[k] = data[nPos + i];
                k++;
                nPos += imageWidth;
            }
        }
        for (int i = 0; i < imageWidth; i += 2) {
            int nPos = wh;
            for (int j = 0; j < uvHeight; j++) {
                yuv[k] = data[nPos + i];
                yuv[k + 1] = data[nPos + i + 1];
                k += 2;
                nPos += imageWidth;
            }
        }
        return rotateYUV420Degree180(yuv, imageWidth, imageHeight);
    }
}
