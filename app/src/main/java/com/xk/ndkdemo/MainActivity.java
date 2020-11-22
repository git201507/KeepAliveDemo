package com.xk.ndkdemo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Process;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences mContextSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContextSp = this.getSharedPreferences( "testContextSp", Context.MODE_PRIVATE );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String msgStr = MyNdk.getString();
//                Snackbar.make(view, msgStr, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

//        int proccessId = mContextSp.getInt( "processId", 0 );
//        if(proccessId > 0){
//            if (isProcessExist(this, proccessId)) {
////                Process.killProcess(proccessId);
//                Log.i("Main", "服务  " + proccessId + "kill了");
//                //save processId
//                SharedPreferences.Editor editor = mContextSp.edit();
//                editor.putInt( "processId", 0);
//                editor.commit();
//                return;
//            }
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, ProcessService.class));
        } else {
            startService(new Intent(this, ProcessService.class));
        }

//        startService(new Intent(this, TestService.class));
    }

//    public static boolean isProcessExist(Context context, int pid) {
//
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningAppProcessInfo> lists ;
//        if (am != null) {
//            lists = am.getRunningAppProcesses();
//            for (ActivityManager.RunningAppProcessInfo appProcess : lists) {
//                if (appProcess.pid == pid) {
//                    Log.e("TAG","333333");
//                    return true;
//                }
//                else{
//                    Log.e("TAG","123123123");
//                }
//            }
//        }
//        return false;
//    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
