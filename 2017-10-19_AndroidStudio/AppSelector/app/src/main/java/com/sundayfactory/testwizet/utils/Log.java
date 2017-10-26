package com.sundayfactory.testwizet.utils;

/**
 * Created by jipark on 2017-10-26.
 */

public class Log {
    private static final boolean isLog = true;
    public static void e(String TAG , String message){
        if(!isLog) return;
        android.util.Log.e(TAG , message);
    }
    public static void i(String TAG , String message){
        if(!isLog) return;
        android.util.Log.i(TAG , message);
    }

}
