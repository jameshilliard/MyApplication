package com.lry.songmachine.ui;

import android.app.Presentation;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lry.songmachine.R;
import com.lry.songmachine.util.MarqueeTextView;

import java.io.IOException;

public class DifferentDisplay extends Presentation {

    public static final String TAG = "liu-DifferentDisplay";
    public boolean DEBUG = true;

    public SurfaceView mSurfaceView;
    public SurfaceHolder holder;
    public MediaPlayer mMediaPlayer;
    public static final String s = "欢迎来到天上人间，";
    public static MarqueeTextView marqueeTextView;

    public int MARQUEE_MARGIN_LEFT = 550; //滚动字幕距离左边的距离
    public int MARQUEE_MARGIN_TOP = 0; //滚动字幕距离上边的距离

    private ImageView imageAtmosphere;
    private AnimationDrawable animationDrawable;
    private Handler handler = new Handler();
    private MediaPlayer mediaPlayer; //播放音效的mediaplay对象

    public DifferentDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    public SurfaceView getmSurfaceView() {
        return mSurfaceView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.different_display);
        mMediaPlayer = new MediaPlayer();
        mSurfaceView = (SurfaceView) this.findViewById(R.id.surface_different_display);
//        holder = mSurfaceView.getHolder();
//        holder.addCallback(this);
        imageAtmosphere = (ImageView) this.findViewById(R.id.image_atmosphere);
        startMarqueeTextView();
    }

    /**********************开启跑马灯方法 start************************/
    private void startMarqueeTextView() {
        marqueeTextView = (MarqueeTextView) findViewById(R.id.tv_scroll);
        ViewGroup.MarginLayoutParams margin1 = new ViewGroup.MarginLayoutParams(
                marqueeTextView.getLayoutParams());
        margin1.setMargins(MARQUEE_MARGIN_LEFT, MARQUEE_MARGIN_TOP, 0, 0);//设置滚动区域位置：在左边距234像素，顶边距0像素的位置
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(margin1);
        layoutParams1.height = 160;//设滚动区域高度
        layoutParams1.width = 800; //设置滚动区域宽度

        marqueeTextView.setLayoutParams(layoutParams1);
        marqueeTextView.setScrollWidth(800);
        marqueeTextView.setCurrentPosition(800);//设置滚动信息从滚动区域的右边出来
        marqueeTextView.setSpeed(1);

        marqueeTextView.setText("我是滚动字幕啊12345，我是滚动字幕啊12345，我是滚动字幕啊12345");
    }

    public static void marqueeText(String string) {
        if (marqueeTextView != null) {
            marqueeTextView.setText(s + string);
        }
    }

    /**********************开启跑马灯方法 end************************/

    //开始播放video，path-->video的路径
    public void startPlayVideo(String path) {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
//            try {
//                mMediaPlayer.setDataSource(path);
//                mMediaPlayer.prepare();
//                mMediaPlayer.setVolume(0f, 0f);//静音播放
//                mMediaPlayer.start();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            Log.e(TAG, "MediaPlayer is null");
//        }
    }

    // 当拔掉HDMI，在插入HDMI时，恢复异显状态的播放
    public void ResumePlayVideo(String path, int position) {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
//            try {
//                mMediaPlayer.setDataSource(path);
//                mMediaPlayer.prepare();
//                mMediaPlayer.setVolume(0f, 0f);//静音播放
//                mMediaPlayer.start();
//                mMediaPlayer.seekTo(position);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mMediaPlayer = new MediaPlayer();
//            try {
//                mMediaPlayer.setDataSource(path);
//                mMediaPlayer.prepare();
//                mMediaPlayer.setVolume(0f, 0f);//静音播放
//                mMediaPlayer.start();
//                mMediaPlayer.seekTo(position);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


    //副屏 - 重唱
    public void Replay() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.seekTo(0); //设置为开始位
//        }
    }

    //副屏 - 暂停播放
    public void pauseAndPlay(int position) {
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.seekTo(position);
//            mMediaPlayer.pause();
//        } else {
//            mMediaPlayer.start();
//            mMediaPlayer.seekTo(position);
//        }
    }

    public void pause(int position) {
//        if (mMediaPlayer.isPlaying()) {
//            mMediaPlayer.seekTo(position - 50);
//            mMediaPlayer.pause();
//        }
    }

    public void play(int position) {
//        if (!mMediaPlayer.isPlaying()) {
//            mMediaPlayer.start();
//            mMediaPlayer.seekTo(position);
//        }
    }

    public void play() {
//        if (!mMediaPlayer.isPlaying()) {
//            mMediaPlayer.start();
//        }
    }

    /**
     * 线程---停止副屏动画效果，并隐藏掉ImageView
     */
    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            if (animationDrawable != null) {
                animationDrawable.stop();
            } else {
                Log.e(TAG, "animationDrawable is null");
            }
            imageAtmosphere.setVisibility(View.GONE);
        }
    });

    /**
     * 播放音效文件，倒彩，口哨。。。。
     */
    private void playSound(int resId) {
        if (DEBUG) {
            Log.e(TAG, "====>>playSound()");
        }
        mediaPlayer = MediaPlayer.create(getContext(), resId);
        if (mediaPlayer == null) {
            Log.e(TAG, "the mediaPlayer is null.");
            return;
        }
        mediaPlayer.start();
    }

    /**
     * 开启动画效果，展示在副屏上
     */
    public void showAtmosphere(int type, int position) {
        if (DEBUG) {
            Log.e("liu", "---showAtmosphere---");
        }
        imageAtmosphere.setVisibility(View.VISIBLE);
        imageAtmosphere.setBackground(null);
        if (type == 1) {
            imageAtmosphere.setBackgroundResource(R.drawable.image_frame_expression);
            animationDrawable = (AnimationDrawable) imageAtmosphere.getBackground();
            animationDrawable.start();
            animationDrawable.setOneShot(false); //是否播放表情包动画（true动画不循环）
        } else if (type == 2) {
            imageAtmosphere.setBackgroundResource(R.drawable.image_frame_flower);
            animationDrawable = (AnimationDrawable) imageAtmosphere.getBackground();
            animationDrawable.start();
            animationDrawable.setOneShot(false); //是否播放送花动画（true动画不循环）
        } else if (type == 3) {
            //播放音效
            //TODO: 2018/4/20 0020 如果播放没有声音的，把播放音效操作放到HoneActivity处理
            if (position == 1) {
                playSound(R.raw.daocai);
            } else if (position == 2) {
                playSound(R.raw.huanhu);
            } else if (position == 3) {
                playSound(R.raw.koushao);
            } else if (position == 4) {
                playSound(R.raw.zhangsheng);
            } else {
                Log.e(TAG, "Please check the audio resource file under the raw file.");
            }
        } else {
            Log.e(TAG, "The category is illegal !");
        }
        handler.postDelayed(thread, 3000);
    }

    /*********************************监听事件 start*****************************/
//    @Override
//    public void surfaceCreated(SurfaceHolder surfaceHolder) {
//        mMediaPlayer.setDisplay(surfaceHolder);
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
//
//    }
//
//    @Override
//    public void onPrepared(MediaPlayer mediaPlayer) {
//
//    }
    /*********************************监听事件 end*****************************/

}
