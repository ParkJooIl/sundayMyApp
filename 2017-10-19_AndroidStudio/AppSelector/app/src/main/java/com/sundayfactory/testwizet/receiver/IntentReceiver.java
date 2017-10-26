package com.sundayfactory.testwizet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.AppOpsManagerCompat;

import com.sundayfactory.testwizet.manager.AppObserverService;
import com.sundayfactory.testwizet.utils.Log;

/**
 * Created by jipark on 2017-10-26.
 */

public class IntentReceiver extends BroadcastReceiver{
    private final String tag = "IntentReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction() != null){
            Log.e(tag , intent.getAction());
            Intent ServiceCheck = new Intent(context , AppObserverService.class);
            context.startService(ServiceCheck);

        }
    }
}
