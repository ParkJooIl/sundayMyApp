package com.sundayfactory.testwizet.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by jipark on 2017-10-23.
 */

public class AppUtils {
    /**
     * 1. 화면이 켜져 있는상태 확인
     * 2. 실행중인 앱 확인
     */
    public static void NowForeGroundAppCheck(Context c){
        ActivityManager AM = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT > 20){
          ActivityManager.RunningAppProcessInfo info =  AM.getRunningAppProcesses().get(0);
        }else{
            AM.getRunningTasks(0).get(0).topActivity.getPackageName();
        }
    }
    /*public static void addShortcut(Context context, String Classname) {
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

    }*/

    /**
     * 삭제
      * @param ac
     * @param pacekageName
     */
    public static void AppDelete(Activity ac, String pacekageName) {
        Uri packageURI = Uri.parse("package:" + pacekageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        ac.startActivity(uninstallIntent);
    }

    /**
     * 시작
     * @param ac
     * @param pacekageName
     */
    public static void AppStart(Activity ac, String pacekageName) {
        Log.i("park", "startApp = " + pacekageName);
        Intent LaunchIntent = ac.getPackageManager().getLaunchIntentForPackage(pacekageName);
        ac.startActivity(LaunchIntent);
    }

    /**
     * 앱정보 실행 (삭제등 작업 가능);
     * @param c
     * @param PackageName
     */
    public static void LunchAppInfo(Context c, String PackageName){
        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + PackageName));
        c.startActivity(i);
    }
}
