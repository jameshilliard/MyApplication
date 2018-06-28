package com.android.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        String packname = TestJni.getPackname(MainActivity.this);
        TestJni.setShiftingValue(800, 480, 16, 16, 5);
        Log.i("liu", "packName: " + packname + "..............");
    }

    private void initView() {
        btnStart = this.findViewById(R.id.btn_Start);
        btnStart.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

    }
}
