package com.pax.hdmitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Create by chendd on 2018/2/6 11:44
 */

public class DrawView extends View {

    private static final String TAG = "DrawView";

    private Paint mPaint;
    private Path mPath;
    private float currentX, currentY;

    private int mWidth, mHeight;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        mWidth = width;
        mHeight = height;
        Log.d(TAG, "onMeasure: width =  " + width + ", height = " + height);
        setMeasuredDimension(width, height);
    }

    private int measureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.UNSPECIFIED) {
            return 1280;
        }
        return MeasureSpec.getSize(spec);
    }

    private int measureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        if (mode == MeasureSpec.UNSPECIFIED) {
            return 800;
        }
        return MeasureSpec.getSize(spec);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 去锯齿
        mPaint.setDither(true);//平滑效果
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLACK);
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(currentX, currentY, x, y); // 移动位置
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        currentX = x;
        currentY = y;
        invalidate();
        return true;
    }

    public void reset() {
        mPath.reset();
        invalidate();
    }


    public String saveToSdcard() {

        try {

            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawPath(mPath, mPaint);

            bitmap = resizeImage(bitmap, 640, 480);

            String sdcardDir = Environment.getExternalStorageDirectory().getPath() +
                    "/FunctionDesign/";
            Log.d(TAG, "saveToSdcard: " + sdcardDir);
            File fileDir = new File(sdcardDir);
            if (!fileDir.exists()) {
                boolean mkdirs = fileDir.mkdirs();
                Log.d(TAG, "create file dir result: " + mkdirs);
                if (!mkdirs) return "";
            }
            String picName = String.format(sdcardDir + "pic_%d.png", System.currentTimeMillis());
            File picFile = new File(picName);
            boolean createFile = picFile.createNewFile();
            if (!createFile) {
                Log.d(TAG, "saveToSdcard: create file error" + picName);
                return "";
            }

            FileOutputStream fos = new FileOutputStream(picFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d(TAG, "saveToSdcard: success");
            return picName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "saveToSdcard: false");
        return "";
    }

    /**
     * 缩放法压缩
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public Bitmap resizeImage(Bitmap bitmap, int width, int height) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();

        float scaleWidth = ((float) width) / originWidth;
        float scaleHeight = ((float) height) / originHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originWidth,
                originHeight, matrix, true);
    }
}
