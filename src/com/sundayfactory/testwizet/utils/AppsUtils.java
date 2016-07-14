package com.sundayfactory.testwizet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;

import com.sundayfactory.testwizet.R;

public class AppsUtils {

	public static void addShortcut(Context context, String Classname) {
		Log.i("park", "addShortcut + " + Classname);
		Intent shortcutIntent = new Intent();
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// shortcutIntent.setClassName(context, getClass().getName());
		shortcutIntent.setClassName(context, Classname);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context, R.drawable.ic_launcher);

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
		intent.putExtra("duplicate", false);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(intent);

	}

	public static void AppDelete(Activity ac, String pacekageName) {
		Uri packageURI = Uri.parse("package:" + pacekageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		ac.startActivity(uninstallIntent);
	}

	public static void AppStart(Activity ac, String pacekageName) {
		Log.i("park", "startApp = " + pacekageName);
		Intent LaunchIntent = ac.getPackageManager().getLaunchIntentForPackage(pacekageName);
		ac.startActivity(LaunchIntent);
	}
}
