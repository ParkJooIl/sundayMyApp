package com.sundayfactory.testwizet.manager;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.sundayfactory.testwizet.core.AppInfo;
import com.sundayfactory.testwizet.utils.FileLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jipark on 2017-10-23.
 */

public class AppUtils {
    private final static String tag = "AppUtils";
    /**
     * 화면 켜짐상태
     * @param c
     * @return
     */
    public static boolean isScreenOnOff(Context c){
        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= 20){
            if (pm.isInteractive()){
                return true;
            }else {
                return false;
            }
        }else if (android.os.Build.VERSION.SDK_INT < 20){
            if (pm.isScreenOn()){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    /**
     *  실행중인 앱 확인
     */
    public static AppInfo NowForeGroundAppCheck(Context c){
        AppInfo appInfo = new AppInfo();

            ActivityManager AM = (ActivityManager)c.getSystemService(Context.ACTIVITY_SERVICE);

            if(Build.VERSION.SDK_INT > 21){
/*                ActivityManager.RunningAppProcessInfo info =  AM.getRunningAppProcesses().get(0);
                appInfo.Package = info.processName;
                Log.d(tag, "NowForeGroundAppCheck Count : " + AM.getRunningAppProcesses().size());
                for (ActivityManager.RunningAppProcessInfo appProcess : AM.getRunningAppProcesses()) {
                    Log.d(tag, "NowForeGroundAppCheck: " + appProcess.processName);
                    Log.d(tag, "NowForeGroundAppCheck: " + appProcess.importance);
usagestatsmanager
                }*/
                UsageStatsManager USM = (UsageStatsManager)c.getSystemService(Context.USAGE_STATS_SERVICE);
                List<UsageStats> UsageApps = USM.queryUsageStats(UsageStatsManager.INTERVAL_BEST , System.currentTimeMillis() - 60000, System.currentTimeMillis());
                SimpleDateFormat sdflog = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
                if(UsageApps != null && UsageApps.size() > 0){
                    FileLog.fLog("===============================================================");
                    StringBuilder log = new StringBuilder();
                    for(UsageStats item : UsageApps){
                        log.append(item.getPackageName() + ":describeContents[" + item.describeContents() + "]getLastTimeStamp["+sdflog.format(new Date(item.getLastTimeStamp())) + "]getLastTimeUsed["+sdflog.format(new Date(item.getLastTimeUsed()))  + "]getTotalTimeInForeground[" + sdflog.format(new Date(item.getTotalTimeInForeground())));
                        log.append("\n");
                    }
                    FileLog.fLog(log.toString());


                    Log.i(tag , "UsageApps.size(" + UsageApps.size() +")");
                    appInfo.Package = UsageApps.get(UsageApps.size()-1).getPackageName() + "[" + UsageApps.size() + "]";
                }else{
                    FileLog.fLog("===================NULL============================================");

                    appInfo.Package = "NULL";
                }


            }else{
                appInfo.Package =  AM.getRunningTasks(0).get(0).topActivity.getPackageName();
            }


       return appInfo;
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
