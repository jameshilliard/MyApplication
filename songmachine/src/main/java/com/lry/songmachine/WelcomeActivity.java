package com.lry.songmachine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lry.songmachine.bean.VideoInfo;
import com.lry.songmachine.rxjava.FileUtils;
import com.lry.songmachine.ui.HomeActivity;
import com.lry.songmachine.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends Activity {

    public static final String TAG = "liu";

    public Context mContext = WelcomeActivity.this;

    public Intent intent;
    //扫描之后得到的文件集合。
    public static List<VideoInfo> videoInfos = new ArrayList<>();
    //扫描之后得到的文件集合，并把它当做已点歌曲，默认选中。
    public static List<VideoInfo> selectedVideos = new ArrayList<>();

    public final int KEY_CODE_START_ACTIVITY = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case KEY_CODE_START_ACTIVITY:
                    intent = new Intent();
                    intent.setClass(mContext, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        videoInfos.clear();
        selectedVideos.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                readVideoInfo();
            }
        }).start();
    }

    /**
     * 读取指定路径的文件信息
     */
    private void readVideoInfo() {
        File file = new File(Utils.KEY_SCANNING_PATH);
        if (file.exists() && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    continue;
                }
                String name = listFiles[i].getName();
                String path = listFiles[i].getPath();
                if (FileUtils.isVideo(new File(path))) { // 判断是否是视频文件
                    VideoInfo info = new VideoInfo(name, path);
                    videoInfos.add(info);
                    selectedVideos.add(info);
                }
                Log.e(TAG, "name: " + name + " path: " + path);
                if (i == listFiles.length - 1) { // 遍历完成
                    Message message = new Message();
                    message.what = KEY_CODE_START_ACTIVITY;
                    handler.sendMessage(message);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
