package com.sundayfactory.testwizet.core;

import java.lang.reflect.Field;

import android.database.Cursor;

public class ObjectCore {
	public void getCursor(Cursor mCursor) throws IllegalArgumentException, IllegalAccessException {
		Field[] Fileds = this.getClass().getFields();
		for(Field mField : Fileds){
			setField(mField, mCursor);
		}
	}
	private void setField(Field mField , Cursor mCursor) throws IllegalArgumentException, IllegalAccessException{
		int index = mCursor.getColumnIndex(mField.getName());	
		Object mValue = null;
		if(mField.get(this) instanceof Integer){
			mValue = mCursor.getInt(index);
		}else if(mField.get(this) instanceof Long){
			mValue = mCursor.getLong(index);
			
		}else if(mField.get(this) instanceof Double){
			mValue = mCursor.getDouble(index);

		}else if(mField.get(this) instanceof String){
			mValue = mCursor.getString(index);
			
		}else if(mField.get(this) instanceof Float){
			mValue = mCursor.getFloat(index);
			
		}		
		mField.set(this, mValue);
			
	}
}
