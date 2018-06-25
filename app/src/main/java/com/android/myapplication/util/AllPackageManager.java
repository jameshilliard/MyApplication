package com.android.myapplication.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by liurenyi on 2018/6/5.
 */

public class AllPackageManager {

    public static final String TAG_G = "liu";

    private Context mContext;
    private List<IPackageInfo> mList = new ArrayList<IPackageInfo>();
    private IPackageInfo packageInfo;

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
