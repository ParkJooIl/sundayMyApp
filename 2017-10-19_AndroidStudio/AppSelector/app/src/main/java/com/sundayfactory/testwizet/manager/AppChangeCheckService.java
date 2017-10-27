package com.sundayfactory.testwizet.manager;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;

import com.sundayfactory.testwizet.utils.Log;

public class AppChangeCheckService extends AccessibilityService {
    private static final String TAG= "AppChangeCheckService";
    private Handler mHandler = new Handler();

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.i(TAG ,"onServiceConnected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if(accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOWS_CHANGED){

        }

    }

    @Override
    public void onInterrupt() {

    }
}
