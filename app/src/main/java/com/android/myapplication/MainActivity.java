package com.android.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.myapplication.util.Method;
import com.android.myapplication.util.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    private Context mContext = MainActivity.this;

    private Button btnScreenDirection;
    private Button btnRecordVideo, btnPackageInfo;
    private Button btnPrepare;
    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        method = new Method();
    }

    private void initView() {
        btnScreenDirection = (Button) this.findViewById(R.id.btn_screen_direction);
        btnRecordVideo = (Button) this.findViewById(R.id.btn_record_video);
        btnPackageInfo = (Button) this.findViewById(R.id.btn_package_info);
        btnPrepare = (Button) this.findViewById(R.id.btn_prepare);

        btnScreenDirection.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
        btnPackageInfo.setOnClickListener(this);
        btnPrepare.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_screen_direction:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_ONE);
                finish();
                break;
            case R.id.btn_record_video:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_TWO);
                //finish();
                break;
            case R.id.btn_package_info:
                method.startOtherUI(mContext, Utils.KEY_ACTIVITY_CODE_THREE);
                break;
            case R.id.btn_prepare:
                writeConfigFile("liuxia", "mnt/private/config.txt");
                readConfigFile("mnt/private/config.txt");
                enterFile("mnt/private");
                break;
            default:
                break;
        }
    }

    /**
     * 复制单个文件到指定路径
     *
     * @param oldPath
     * @param newPath
     */
    private void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[9216]; // 9216因是最大的copy速度
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                Toast.makeText(mContext, "文件拷贝完成", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 进入对应文件目录
     *
     * @param path
     */
    private void enterFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            for (File list : listFiles) {
                Log.i("liu", "list -> " + list.toString());
            }
        }
    }

    /**
     * 创建配置文件
     *
     * @param path
     */
    private void readConfigFile(String path) {
        ArrayList<String> lineResult = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("liu", "Exception:" + e.toString());
                e.printStackTrace();
            }
        } else {
            try {
                InputStream inputStream = new FileInputStream(path);
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    lineResult.add(line + "\n");
                }
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("liu", "result:" + lineResult.toString());
    }

    private void writeConfigFile(String content, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            try {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.seek(file.length());
                randomAccessFile.write(content.getBytes());
                randomAccessFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            readConfigFile(fileName);
            writeConfigFile(content, fileName);
        }
    }

}
