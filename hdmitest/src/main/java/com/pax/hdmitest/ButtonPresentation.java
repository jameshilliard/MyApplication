package com.pax.hdmitest;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Create by chendd on 2018/2/6 15:35
 */

public class ButtonPresentation extends Presentation implements View.OnClickListener {


    private EditText et_result;

    /**
     * 其他
     */
    private Button btn_clean;
    private Button btn_back;

    /**
     * 数字
     */
    private Button num0;
    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;

    /**
     * 运算符
     */
    private Button btn_plus;
    private Button btn_reduce;
    private Button btn_multiply;
    private Button btn_divide;

    /**
     * 其他
     */
    private Button btn_point;
    private Button btn_result;

    /**
     * 文本信息
     */
    private String mExistedText = "0";


    public ButtonPresentation(Context outerContext, Display display) {
        super(outerContext, display);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presentation_button);

        init();

    }

    private void init() {
        et_result = (EditText) findViewById(R.id.et_result);

        btn_clean = (Button) findViewById(R.id.btn_clean);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_clean.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        num0 = (Button) findViewById(R.id.num0);
        num1 = (Button) findViewById(R.id.num1);
        num2 = (Button) findViewById(R.id.num2);
        num3 = (Button) findViewById(R.id.num3);
        num4 = (Button) findViewById(R.id.num4);
        num5 = (Button) findViewById(R.id.num5);
        num6 = (Button) findViewById(R.id.num6);
        num7 = (Button) findViewById(R.id.num7);
        num8 = (Button) findViewById(R.id.num8);
        num9 = (Button) findViewById(R.id.num9);
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);

        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_reduce = (Button) findViewById(R.id.btn_reduce);
        btn_multiply = (Button) findViewById(R.id.btn_multiply);
        btn_divide = (Button) findViewById(R.id.btn_divide);
        btn_plus.setOnClickListener(this);
        btn_reduce.setOnClickListener(this);
        btn_multiply.setOnClickListener(this);
        btn_divide.setOnClickListener(this);

        btn_point = (Button) findViewById(R.id.btn_point);
        btn_result = (Button) findViewById(R.id.btn_result);
        btn_point.setOnClickListener(this);
        btn_result.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.num0:
                mExistedText = isOverRange(mExistedText, "0");
                break;
            case R.id.num1:
                mExistedText = isOverRange(mExistedText, "1");
                break;
            case R.id.num2:
                mExistedText = isOverRange(mExistedText, "2");
                break;
            case R.id.num3:
                mExistedText = isOverRange(mExistedText, "3");
                break;
            case R.id.num4:
                mExistedText = isOverRange(mExistedText, "4");
                break;
            case R.id.num5:
                mExistedText = isOverRange(mExistedText, "5");
                break;
            case R.id.num6:
                mExistedText = isOverRange(mExistedText, "6");
                break;
            case R.id.num7:
                mExistedText = isOverRange(mExistedText, "7");
                break;
            case R.id.num8:
                mExistedText = isOverRange(mExistedText, "8");
                break;
            case R.id.num9:
                mExistedText = isOverRange(mExistedText, "9");
                break;


            case R.id.btn_plus:
                mExistedText = isOverRange(mExistedText, "+");
                break;
            case R.id.btn_reduce:
                mExistedText = isOverRange(mExistedText, "-");
                break;
            case R.id.btn_multiply:
                mExistedText = isOverRange(mExistedText, "x");
                break;
            case R.id.btn_divide:
                mExistedText = isOverRange(mExistedText, "÷");
                break;

            case R.id.btn_point:
                mExistedText = isOverRange(mExistedText, ".");
                break;
            case R.id.btn_result:
                mExistedText = isOverRange(mExistedText, "=");
                break;


            case R.id.btn_clean:
                cleanAll();
                break;

            case R.id.btn_back:
                if (!"0".equals(mExistedText)) {
                    int length = mExistedText.length();
                    if (length == 1) {
                        mExistedText = "0";
                    } else if (length > 1) {
                        mExistedText = mExistedText.substring(0, length - 1);
                    }

                }
                break;
        }

        et_result.setText(mExistedText);
    }

    private void cleanAll() {
        mExistedText = "0";

    }

    /**
     * 计算输入值
     *
     * @param existedText
     * @param s
     * @return
     */
    private String isOverRange(String existedText, String s) {

        if ("0".equals(existedText)) {
            existedText = "";
        }

        existedText += s;

        return existedText;
    }
}
