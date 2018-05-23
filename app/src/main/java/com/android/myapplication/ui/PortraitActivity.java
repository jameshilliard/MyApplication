package com.android.myapplication.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.myapplication.MainActivity;
import com.android.myapplication.R;

public class PortraitActivity extends AppCompatActivity {

    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrait);
        btnChange = (Button) this.findViewById(R.id.btn_change);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PortraitActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
