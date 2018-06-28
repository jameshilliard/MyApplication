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
import android.view.Menu;
import android.view.MenuItem;
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

    public static final String TAG = "PMActivity";
    public Context mContext = PackageManagerActivity.this;

    private List<IPackageInfo> mList = new ArrayList<IPackageInfo>();
    private List<IPackageInfo> useAppInfos = new ArrayList<>(); // 当前使用的app的集合
    private List<IPackageInfo> allPackageName; // 所有app的集合
    private List<IPackageInfo> systemAppInfos; // 系统app的集合
    private List<IPackageInfo> threeAppInfos; // 第三方app的集合

    private AllPackageManager manager;
    private ListView listView;
    private PackageBaseAdapter baseAdapter;
    private ResolveInfo resolveinfo;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_manager);
        getAppList(0);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_app:
                updateList(getAppList(0));
                break;
            case R.id.system_app:
                updateList(getAppList(1));
                break;
            case R.id.three_app:
                updateList(getAppList(2));
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取当前需要加载的显示的app集合，（所有应用的集合，系统应用的集合，第三方应用的集合）
     *
     * @param type 类型
     * @return 返回得到当前的应用集合
     */
    private List<IPackageInfo> getAppList(int type) {
        useAppInfos.clear();
        manager = new AllPackageManager(mContext, mList);
        manager.getSystemApp();
        if (type == 0) {
            allPackageName = manager.getAllPackageName();
            useAppInfos = allPackageName;
        } else if (type == 1) {
            systemAppInfos = manager.getmSystemAppList();
            useAppInfos = systemAppInfos;
        } else if (type == 2) {
            threeAppInfos = manager.getmThreeAppList();
            useAppInfos = threeAppInfos;
        }
        return useAppInfos;
    }

    /**
     * 初始化list view的操作
     */
    private void initView() {
        listView = (ListView) this.findViewById(R.id.list_info);
        setListViewInfo();
    }

    /**
     * 更新list view 的数据源，展示不同的数据
     *
     * @param infoList
     */
    private void updateList(List<IPackageInfo> infoList) {
        useAppInfos = infoList;
        setListViewInfo();
    }

    private void setListViewInfo() {
        baseAdapter = new PackageBaseAdapter(mContext, useAppInfos);
        listView.setAdapter(baseAdapter);
        listView.setOnItemClickListener(new ListViewOnItemClick());
        listView.setOnItemLongClickListener(new ListViewOnItemLongClick());
    }

    /**
     * itme的点击监听事件
     */
    class ListViewOnItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String packageName = useAppInfos.get(i).getPackageName();
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
            appDetailedInfo(useAppInfos.get(i).getPackageName());
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
            Log.e(TAG, "error package info is null");
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName); // 通过getPackageManager()的queryIntentActivities方法遍历
        List resolveinfoList = getPackageManager().queryIntentActivities(resolveIntent, 0);
        try {
            resolveinfo = (ResolveInfo) resolveinfoList.iterator().next();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
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
