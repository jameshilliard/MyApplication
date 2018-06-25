package com.android.myapplication.util;

/**
 * Created by liurenyi on 2018/6/5.
 */

public class IPackageInfo {

    private String packageName;
    private String packageClassName;
    private String packageAppName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageClassName() {
        return packageClassName;
    }

    public void setPackageClassName(String packageClassName) {
        this.packageClassName = packageClassName;
    }

    public String getPackageAppName() {
        return packageAppName;
    }

    public void setPackageAppName(String packageAppName) {
        this.packageAppName = packageAppName;
    }

    @Override
    public String toString() {
        return "IPackageInfo{" +
                "packageName='" + packageName + '\'' +
                ", packageClassName='" + packageClassName + '\'' +
                ", packageAppName='" + packageAppName + '\'' +
                '}';
    }
}
