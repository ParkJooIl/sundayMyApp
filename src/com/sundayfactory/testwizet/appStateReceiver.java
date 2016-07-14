package com.sundayfactory.testwizet;

import com.sundayfactory.testwizet.core.AppEntry;
import com.sundayfactory.testwizet.db.MyappDb;
import com.sundayfactory.testwizet.searchfragment.fragment_search_list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class appStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("park", "onReceive = " + intent.getAction());
		MyappDb myappDb = new MyappDb(context);
		if(intent.getAction().equals(Intent.ACTION_PACKAGE_INSTALL)
				||intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)
				||intent.getAction().equals(Intent.ACTION_PACKAGE_CHANGED)
				||intent.getAction().equals(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
			){
			AppEntry mAppEntry = new AppEntry(context, intent.getData().getSchemeSpecificPart(), System.currentTimeMillis(), 0);
			if(mAppEntry.getApplicationInfo() != null){
				myappDb.insertAppEntry(mAppEntry);
			}
		}else if(intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)
				||intent.getAction().equals(Intent.ACTION_PACKAGE_FULLY_REMOVED)
				||intent.getAction().equals(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)
			){
			myappDb.deleteAppentry(intent.getData().getSchemeSpecificPart());
		}
		context.sendBroadcast(new Intent(fragment_search_list.PackageIntentReceiver.ACTION_ONCONTENT_CHANGED));
		
	}

}
