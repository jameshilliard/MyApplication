package com.lry.songmachine.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.lry.songmachine.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDialog implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static final String TAG = "liu-CustomerDialog";
    public boolean DEBUG = true;

    private Dialog atmosphereDialog;
    private View atmosphereView;
    private Context context;
    private Window window;
    private WindowManager manager;
    private Display display;
    private float widthScale = 0.55f; //dialog宽的比例
    private float heightScale = 0.65f; //dialog高的比例

    private GridView gridView;
    private ImageView image_hua1, image_hua2, image_hua3, image_hua4;
    private Button btn_daocai, btn_huanhu, btn_koushao, btn_zhangsheng;

    private int KEY_EXPRESSION = 1;
    private int KEY_FLOWER = 2;
    private int KEY_SOUND = 3;

    public interface OnItemClickListener {
        void OnItemClick(int type, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private SimpleAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<>();
    private int[] icons = {R.mipmap.bq1, R.mipmap.bq2, R.mipmap.bq3, R.mipmap.bq4, R.mipmap.bq5,
            R.mipmap.bq6, R.mipmap.bq7, R.mipmap.bq8, R.mipmap.bq9, R.mipmap.bq10};

    public CustomerDialog(Context context) {
        this.context = context;
        initData();
    }

    private void initData() {
        for (int i = 0; i < icons.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("icons", icons[i]);
            list.add(map);
        }
    }

    public void AtmosphereDialog(int resId) {
        atmosphereDialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        atmosphereView = LayoutInflater.from(context).inflate(resId, null);
        if (resId == R.layout.layout_atmosphere_view) {
            gridView = (GridView) atmosphereView.findViewById(R.id.grid_view_expression);
            adapter = new SimpleAdapter(context, list, R.layout.adapter_item_expression, new String[]{"icons"}, new int[]{R.id.image_expression});
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);

            image_hua1 = (ImageView) atmosphereView.findViewById(R.id.image_xh1);
            image_hua2 = (ImageView) atmosphereView.findViewById(R.id.image_xh2);
            image_hua3 = (ImageView) atmosphereView.findViewById(R.id.image_xh3);
            image_hua4 = (ImageView) atmosphereView.findViewById(R.id.image_xh4);
            image_hua1.setOnClickListener(this);
            image_hua2.setOnClickListener(this);
            image_hua3.setOnClickListener(this);
            image_hua4.setOnClickListener(this);

            btn_daocai = (Button) atmosphereView.findViewById(R.id.btn_daocai);
            btn_huanhu = (Button) atmosphereView.findViewById(R.id.btn_huanhu);
            btn_koushao = (Button) atmosphereView.findViewById(R.id.btn_koushao);
            btn_zhangsheng = (Button) atmosphereView.findViewById(R.id.btn_zhangsheng);
            btn_daocai.setOnClickListener(this);
            btn_huanhu.setOnClickListener(this);
            btn_koushao.setOnClickListener(this);
            btn_zhangsheng.setOnClickListener(this);
        }
        atmosphereDialog.setContentView(atmosphereView);
        /***********************重新绘制dialog窗口大小***********************/
        window = atmosphereDialog.getWindow(); //获得dialog窗口
        manager = window.getWindowManager(); //获得windowmanager
        display = manager.getDefaultDisplay();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (int) (display.getWidth() * widthScale);
        params.height = (int) (display.getHeight() * heightScale);
        window.setAttributes(params);
        /***********************重新绘制dialog窗口大小***********************/
        atmosphereDialog.show();
    }

    /**
     * @param type     种类，可能是鲜花被点击，或者音效被点击,或者表情被点击
     * @param position 某个种类的某个位置被点击，确认点击的是哪个按钮
     */
    private void clickListener(int type, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.OnItemClick(type, position);
        } else {
            Log.e(TAG, "onItemClickListener is null");
        }
    }

    /*******************************监听事件**************************************/
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (DEBUG) {
            Log.e("liu", "click: " + i);
        }
        clickListener(KEY_EXPRESSION, i);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_xh1:
                clickListener(KEY_FLOWER, 1);
                break;
            case R.id.image_xh2:
                clickListener(KEY_FLOWER, 2);
                break;
            case R.id.image_xh3:
                clickListener(KEY_FLOWER, 3);
                break;
            case R.id.image_xh4:
                clickListener(KEY_FLOWER, 4);
                break;
            case R.id.btn_daocai:
                clickListener(KEY_SOUND, 1);
                break;
            case R.id.btn_huanhu:
                clickListener(KEY_SOUND, 2);
                break;
            case R.id.btn_koushao:
                clickListener(KEY_SOUND, 3);
                break;
            case R.id.btn_zhangsheng:
                clickListener(KEY_SOUND, 4);
                break;
        }
    }
    /*******************************监听事件**************************************/

}