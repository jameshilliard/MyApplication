package com.pax.hdmitest;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;



/**
 * Create by chendd on 2018/2/5 15:25
 */

public class StringPresentation extends Presentation {

    private static final String TAG = "StringPresentation";

    private Context mContext;

    public StringPresentation(Context outerContext, Display display) {
        super(outerContext, display);
        this.mContext = outerContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(mContext);
        TextView textView = new TextView(mContext);
        textView.setTextSize(20);
        textView.setTextColor(getResources().getColor(R.color.black));
        setContentView(scrollView);
        textView.setText(mContext.getString(R.string.text_spring));
        textView.append(mContext.getString(R.string.text_spring));
        textView.append(mContext.getString(R.string.text_spring));
        textView.append(mContext.getString(R.string.text_spring));
        textView.append(mContext.getString(R.string.text_spring));
        textView.append(mContext.getString(R.string.text_spring));

//        ViewGroup.LayoutParams p = scrollView.getLayoutParams();
//        p.width = 1000;
//        p.height = 1000;
//        scrollView.setLayoutParams(p);
        scrollView.addView(textView);

    }

}
