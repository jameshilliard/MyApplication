#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include "jnitest.h"
#include <dlfcn.h>

JNIEXPORT jstring JNICALL
Java_com_android_myapplication_TestJni_getPackname(JNIEnv *env, jclass clazz, jobject obj) {

    jclass native_class = env->GetObjectClass(obj);
    jmethodID mID = env->GetMethodID(native_class, "getPackageName", "()Ljava/lang/String;");
    jstring packName = static_cast<jstring>(env->CallObjectMethod(obj, mID));

    return packName;
}

// 传参数给移动侦测的so库，判断是否有移动
JNIEXPORT void JNICALL Java_com_android_myapplication_TestJni_setShiftingValue
        (JNIEnv *env, jclass clazz, jint width, jint height, jint sample_num, jint time_sample_num,
         jint sub_sample_factor) {
    void *handle = NULL;
    char *mylib = "src/main/libs/armeabi-v7a/libawmd.so";
    char *error;

    //打开动态链接库
    handle = dlopen(mylib, RTLD_LAZY);
    dlerror();


//
//    if (!handle) {
//        fprintf(stderr, "%s\n", dlerror());
//        exit(EXIT_FAILURE);
//    } else{
//
//    }

}


