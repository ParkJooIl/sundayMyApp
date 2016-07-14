package com.sundayfactory.testwizet.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sundayfactory.testwizet.core.AppEntry;
import com.sundayfactory.testwizet.core.appInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class MyappDb {
	public static final String TABLE_NAME_APPINFO = "appinfo";
	public static final String COULMN_ID = "COULMN_ID";
	public static final String COLUMN_PACKEGE = "packageName";
	public static final String COLUMN_LAST_START_TIME = "lastStartTime";
	public static final String COLUMN_TITLE = "title";

	public static final String COLUMN_START_COUNT = "startCount";
	public final String[] COLUMN_ARRAY = new String[]{COULMN_ID , COLUMN_PACKEGE , COLUMN_LAST_START_TIME , COLUMN_START_COUNT , COLUMN_TITLE};
	
	private SQLiteDatabase mSqlDb;
	private SqlHelper mSqlHelper;
	public MyappDb(Context context) {
		// TODO Auto-generated constructor stub
		mSqlHelper = new SqlHelper(context, "MyAppDb", null, 3);
	}
	public long upgradeInsertMyINFO(appInfo info){
		long _ID = -1;
		try {
			mSqlDb = mSqlHelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(COLUMN_PACKEGE, info.getPackage());
			contentValues.put(COLUMN_START_COUNT, info.getCount());
			
			
			String whereClause  = COLUMN_PACKEGE + " = ? ";
			String[] whereArgs = new String[]{info.getPackage()};
			_ID = mSqlDb.update(TABLE_NAME_APPINFO, contentValues, whereClause, whereArgs);
			if(_ID <= 0){
				_ID = 	mSqlDb.insert(TABLE_NAME_APPINFO,null,contentValues);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _ID;
	}
	public List<AppEntry> getAppList(Context mContext){
		List<AppEntry> mAppEntries = new ArrayList<AppEntry>();
		try {
			mSqlDb = mSqlHelper.getWritableDatabase();
			Cursor mCursor = ReadQuery("SELECT * FROM "+TABLE_NAME_APPINFO, null);
			
			if(mCursor != null && mCursor.getCount() > 0){
				mCursor.moveToFirst();
				do {
					
					AppEntry app = new AppEntry(mContext , mCursor);
					
					
					if(app.getApplicationInfo() == null){
						mSqlDb.delete(TABLE_NAME_APPINFO, COLUMN_PACKEGE + " =?", new String[]{app.getPackageName()});
					}else{
						if(TextUtils.isEmpty(app.mLabel)){
							app.loadLabel(mContext);
						}
						mAppEntries.add(app);
					}
				} while (mCursor.moveToNext());
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return mAppEntries;
	}
	private Cursor ReadQuery(String SqlQuery , String[] arg){
		try {
			mSqlDb = mSqlHelper.getReadableDatabase();
			return mSqlDb.rawQuery(SqlQuery, arg);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public int getAllAppListcount(String TableName){
		Cursor mCursor = ReadQuery("SELECT * FROM "+TableName, null);
		if(mCursor == null){
			return -1;
		}
		return mCursor.getCount();
		
	}
	public void insertAppEntrys(List<AppEntry> mAppEntries){
		try {
			Log.i("park","=============="+mAppEntries.size()+"===============");
			
			mSqlDb = mSqlHelper.getWritableDatabase();
			mSqlDb.execSQL("delete from " + TABLE_NAME_APPINFO);
			for(AppEntry mEntry : mAppEntries){
				ContentValues values = mEntry.getValues();
				values.put(MyappDb.COULMN_ID, mEntry.mID);
				long index = -1;
				index = mSqlDb.update(TABLE_NAME_APPINFO, values, COLUMN_PACKEGE + " = ? " , new String[]{mEntry.getPackageName()});
				if(index == -1 || index == 0){
					Log.i("park", "insert ["+mEntry.getPackageName() + "]" + index);
					index = mSqlDb.insert(TABLE_NAME_APPINFO, null, mEntry.getValues());
					
				}else{
					Log.i("park", "update ["+mEntry.getPackageName() + "]" + index);
						
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			mSqlDb.close();
		}
	}
	
	public void insertAppEntry(AppEntry mAppEntry){
		try {
			mSqlDb = mSqlHelper.getWritableDatabase();
				ContentValues values = mAppEntry.getValues();
				values.put(MyappDb.COULMN_ID, mAppEntry.mID);
				long index = -1;
				if(mAppEntry.mID == -1){
					index = mSqlDb.update(TABLE_NAME_APPINFO, values, COLUMN_PACKEGE + " = ? " , new String[]{mAppEntry.getPackageName()});
				}else{
					index = mSqlDb.update(TABLE_NAME_APPINFO, values, COULMN_ID + " = '"+mAppEntry.mID+"'" , null);
				}
				if(index == -1 || index == 0){
					Log.i("park", "insert ["+mAppEntry.getPackageName() + "]" + index);
					index = mSqlDb.insert(TABLE_NAME_APPINFO, null, mAppEntry.getValues());
					
				}else{
					Log.i("park", "update ["+mAppEntry.getPackageName() + "]" + index);
						
				}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			mSqlDb.close();
		}
		
	}
	public void deleteAppentry(String name){
		try {
			
			mSqlDb = mSqlHelper.getWritableDatabase();
			mSqlDb.delete(TABLE_NAME_APPINFO, COLUMN_PACKEGE +" = ? ", new String[]{name});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class SqlHelper extends SQLiteOpenHelper {
		public SqlHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String query = 
					"CREATE TABLE " + TABLE_NAME_APPINFO + " (" + 
					COULMN_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT , "
					+ COLUMN_PACKEGE + " TEXT  , "
					+ COLUMN_START_COUNT + " REAL ,  "
					+ COLUMN_LAST_START_TIME + " REAL , "
					+ COLUMN_TITLE + " TEXT  "
					
					+")";
			db.execSQL(query);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(newVersion > oldVersion){
				Log.i("park", "Reset DB");
				db.execSQL("delete from " + TABLE_NAME_APPINFO);
				
				Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_APPINFO, null); // grab cursor for all data
				int deleteStateColumnIndex = cursor.getColumnIndex(COLUMN_TITLE);  // see if the column is there
				if (deleteStateColumnIndex < 0) { 
				    // missing_column not there - add it
				    db.execSQL("ALTER TABLE "+TABLE_NAME_APPINFO+ " ADD COLUMN "+COLUMN_TITLE+" TEXT ;");
				}
				
			}

		}
		
		private String getColumnString(Object mObject) throws IllegalArgumentException, IllegalAccessException{
			String Columns = "";
			Field[] mFields = mObject.getClass().getFields();
			for(Field mField : mFields){
				if(mField.get(mObject) instanceof Integer){
					Columns += mField.getName() + "	INTEGER ,";
				}else if(mField.get(mObject) instanceof Float){
					Columns += mField.getName() + "	REAL ,";
				}else if(mField.get(mObject) instanceof String){
					Columns += mField.getName() + "	TEXT ,";
					
				} 	
			}
			
			return Columns;
		}
	}
	
	
	
}
