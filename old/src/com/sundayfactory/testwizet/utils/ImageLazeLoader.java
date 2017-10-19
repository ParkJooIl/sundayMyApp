package com.sundayfactory.testwizet.utils;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.sundayfactory.testwizet.core.AppEntry;

public class ImageLazeLoader {
	/*private Map<String, Bitmap> mImageList =  
			Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10,1.5f,true));*/
	LruCache<String, Bitmap> mImageList ;
	public ImageLazeLoader(Context context) {
		int memClass = (( ActivityManager )context.getSystemService( Context.ACTIVITY_SERVICE )).getMemoryClass();
		int cacheSize = 1024 * 1024 * memClass / 8;
		mImageList = new LruCache<String, Bitmap>( cacheSize );
	}
	
	public void onLoadImage(ImageView mView , AppEntry mAppEntry){
		String Filename = mAppEntry.getPackageName().replace(".", "")+".tmp";
		File FilePath = new File(mAppEntry.mContext.getFilesDir().getPath()+"/"+Filename);
		Bitmap map = (Bitmap) mImageList.get(FilePath.getPath());
		if(map == null){
			new AsyncImageLoader().execute(mAppEntry , mView);
			mView.setImageResource(android.R.drawable.sym_def_app_icon);
		}else{
			mView.setImageBitmap(map);
		}
		
	}
	
	public void onDestory(){
		mImageList.evictAll();
		mImageList = null;
	}
	private class AsyncImageLoader extends AsyncTask<Object, String, Drawable>{
		private AppEntry mAppEntry;
		private ImageView mImageView;
		@Override
		protected Drawable doInBackground(Object... params) {
			mAppEntry = (AppEntry)params[0];
			mImageView = (ImageView)params[1];
			if(mImageList == null || mImageView == null ||mAppEntry == null || !((String)mImageView.getTag()).equals(mAppEntry.getPackageName())){
				return null;
			}
			String Filename = mAppEntry.getPackageName().replace(".", "")+".tmp";
			File FilePath = new File(mAppEntry.mContext.getFilesDir().getPath()+"/"+Filename);
			Bitmap map = (Bitmap) mImageList.get(FilePath.getPath());
			if(map == null){
				if(!FilePath.exists()){
					UtlsFile.BitmapToFile(FilePath.getPath(), mAppEntry.getIcontoBitmap(), 100);
					map = UtlsFile.FileToBitmap(FilePath);
					mImageList.put(FilePath.getPath(), map);
				}else{
					map = UtlsFile.FileToBitmap(FilePath);
					mImageList.put(FilePath.getPath(), map);
				}
				try {
					Thread.sleep(300);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			return  new BitmapDrawable(mAppEntry.mContext.getResources(), map);
		}
	
		@Override
		protected void onPostExecute(Drawable result) {
			if(mImageView == null  || !((String)mImageView.getTag()).equals(mAppEntry.getPackageName())){
				return;
			}
			if(result != null){
				mImageView.setImageDrawable(result);
			}else{
				mImageView.setImageResource(android.R.drawable.sym_def_app_icon);
					
			}
			
		}
		
	}
	
	
}
