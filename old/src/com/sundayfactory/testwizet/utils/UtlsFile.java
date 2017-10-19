package com.sundayfactory.testwizet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;

public class UtlsFile {
	public static File BitmapToFile(String path, Bitmap bitmap, int quality){
	
		FileOutputStream fos = null;
		File file = null;
		try {
			file = new File(path);
			fos = new FileOutputStream(file);
			
			bitmap.compress(CompressFormat.JPEG, quality, fos);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
		return file;
				
	}
	
	 public static Bitmap FileToBitmap(File f){
         
	        try {
	             
	            //Decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            FileInputStream stream1=new FileInputStream(f);
	            BitmapFactory.decodeStream(stream1,null,o);
	            stream1.close();
	             
	          //Find the correct scale value. It should be the power of 2.
	          
	            // Set width/height of recreated image
	            final int REQUIRED_SIZE=85;
	             
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE) 
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	             
	            //decode with current scale values
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inPreferredConfig = Bitmap.Config.ALPHA_8;
	            o2.inSampleSize=scale;
	            FileInputStream stream2=new FileInputStream(f);
	            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
	            stream2.close();
	            return bitmap;
	             
	        } catch (FileNotFoundException e) {
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
}
