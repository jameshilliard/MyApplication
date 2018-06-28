package com.pax.hdmitest;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;


/**
 * Create by chendd on 2018/2/5 17:17
 */

public class TouchPresentation extends Presentation {

    private static final String TAG = "TouchPresentation";

    private Context mContext;

    public TouchPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchView view = new TouchView(mContext);
//        ViewGroup.LayoutParams p = view.getLayoutParams();
//        p.width = 900;
//        p.height = 900;
//        view.setLayoutParams(p);
        setContentView(view);
    }


    private final class TouchView extends SurfaceView implements SurfaceHolder.Callback {

        private static final int MAX_TOUCHPOINTS = 10;

        private Paint touchPaints[] = new Paint[MAX_TOUCHPOINTS];
        private int colors[] = new int[MAX_TOUCHPOINTS];

        private int width, height;
        private float scale = 1.0f;


        public TouchView(Context context) {
            super(context);

            init();
        }

        private void init() {
            getHolder().addCallback(this);
            setFocusable(true);
            setFocusableInTouchMode(true);

            // 初始化10个不同颜色的画笔
            colors[0] = Color.BLUE;
            colors[1] = Color.RED;
            colors[2] = Color.GREEN;
            colors[3] = Color.YELLOW;
            colors[4] = Color.CYAN;
            colors[5] = Color.MAGENTA;
            colors[6] = Color.DKGRAY;
            colors[7] = Color.WHITE;
            colors[8] = Color.LTGRAY;
            colors[9] = Color.GRAY;
            for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                touchPaints[i] = new Paint();
                touchPaints[i].setColor(colors[i]);
            }


        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            int pointerCount = event.getPointerCount();
            if (pointerCount > MAX_TOUCHPOINTS) {
                pointerCount = MAX_TOUCHPOINTS;
            }
            //锁定canvas，进行绘制
            Canvas canvas = getHolder().lockCanvas();
            if (null != canvas) {
                canvas.drawColor(Color.WHITE);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //手离开时，清屏
                } else {
                    // 在每一个触点上绘制一个十字和坐标信息
                    for (int i = 0; i < pointerCount; i++) {
                        int id = event.getPointerId(i);
                        int x = (int) event.getX(i);
                        int y = (int) event.getY(i);
                        drawCrosshairsAndText(x, y, touchPaints[id], i, id, canvas);
                    }

                    // 在每一个触点上绘制一个圆
                    for (int i = 0; i < pointerCount; i++) {
                        int id = event.getPointerId(i);
                        int x = (int) event.getX(i);
                        int y = (int) event.getY(i);
                        drawCircle(x, y, touchPaints[id], canvas);
                    }
                }
                // 画完后，unlock
                getHolder().unlockCanvasAndPost(canvas);
            }
            return true;
        }

        private void drawCrosshairsAndText(int x, int y, Paint paint, int ptr,
                                           int id, Canvas c) {
            c.drawLine(0, y, width, y, paint);
            c.drawLine(x, 0, x, height, paint);
            int textY = (int) ((15 + 20 * ptr) * scale);
            paint.setTextSize(14 * scale);
            c.drawText("x" + ptr + "=" + x, 10 * scale, textY, paint);
            c.drawText("y" + ptr + "=" + y, 70 * scale, textY, paint);
            c.drawText("id" + ptr + "=" + id, width - 55 * scale, textY, paint);
        }

        private void drawCircle(int x, int y, Paint paint, Canvas c) {
            c.drawCircle(x, y, 20 * scale, paint);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            this.width = width;
            this.height = height;
            if (width > height) {
                this.scale = width / 480f;
            } else {
                this.scale = height / 480f;
            }
            Canvas c = getHolder().lockCanvas();
            if (c != null) {
                c.drawColor(Color.WHITE);
                getHolder().unlockCanvasAndPost(c);
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

}
