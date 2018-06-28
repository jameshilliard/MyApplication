package com.pax.hdmitest;

import android.animation.Animator;
import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;


/**
 * Create by chendd on 2018/2/6 11:31
 */

public class DrawPresentation extends Presentation {

    private static final String TAG = "DrawPresentation";

    private Context mContext;

    private DrawView mDrawView;

    private TextView tv_vice_draw;

    public DrawPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_draw);

        mDrawView = (DrawView) findViewById(R.id.draw_view);
        tv_vice_draw = (TextView) findViewById(R.id.tv_vice_draw);
        tv_vice_draw.setVisibility(View.GONE);
        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 清除
                mDrawView.reset();
            }
        });

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 保存
                String saveName = mDrawView.saveToSdcard();
                if (saveName.contains("pic")) {
                    showTextToast(saveName);
                } else {
                    showTextToast("save bitmap failed");
                }
            }
        });
    }

    private void showTextToast(String text) {

        tv_vice_draw.setText(text);
        tv_vice_draw.setAlpha(0f);
        tv_vice_draw.animate().alpha(1f).setDuration(1000).setListener(new Animator
                .AnimatorListener() {


            @Override
            public void onAnimationStart(Animator animation) {
                tv_vice_draw.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                tv_vice_draw.animate().alpha(0f).setDuration(3000).setListener(new Animator
                        .AnimatorListener() {


                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tv_vice_draw.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

}
