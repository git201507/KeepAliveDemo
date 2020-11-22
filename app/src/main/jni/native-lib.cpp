#include "com_xk_ndkdemo_Wathcer.h"

#include <jni.h>
#include <string>
#include <unistd.h>
#include <android/log.h>
#include <pthread.h>

#define LOG_TAG "aruba"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

int user_id;

void startDaemonProcess();

extern "C"
JNIEXPORT void JNICALL
Java_com_xk_ndkdemo_Wathcer_startDaemon(JNIEnv *env, jobject instance,
                                                            jint userId) {
    user_id = userId;
    //开启守护进程
    startDaemonProcess();
}

void *thread_rt1(void *data) {
    //LOGD("重启父进程");
    execlp("am", "am", "startservice", "--user", user_id,
           "com.xk.ndkdemo/com.xk.ndkdemo.ProcessService", (char *) NULL);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_xk_ndkdemo_Wathcer_stopDuplicateService(JNIEnv *env, jobject instance,
                                                            jint userId) {
    pthread_t tid;
    pthread_create(&tid, NULL, thread_rt1, NULL);
}


void *thread_rt(void *data) {
    pid_t pid;
    while ((pid = getppid()) != 1) {
        sleep(2);
        LOGD("循环 %d ", pid);
    }

    //父进程等于1  app被干掉了
    LOGD("重启父进程");
    execlp("am", "am", "startservice", "--user", user_id,
           "com.xk.ndkdemo/com.xk.ndkdemo.ProcessService", (char *) NULL);
}

void startDaemonProcess() {
    pid_t pid = fork();
    if (pid > 0) {//父进程

    } else {//子进程，创建线程，开启轮询
        pthread_t tid;

        //参数1为为指向线程标识符的地址。
        //参数2用于设置线程属性，一般为空，表示使用默认属性。
        //参数3是线程运行函数的地址，填函数名就可以了。
        //参数4是线程运行函数的参数。新创建的线程从thread_rt函数的地址开始运行，
        // 该函数只有一个无类型指针参数arg。若要想向thread_rt传递多个参数，
        // 可以将多个参数放在一个结构体中，然后把结构体的地址作为arg参数传入，
        // 但是要非常慎重，程序员一般不会这么做。
        pthread_create(&tid, NULL, thread_rt, NULL);
    }
}