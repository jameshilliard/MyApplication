package com.pax.hdmitest;

import java.util.Formatter;
import java.util.Locale;

import android.app.Presentation;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Create by chendd on 2018/2/5 15:55
 */

public class VideoPresentation extends Presentation {
    private static final String TAG = "VideoPresentation";

    private RelativeLayout relativeLayout;

    private Context mContext;

    private VideoView videoView;

    private TextView current;

    private SeekBar progress;

    private TextView total;

    private ImageView start;

    private boolean isPlaying = false;

    private LinearLayout layout_bottom;


    private Handler mHandler = new Handler();

    public VideoPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

//    private void showLayout(boolean show) {
//        int visible = show ? View.VISIBLE : View.GONE;
//        layout_bottom.setVisibility(visible);
//        start.setVisibility(visible);
//    }

//    private void startTimer() {
//        mTimer.cancel();
//        mTimer.start();
//    }

//    private CountDownTimer mTimer = new CountDownTimer(1000 * 10, 1000) {
//        @Override
//        public void onTick(long millisUntilFinished) {
//
//        }
//
//        @Override
//        public void onFinish() {
//            showLayout(false);
//        }
//    };

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
//        mTimer.cancel();
//        showLayout(true);
//        mTimer.start();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_vedio);

        videoView = (VideoView) findViewById(R.id.vv_vedio);
        current = (TextView) findViewById(R.id.current);
        total = (TextView) findViewById(R.id.total);
        progress = (SeekBar) findViewById(R.id.progress);
        start = (ImageView) findViewById(R.id.start);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        relativeLayout = this.findViewById(R.id.relative);

//        String localUrl = Environment.getExternalStorageDirectory().getPath() + "/MyVideo.mp4";
//
//        Uri uri = Uri.parse(localUrl);
//        //播放完成回调
//        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
//        //设置视频路径
//        videoView.setVideoURI(uri);
//
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                //获取总时长
//                int duration = videoView.getDuration();
//                total.setText(stringForTime(duration));
//                progress.setMax(duration);
//
//                mRunnable.run();
//            }
//        });
//
//
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startTimer();
//                if (isPlaying) {
//                    videoView.pause();
//                } else {
//                    videoView.start();
//                }
//                isPlaying = !isPlaying;
//                start.setPressed(isPlaying);
//                start.setImageResource(isPlaying ? R.drawable.jc_click_pause_selector : R
//                        .drawable.jc_click_play_selector);
//
//            }
//        });


//        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "onStartTrackingTouch: " + seekBar.getProgress());
//                mHandler.removeCallbacks(mRunnable);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(TAG, "onStopTrackingTouch: " + seekBar.getProgress());
//                videoView.seekTo(seekBar.getProgress());
//                mRunnable.run();
//            }
//        });
//
//        progress.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                startTimer();
//                return false;
//            }
//        });
//
//
//    }
//
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//            int currentPosition = videoView.getCurrentPosition();
//            progress.setProgress(currentPosition);
//            current.setText(stringForTime(currentPosition));
//            mHandler.postDelayed(mRunnable, 1000);
//        }
//    };
//
//
//    @Override
//    public void show() {
//        super.show();
//    }
//
//    @Override
//    public void dismiss() {
//        Log.e("liu", "<VideoPresentation> --> dismiss");
//        mHandler.removeCallbacksAndMessages(null);
//        videoView.pause();
//        relativeLayout.setVisibility(View.GONE);
//        super.dismiss();
//    }
//
//    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
//
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            //Toast.makeText(mContext, "播放完成了", Toast.LENGTH_SHORT).show();
//            mp.start();
//        }
//    }
//
//    public static String stringForTime(int timeMs) {
//        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
//            return "00:00";
//        }
//        int totalSeconds = timeMs / 1000;
//        int seconds = totalSeconds % 60;
//        int minutes = (totalSeconds / 60) % 60;
//        int hours = totalSeconds / 3600;
//        StringBuilder stringBuilder = new StringBuilder();
//        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
//        if (hours > 0) {
//            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
//        } else {
//            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
//        }
//    }
//
//    @Override
//    public void onDisplayRemoved() {
//        Log.e("liu", "<onDisplayRemoved> --> ");
//        relativeLayout.setVisibility(View.GONE);
//        super.onDisplayRemoved();
//    }
    }
}
