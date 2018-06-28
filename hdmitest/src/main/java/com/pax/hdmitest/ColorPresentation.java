package com.pax.hdmitest;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

/**
 * Create by chendd on 2018/2/5 14:12
 */

public class ColorPresentation extends Presentation {

    private static final String TAG = "ColorPresentation";

    private Context mContext;

    private View mView;

    private CountDownTimer mTimer;

    public ColorPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mView = new View(mContext);
        setContentView(mView);
        mView.setBackgroundColor(getRandomColor());
//        ViewGroup.LayoutParams p = mView.getLayoutParams();
//        p.width = 800;
//        p.height = 800;
//        mView.setLayoutParams(p);

        mTimer = new CountDownTimer(Integer.MAX_VALUE, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                mView.setBackgroundColor(getRandomColor());
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.d(TAG, "dismiss: ");
        mTimer.cancel();
    }

    @Override
    public void show() {
        super.show();
        Log.d(TAG, "show: ");
        mTimer.start();
    }

    @Override
    public Display getDisplay() {
        return super.getDisplay();
    }

    private int getRandomColor() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return Color.parseColor("#" + r + g + b);
    }

}
