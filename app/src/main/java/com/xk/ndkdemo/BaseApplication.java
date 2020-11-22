package com.xk.ndkdemo;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks(){

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                //一个App所有的任何一个Actvity进入生命周期的onCreate后会回调这个方法。
                if(activity.getClass().getSimpleName().equals("MainActiviy")){

                }
            }

            @Override
            public void onActivityStarted(Activity activity) {
                //一个App所有的任何一个Actvity进入生命周期的onStart后会回调这个方法。
            }

            @Override
            public void onActivityResumed(Activity activity) {
                //一个App所有的任何一个Actvity进入生命周期的onResumed后会回调这个方法。
            }

            @Override
            public void onActivityPaused(Activity activity) {
                //一个App所有的任何一个Actvity进入生命周期的onPaused后会回调这个方法。
            }

            @Override
            public void onActivityStopped(Activity activity) {
                //一个App所有的任何一个Actvity进入生命周期的onStopped后会回调这个方法。
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //一个App所有的任何一个Actvity进入生命周期的onDestroyed后会回调这个方法。
                System.out.println("");
            }
        });

    }
}
