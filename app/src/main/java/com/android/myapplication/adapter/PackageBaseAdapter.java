package com.android.myapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.myapplication.R;
import com.android.myapplication.util.IPackageInfo;
import com.android.myapplication.util.Method;

import java.util.List;

/**
 * Created by liurenyi on 2018/6/9.
 */

public class PackageBaseAdapter extends BaseAdapter {

    public static final String TAG_G = "liu";
    public static final String Content = "<PackageBaseAdapter> --";

    public List<IPackageInfo> allPackageName;
    public Context mContext;

    public PackageBaseAdapter(Context mContext, List<IPackageInfo> allPackageName) {
        this.allPackageName = allPackageName;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return allPackageName.size();
    }

    @Override
    public Object getItem(int i) {
        return allPackageName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHold viewHold = null;
        if (view == null) {
            viewHold = new ViewHold();
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_package_info_item, null);
            viewHold.imageView = view.findViewById(R.id.image);
            viewHold.appName = view.findViewById(R.id.tv_app_name);
            viewHold.packageName = view.findViewById(R.id.tv_package_name);
            view.setTag(viewHold);
        } else {
            viewHold = (ViewHold) view.getTag();
        }

        IPackageInfo packageInfo = allPackageName.get(i);
        viewHold.appName.setText(packageInfo.getPackageAppName());
        viewHold.packageName.setText(packageInfo.getPackageName());
        Drawable drawable = Method.getIcon(mContext, packageInfo.getPackageName());
        if (drawable != null) {
            viewHold.imageView.setBackground(drawable);
        } else {
            viewHold.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        }
        return view;
    }

    class ViewHold {
        ImageView imageView;
        TextView appName;
        TextView packageName;
    }
}
