package com.sundayfactory.testwizet.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedUtils {
	private static SharedPreferences mPreferences;
	private final static String TAG_SHARED = "sunday";
	private final static String TAG_RESET = "TAG_RESET";
	
	
	public static boolean getBoolean(Context mContext, String _key) {
		 Init(mContext);
		return mPreferences.getBoolean(_key, false);
	}
	private static void Init(Context mContext){
		if (mPreferences == null) {
			mPreferences = mContext.getSharedPreferences(TAG_SHARED, Activity.MODE_WORLD_READABLE | Activity.MODE_WORLD_WRITEABLE);
		}
	}
	public static void setBoolean (Context mContext , String _key, boolean Value){
		Init(mContext);
		SharedPreferences.Editor mEditer = mPreferences.edit();
		mEditer.putBoolean(_key, Value);
		mEditer.commit();
	}
	public static void setInt (Context mContext , String _key, int Value){
		Init(mContext);
		SharedPreferences.Editor mEditer = mPreferences.edit();
		mEditer.putInt(_key, Value);
		mEditer.commit();
	}
	public static int getInt(Context mContext , String _key , int Default){
		Init(mContext);
		return mPreferences.getInt(_key, Default);
	}
	public static class ListMode{
		public final static String KEY_LISTMODE = "TAG_LISTMODE";
		public final static int LISTMODE_LIST = 1;
		public final static int LISTMODE_GRID = 2;
		public static int LIST_MODE  = LISTMODE_LIST;  
	}
	public static class FontSize{
		public final static String KEY = "FontSize";
		public final static int Font_NOMAL = 20;
		public final static int Font_Large = 30;
		public final static int Font_BigLarge = 40;
		public static int FONT_SIZE = Font_NOMAL;

	}
	
}
