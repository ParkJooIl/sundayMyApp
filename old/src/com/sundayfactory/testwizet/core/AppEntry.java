package com.sundayfactory.testwizet.core;

import java.io.File;

import com.sundayfactory.testwizet.db.MyappDb;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

public class AppEntry {
	public PackageManager manager;
	private ApplicationInfo mInfo;
	private String packageName;
	private long lastStartTime = 0;
	private long startCount = 0;
	public File mApkFile = null;
	public String mLabel="";
	public Drawable mIcon;
	public boolean mMounted;
	public long mID = -1;
	public Context mContext;
	public ContentValues getValues(){
		ContentValues mContentValues = new ContentValues();
		
		mContentValues.put("packageName", getPackageName());
		mContentValues.put("lastStartTime", lastStartTime);
		mContentValues.put("startCount", startCount);
		mContentValues.put(MyappDb.COLUMN_TITLE, mLabel);
		
		return mContentValues;
	}
	public AppEntry(Context mContext, ApplicationInfo info) {
		this.mContext = mContext;
		manager = mContext.getPackageManager();
		mInfo = info;
		
		mApkFile = new File(info.sourceDir);
		loadLabel(mContext);
	}
	public AppEntry(Context mContext , Cursor mCursor)  {
		this.mContext = mContext;
		packageName = mCursor.getString(mCursor.getColumnIndex(MyappDb.COLUMN_PACKEGE));
		mID = mCursor.getLong(mCursor.getColumnIndex(MyappDb.COULMN_ID));
		mLabel = mCursor.getString(mCursor.getColumnIndex(MyappDb.COLUMN_TITLE));
		manager = mContext.getPackageManager();
		/*if(TextUtils.isEmpty(mLabel)){
			loadLabel(mContext);
		}*/
		try {
			mInfo = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public AppEntry(Context mContext , String Packegename , long lastStartTime , int startCount) {
		this.mContext = mContext;
		this.packageName = Packegename;
		this.lastStartTime = lastStartTime;
		this.startCount = startCount;
		manager = mContext.getPackageManager();
		try {
			mInfo = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			mApkFile = new File(mInfo.sourceDir);
			loadLabel(mContext);
			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			mInfo = null;
		}
		
	}
	

	public ApplicationInfo getApplicationInfo() {
		return mInfo;
	}

	public String getLabel() {
		if(mLabel == null){
			loadLabel(mContext);
		}
		return mLabel.trim();
	}
	public void setIndexWordPosition(int start , int end){
		mStart = start;
		mEnd = end;
	}
	int mStart = 0;
	int mEnd = 0;
	
	public String getHtmlText(){
		if(mStart <= 0 && mEnd <= 0){
			return getLabel();
		}else{

			String HtmlBody = "<font color='GRAY'>%s</Font><font color='BLUE'>%s</Font><font color='GRAY'>%s</Font>";
			String appLabel = getLabel();
			HtmlBody = String.format(HtmlBody,
					appLabel.substring(0,mStart),
					appLabel.substring(mStart,mStart + mEnd),
					appLabel.substring(mStart + mEnd,appLabel.length()));
			return HtmlBody;	
		}
	
	};
	public Drawable getIcon() {
		if(mApkFile == null){
			mApkFile = new File(mInfo.sourceDir);
			loadLabel(mContext);	
		}
		
		if (mIcon == null) {
			if (mApkFile.exists()) {
				mIcon = mInfo.loadIcon(manager);
				return mIcon;
			} else {
				mMounted = false;
			}
		} else if (!mMounted) {
			// If the app wasn't mounted but is now mounted, reload
			// its icon.
			if (mApkFile.exists()) {
				mMounted = true;
				mIcon = mInfo.loadIcon(manager);
				return mIcon;
			}
		} else {
			return mIcon;
		}

		return mContext.getResources().getDrawable(android.R.drawable.sym_def_app_icon);
	}

	public Bitmap getIcontoBitmap() {
		Drawable Icon = getIcon();
		Bitmap bitmap = Bitmap.createBitmap(Icon.getIntrinsicWidth(), Icon.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Icon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		Icon.draw(canvas);
		return bitmap;
	}

	@Override
	public String toString() {
		return mLabel.trim();
	}

	public void loadLabel(Context context) {
		if(mInfo == null){
			try {
				mInfo = mContext.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
				mApkFile = new File(mInfo.sourceDir);
				loadLabel(mContext);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				mInfo = null;
			}	
		}
		if (mLabel == null || !mMounted) {
			if (!mApkFile.exists()) {
				mMounted = false;
				mLabel = mInfo.packageName;
			} else {
				mMounted = true;
				CharSequence label = mInfo.loadLabel(context.getPackageManager());
				mLabel = label != null ? label.toString() : mInfo.packageName;

			}
		}
	}

	public String getPackageName() {
		if(packageName != null ){
			return packageName;
		}else
		if(mInfo != null){
			return mInfo.packageName;
		}
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}
