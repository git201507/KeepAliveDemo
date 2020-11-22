//
// Created by Administrator on 2020/11/2.
//

#include "com_xk_ndkdemo_MyNdk.h"

#include <android/log.h>

#define  LOG_TAG    "lzd"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

JNIEXPORT jstring JNICALL Java_com_xk_ndkdemo_MyNdk_getString
  (JNIEnv *env, jclass){
        LOGI("getString");
        return env->NewStringUTF("This is native-lib !!!");
  }