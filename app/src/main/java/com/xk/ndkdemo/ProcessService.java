package com.xk.ndkdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by aruba on 2020/5/20.
 */
public class ProcessService extends Service {
    public final static String TAG = ProcessService.class.getSimpleName();
    int ix=1;
    public static final int UPDATE = 0x1;
    public static final int RESTART = 0x3;
    SharedPreferences mContextSp;
    NotificationCompat.Builder mBuilder;
    static {
        System.loadLibrary("native-lib");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //消息处理者,创建一个Handler的子类对象,目的是重写Handler的处理消息的方法(handleMessage())
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE:
//                    Toast.makeText(getApplicationContext() ,"服务  " + msg.arg1, Toast.LENGTH_SHORT).show();
                    AppShortCutUtil.setCount(ix, getApplicationContext());
                    break;

                case RESTART:
                    Intent dialogIntent = new Intent(ProcessService.this, MainActivity.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //必须添加 Intent.FLAG_ACTIVITY_NEW_TASK
                    ProcessService.this.startActivity(dialogIntent);

                    Toast.makeText(getApplicationContext() ,"重启Activity  " + msg.arg1, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mContextSp = this.getSharedPreferences( "testContextSp", Context.MODE_PRIVATE );
        Wathcer wathcer = new Wathcer();

        final int pid= Process.myUid();
        wathcer.startDaemon(pid);

        //save processId
        SharedPreferences.Editor editor = mContextSp.edit();
        editor.putInt( "processId", pid);
        editor.commit();

        Timer timer = new Timer();
        //定时器
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        Log.i(TAG, ix + " 服务  " + pid);
                        ix++;
                        Message msg = new Message();
                        msg.what = UPDATE;
                        msg.arg1 = pid;
                        handler.sendMessageDelayed(msg, 100);
                    }
                }, 0, 1000 * 5);


            Message msg = new Message();
            msg.what = RESTART;
            msg.arg1 = pid;
            handler.sendMessageDelayed(msg, 3000);
//        Intent dialogIntent = new Intent(getBaseContext(), MainActivity.class);
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //必须添加 Intent.FLAG_ACTIVITY_NEW_TASK
//        getApplication().startActivity(dialogIntent);

//         setForeground();// 启动前台服务,并隐藏前台服务的通知
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "onStartCommand: -----");
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this,"10");

            NotificationManager notificationManager = (NotificationManager) getSystemService
                    (NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("10", "senblo", NotificationManager.IMPORTANCE_DEFAULT);
//                mBuilder.setChannelId("10");
                notificationManager.createNotificationChannel(channel);
            }

            mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_launcher));
            mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            mBuilder.setContentText("点击跳转至APP");
            mBuilder.setContentTitle("程序运行中");
            mBuilder.setOnlyAlertOnce(true);

            Intent newIntent = new Intent(this, ProcessService.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            mBuilder.setContentIntent(PendingIntent.getActivity(this, 10, newIntent, PendingIntent.FLAG_UPDATE_CURRENT));

            mBuilder.build().flags |= Notification.FLAG_ONGOING_EVENT;
            mBuilder.build().flags |= Notification.FLAG_NO_CLEAR;
        }

        startForeground(10, mBuilder.build());

        return START_STICKY;

    }
//    private final int PID = android.os.Process.myPid();
//
//    private ServiceConnection mConnection;
//    public void setForeground() {
//        // sdk < 18 , 直接调用startForeground即可,不会在通知栏创建通知
//        if (Build.VERSION.SDK_INT < 18) {
//            this.startForeground(PID, getNotification());
//            return;
//        }
//
//        if (null == mConnection) {
//            mConnection = new CoverServiceConnection();
//        }
//
//        this.bindService(new Intent(this, HelpService.class), mConnection,
//                Service.BIND_AUTO_CREATE);
//    }
//
//    private Notification getNotification() {
//        // 定义一个notification
//        Notification notification = new Notification();
//        Intent notificationIntent = new Intent(this, ProcessService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//        // notification.setLatestEventInfo(this, "Foreground", "正在运行哦",
//        // pendingIntent);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setAutoCancel(true).setContentTitle("ForegroundService")
//                .setContentText("正在运行哦").setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setWhen(System.currentTimeMillis()).setOngoing(true);
//        notification = builder.getNotification();
//        return notification;
//    }
//
//    private class CoverServiceConnection implements ServiceConnection {
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            Log.d(TAG, "ForegroundService: onServiceDisconnected");
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder binder) {
//            Log.d(TAG, "ForegroundService: onServiceConnected");
//
//            // sdk >= 18 的，会在通知栏显示service正在运行，这里不要让用户感知，所以这里的实现方式是利用2个同进程的service，利用相同的notificationID，
//            // 2个service分别startForeground，然后只在1个service里stopForeground，这样即可去掉通知栏的显示
//            Service helpService = ((HelpService.LocalBinder) binder)
//                    .getService();
//            ProcessService.this.startForeground(PID, getNotification());
//            helpService.startForeground(PID, getNotification());
//            helpService.stopForeground(true);
//
//            ProcessService.this.unbindService(mConnection);
//            mConnection = null;
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        unbindService(mConnection);
//        stopForeground(true);
//    }
}
