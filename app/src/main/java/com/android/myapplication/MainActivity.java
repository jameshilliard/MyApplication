package com.android.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.myapplication.util.Method;
import com.android.myapplication.util.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = MainActivity.this;

    private Button btnScreenDirection;
    private Button btnRecordVideo;
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
        btnPrepare = (Button) this.findViewById(R.id.btn_prepare);

        btnScreenDirection.setOnClickListener(this);
        btnRecordVideo.setOnClickListener(this);
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
                method.startOtherUI(mContext,Utils.KEY_ACTIVITY_CODE_TWO);
                //finish();
                break;
            case R.id.btn_prepare:
                break;
            default:
                break;
        }
    }
}
