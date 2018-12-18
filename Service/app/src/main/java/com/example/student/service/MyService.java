package com.example.student.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("log_MyService", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("log_MyService", "onStarCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("log_MyService", "onDestroy()");
    }

    @Override
    public IBinder onBind(Intent intent) {
       Log.d("log_MyService","onBind()");
       return null;
    }
}
