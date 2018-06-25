package com.android.myapplication.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.myapplication.R;
import com.android.myapplication.adapter.PackageBaseAdapter;
import com.android.myapplication.util.AllPackageManager;
import com.android.myapplication.util.IPackageInfo;

import java.util.ArrayList;
import java.util.List;

public class PackageManagerActivity extends Activity {

    public static final String TAG = "liu-PMActivity";
    public Context mContext = PackageManagerActivity.this;

    private List<IPackageInfo> mList = new ArrayList<IPackageInfo>();
    private List<IPackageInfo> allPackageName;

    private AllPackageManager manager;
    private ListView listView;
    private PackageBaseAdapter baseAdapter;
    private ResolveInfo resolveinfo;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);
        manager = new AllPackageManager(mContext, mList);
        allPackageName = manager.getAllPackageName();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        listView = (ListView) this.findViewById(R.id.list_info);
        baseAdapter = new PackageBaseAdapter(mContext, allPackageName);
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new ListViewOnItemClick());
        listView.setOnItemLongClickListener(new ListViewOnItemLongClick());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * itme的点击监听事件
     */
    class ListViewOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String packageName = mList.get(i).getPackageName();
            String className = doStartApplicationWithPackageName(packageName);
            createDialog(className);
        }
    }

    /**
     * itme的长按监听事件
     */
    class ListViewOnItemLongClick implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            appDetailedInfo(mList.get(i).getPackageName());
            return true; // 返回true,消费掉此次长按事件
        }
    }

    private void createDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("apk的类名是:" + message);
        builder.show();
    }

    /**
     * 通过包名获取此APP详细信息,包括Activities、services、versioncode、name等等 并且可以直接调用
     */
    private String doStartApplicationWithPackageName(String packagename) {
        PackageInfo packageinfo = null;
        String className = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            Log.e("liu", "error package info is null");
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName); // 通过getPackageManager()的queryIntentActivities方法遍历
        List resolveinfoList = getPackageManager().queryIntentActivities(resolveIntent, 0);
        try {
            resolveinfo = (ResolveInfo) resolveinfoList.iterator().next();
        } catch (Exception e) {
            Log.e("liu", e.toString());
            return "unknowns";
        }
        if (resolveinfo != null) {
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式:packagename.mainActivityname]
            className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER); // 设置ComponentName参数1:packagename参数2:MainActivity路径
//            ComponentName cn = new ComponentName(packageName, className);
//            intent.setComponent(cn);
//            startActivity(intent);
        }
        return className;
    }

    /**
     * 启动特定的app的详情界面
     */
    private void appDetailedInfo(String packageName) {
        intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        mContext.startActivity(intent);
    }

}
