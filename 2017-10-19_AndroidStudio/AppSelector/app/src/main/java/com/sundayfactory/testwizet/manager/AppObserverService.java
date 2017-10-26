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
    private Timer _AppObserverTimer;
    private TimerTask _AppObserverTask = new TimerTask() {
        @Override
        public void run() {
            if(AppUtils.isScreenOnOff(AppObserverService.this)){

                _Handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AppObserverService.this ,"NowForeGroundAppCheck:" + AppUtils.NowForeGroundAppCheck(AppObserverService.this) , Toast.LENGTH_LONG ).show();
                    }
                });
                Log.e(tag , "NowForeGroundAppCheck on");
            }else{
                Log.e(tag , "NowForeGroundAppCheck off");
            }
        }
    };
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(tag , "onStartCommand");
        startObserver();
        return Service.START_STICKY;
    }
    private void startObserver(){
        if(_AppObserverTimer == null){
            _AppObserverTimer = new Timer();
            _AppObserverTimer.schedule(_AppObserverTask , 1000 , 30000);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
