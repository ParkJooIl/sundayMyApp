package com.sundayfactory.testwizet.manager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;


import com.sundayfactory.testwizet.utils.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jipark on 2017-10-23.
 * 앱의 실행을 실시간 감시한다
 */

public class AppObserverService extends Service {
    private final String tag = "AppObserverService";
    private Handler _Handler = new Handler();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(tag , "onStartCommand");

        return Service.START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
