package com.sundayfactory.testwizet.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

/**
 * @author coffee
 */
public class ShortCutUtils {

	static final String ACTION_INSTALL = "com.android.launcher.action.INSTALL_SHORTCUT";
	static final String ACTION_UNINSTALL = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	/**
	 * 创建快捷方式 需要权限 <uses-permission
	 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
	 */

	public static void createShortcut(Activity context, String startupActivityClass, String appName, Bitmap icon) {
		Log.i("park", "createShortcut " + startupActivityClass);
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		shortcut.putExtra("duplicate", false); // 不允许重复创建
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setClassName(context, startupActivityClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, icon);
		context.sendBroadcast(shortcut);
	}

	public static void createShortcut(Activity context, Class<?> startupActivityClass, int appName, int icon) {
		Log.i("park", "createShortcut " + startupActivityClass);
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
		// context.getString(appName));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		shortcut.putExtra("duplicate", false); // 不允许重复创建

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.setClass(context.getApplicationContext(), startupActivityClass);

		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

		context.sendBroadcast(shortcut);
	}

	/**
	 * 删除快捷方式 需要权限 <uses-permission
	 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT"/>
	 */
	public static void delShortcut(Activity context) {
		/*
		 * //该代码不管用，决定采用从数据库中删除的办法 Intent shortcut = new
		 * Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		 * shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
		 * context.getString(R.string.app_name)); ComponentName
		 * localComponentName = new ComponentName(context.getPackageName(),
		 * "."+context.getLocalClassName()); Intent intent = new
		 * Intent(Intent.ACTION_MAIN).setComponent(localComponentName);
		 * shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		 * context.sendBroadcast(shortcut); return;
		 */
		String dbPath = "/data/data/com.android.launcher/databases/launcher.db";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
		db.delete("favorites", "iconPackage=?", new String[] { context.getApplication().getPackageName() });
		db.close();

	}

	/**
	 * 检测快捷方式是否存在 需要权限<uses-permission
	 * android:name="com.android.launcher.permission.READ_SETTINGS"/>
	 * 
	 * @param context
	 */
	public static boolean isExistShortcut(Activity context) {
		boolean isInstallShortcut = false;
		final ContentResolver cr = context.getContentResolver();
		String AUTHORITIES = "";
		if (android.os.Build.VERSION.SDK_INT < 8) {
			AUTHORITIES = "com.android.launcher2.settings";
		} else {
			AUTHORITIES = "com.android.launcher.settings";
		}
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI, new String[] { "iconPackage" }, "iconPackage=?", new String[] { context.getApplication().getPackageName() }, null);
		if (c != null) {
			if (c.getCount() > 0) {
				isInstallShortcut = true;
			}
			c.close();
		}
		return isInstallShortcut;
	}

	public static void delShortcutFromDesktop(Context paramContext, String paramString1, String paramString2, String paramString3) {
		Intent localIntent1 = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		String str = paramString3;
		PackageManager localPackageManager = paramContext.getPackageManager();
		int i = 8320;
		try {
			ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(paramString1, i);
			if (str == null)
				str = localPackageManager.getApplicationLabel(localApplicationInfo).toString();
			localIntent1.putExtra("android.intent.extra.shortcut.NAME", str);
			ComponentName localComponentName = new ComponentName(paramString1, paramString2);
			Intent localIntent2 = new Intent("android.intent.action.MAIN").setComponent(localComponentName);
			localIntent1.putExtra("android.intent.extra.shortcut.INTENT", localIntent2);
			paramContext.sendBroadcast(localIntent1);
			return;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			while (true)
				localNameNotFoundException.printStackTrace();
		}
	}
}