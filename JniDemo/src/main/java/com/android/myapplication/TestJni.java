package com.android.myapplication;

/**
 * Created by liurenyi on 2018/6/1.
 */

public class TestJni {
    /**
     * 将用C++代码实现，在android代码中调用的方法：获取当前app的包名
     * @param o
     * @return
     */
    public static native String getPackname(Object o);

    public static native void setShiftingValue(int width,int height,int sample_num,int time_sample_num,int sub_sample_factor);

    /**
     * 加载so库或jni库，在使用到该库之前加载就行，不一定非要写在这个类内
     * 系统自己会判断扩展名是dll还是so,这里加载libJNI_ANDROID_TEST.so
     */
    static {
        System.loadLibrary("JNI_ANDROID_TEST");
    }
}
