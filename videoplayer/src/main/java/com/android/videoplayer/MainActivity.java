package com.android.videoplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.android.videoplayer.dialog.DialogManager;
import com.android.videoplayer.storage.StorageManager;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "MainActivity";
    private MediaPlayer player;
    private SurfaceView videoSurface;
    private SurfaceHolder holder;
    private String videoPath = null;
    private DialogManager dialogManager;
    private Context context = MainActivity.this;
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //保持屏幕常亮
        setContentView(R.layout.activity_main);
        uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.aaa);
        initView();
        String video = getVideoPath(StorageManager.rootPath);
        Log.i("liu", "video-->" + video);
        if ((video == null || !new File(video).exists()) && uri == null) {
            dialogManager = new DialogManager(context);
            dialogManager.createDialog();
        } else {
            playVideo(uri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (player != null) {
            player.setDisplay(surfaceHolder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    private void initView() {
        videoSurface = this.findViewById(R.id.video_surface);
        holder = videoSurface.getHolder();
        holder.addCallback(this);
        holder.setKeepScreenOn(true);
    }


    /**
     * 获取一个video的路径用来准备播放
     *
     * @param path
     * @return
     */
    private String getVideoPath(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        for (File file1 : files) {
            if (file1.toString().contains(".mp4")
                    || file1.toString().contains(".mpg")
                    || file1.toString().contains(".mkv")
                    || file1.toString().contains(".3gp")) {
                videoPath = file1.getPath();
                return videoPath;
            } else if (!file1.isHidden() && file1.isDirectory()) {
                getVideoPath(file1.getPath());
            }
        }
        return videoPath;
    }

    /**
     * 播放视频
     *
     * @param videoPath
     */
    private void playVideo(Uri videoPath) {
        if (player == null) {
            player = new MediaPlayer();
            player.setOnPreparedListener(this);
            player.setOnCompletionListener(this);
        }
        try {
            player.setDataSource(context,videoPath);
            player.prepare();
            player.start();
            player.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
