package com.sundayfactory.testwizet.core;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.sundayfactory.testwizet.db.MyappDb;
import com.sundayfactory.testwizet.searchfragment.fragment_search_list.InterestingConfigChanges;
import com.sundayfactory.testwizet.searchfragment.fragment_search_list.PackageIntentReceiver;

/**
 * A custom Loader that loads all of the installed applications.
 */
public class AppListLoader extends AsyncTaskLoader<List<AppEntry>> {
	final InterestingConfigChanges mLastConfig = new InterestingConfigChanges();
	public final PackageManager mPm;
	public long DelayTime = 0;
	private MyappDb myappDb;
	/**
	 * Perform alphabetical comparison of application entry objects.
	 */
	public static final Comparator<AppEntry> ALPHA_COMPARATOR = new Comparator<AppEntry>() {
		private final Collator sCollator = Collator.getInstance();

		@Override
		public int compare(AppEntry object1, AppEntry object2) {
			return sCollator.compare(object1.getLabel(), object2.getLabel());
		}
	};
	List<AppEntry> mApps;
	PackageIntentReceiver mPackageObserver;

	public AppListLoader(Context context) {
		super(context);

		mPm = getContext().getPackageManager();
		myappDb = new MyappDb(context);
	}

	/**
	 * This is where the bulk of our work is done. This function is called
	 * in a background thread and should generate a new set of data to be
	 * published by the loader.
	 */
	@Override
	public List<AppEntry> loadInBackground() {
		DelayTime = System.currentTimeMillis();

		List<AppEntry> entries = new ArrayList<AppEntry>();
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> apps = mPm.queryIntentActivities(mainIntent, PackageManager.COMPONENT_ENABLED_STATE_DISABLED);
		HashMap<String, String> AddApps= new HashMap<String, String>();
		for (int i = 0; i < apps.size(); i++) {
			if(!AddApps.containsKey(apps.get(i).activityInfo.applicationInfo.packageName)){
				AddApps.put(apps.get(i).activityInfo.applicationInfo.packageName, apps.get(i).activityInfo.applicationInfo.packageName);
			}
		}
		
		
		
		
		int Db_size = myappDb.getAllAppListcount(MyappDb.TABLE_NAME_APPINFO);
		Log.i("park", "DB["+Db_size + "]apps[" + AddApps.size());
		if(Db_size == AddApps.size()){
			entries = myappDb.getAppList(getContext());
		}else{
			if (apps != null) {
				HashMap<String, String> AddMap= new HashMap<String, String>();
				for (int i = 0; i < apps.size(); i++) {
					AppEntry entry = new AppEntry(getContext(), apps.get(i).activityInfo.applicationInfo);
					entry.loadLabel(getContext());
					if(!AddMap.containsKey(entry.getApplicationInfo().packageName)){
						entries.add(entry);
						AddMap.put(entry.getApplicationInfo().packageName, entry.getApplicationInfo().packageName);
					}
				}
				myappDb.insertAppEntrys(entries);
			}
		}
		// Sort the list.
		Collections.sort(entries, ALPHA_COMPARATOR);
		Log.i("park", "ProcessTime = "+ ((System.currentTimeMillis() - DelayTime)/1000));
		// Done!
		return entries;
	}

	/**
	 * Called when there is new data to deliver to the client. The super
	 * class will take care of delivering it; the implementation here just
	 * adds a little more logic.
	 */
	@Override
	public void deliverResult(List<AppEntry> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We
			// don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		List<AppEntry> oldApps = apps;
		mApps = apps;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(apps);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (mApps != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(mApps);
		}

		// Start watching for changes in the app data.
		if (mPackageObserver == null) {
			mPackageObserver = new PackageIntentReceiver(this);
		}

		// Has something interesting in the configuration changed since we
		// last built the app list?
		boolean configChange = mLastConfig.applyNewConfig(getContext().getResources());

		if (takeContentChanged() || mApps == null || configChange) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(List<AppEntry> apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (mApps != null) {
			onReleaseResources(mApps);
			mApps = null;
		}

		// Stop monitoring for changes.
		if (mPackageObserver != null) {
			getContext().unregisterReceiver(mPackageObserver);
			mPackageObserver = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated with
	 * an actively loaded data set.
	 */
	protected void onReleaseResources(List<AppEntry> apps) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}
}
