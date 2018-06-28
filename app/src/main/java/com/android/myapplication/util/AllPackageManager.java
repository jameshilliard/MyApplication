package com.android.myapplication.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liurenyi on 2018/6/5.
 */

public class AllPackageManager {

    public static final String TAG = "AllPackageManager";
    public static final boolean DEBUG = false;

    private Context mContext;
    private List<IPackageInfo> mList = new ArrayList<IPackageInfo>();
    private IPackageInfo packageInfo;
    private Intent intent;

    private List<IPackageInfo> mSystemAppList = new ArrayList<>();
    private List<IPackageInfo> mThreeAppList = new ArrayList<>();

    public AllPackageManager(Context mContext, List<IPackageInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    // 获取所有的应用程序包名
    public List<IPackageInfo> getAllPackageName() {
        PackageManager packageManager = mContext.getPackageManager();
        List<ApplicationInfo> listAppcations = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(packageManager));
        for (ApplicationInfo info : listAppcations) {
            String packageName = getProgramNameByPackageName(info.packageName);
            String packageClassName = getAllPackageClassName(info.packageName);
            packageInfo = new IPackageInfo();
            packageInfo.setPackageAppName(packageName);
            packageInfo.setPackageName(info.packageName);
            packageInfo.setPackageClassName(packageClassName);
            mList.add(packageInfo);
        }
        return mList;
    }

    /**
     * 获取系统app
     */
    public void getSystemApp() {
        // 获取全部应用
        PackageManager packageManager = mContext.getPackageManager();
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infos = packageManager.queryIntentActivities(intent, 0);
        if (DEBUG) {
            Log.e(TAG, "infos-->" + infos.size());
        }
        for (int i = 0; i < infos.size(); i++) {
            ResolveInfo resolveInfo = infos.get(i);
            String packageName = resolveInfo.activityInfo.packageName;
            try {
                PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(packageName, 0);
                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) { //第三方应用
                    if (DEBUG) {
                        Log.i(TAG, packageName + "是第三方应用");
                    }
                    String packageName1 = getProgramNameByPackageName(packageName);
                    String className = getAllPackageClassName(packageName);
                    IPackageInfo info = new IPackageInfo();
                    info.setPackageAppName(packageName1);
                    info.setPackageName(packageName);
                    info.setPackageClassName(className);
                    mThreeAppList.add(info);
                } else {
                    if (DEBUG) {
                        Log.i(TAG, packageName + "系统应用");
                    }
                    String packageName1 = getProgramNameByPackageName(packageName);
                    String className = getAllPackageClassName(packageName);
                    IPackageInfo info = new IPackageInfo();
                    info.setPackageAppName(packageName1);
                    info.setPackageName(packageName);
                    info.setPackageClassName(className);
                    mSystemAppList.add(info);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public List<IPackageInfo> getmSystemAppList() {
        return mSystemAppList;
    }

    public List<IPackageInfo> getmThreeAppList() {
        return mThreeAppList;
    }

    // 获取指定包名的应用的名称
    public String getProgramNameByPackageName(String packageName) {
        PackageManager pm = mContext.getPackageManager();
        String name = null;
        try {
            name = pm.getApplicationLabel(
                    pm.getApplicationInfo(packageName,
                            PackageManager.GET_META_DATA)).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    // 获取指定包名的应用的类名
    public String getAllPackageClassName(String packageName) {
        PackageManager pm = mContext.getPackageManager();
        String className = null;
        ApplicationInfo info = null;
        try {
            info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info.className == null) {
            return "nukown";
        }
        return info.className;
    }
}
