package com.xk.ndkdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class HelpService extends Service {


    private static final String TAG = "HelpService";

    public class LocalBinder extends Binder {
        public HelpService getService() {
            return HelpService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "HelpService: onBind()");
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HelpService: onDestroy()");
    }

}
