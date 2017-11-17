package com.sundayfactory.testwizet.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jipark on 2017-07-06.
 */

public class FileLog {
    /**
     * 서버 전송용 파일로그
     */
    public static final String LOG_EXCEPTION="AppSelectLog";

    /**
     * 디바이스 저장용 로그
     */
    public static final String LOG_DEVICE="AppSelectLog";



    /**
     *  로그 청소
     */
    public static void LogClean(){
        fLog("모든 로그 삭제 ");
        File folder = new File( Environment.getExternalStorageDirectory().toString() + File.separator +LOG_DEVICE);
        if(!folder.exists()){
            return;
        }
        File[] LogFiles = folder.listFiles();
        for(File Log : LogFiles){
            Log.delete();
        }
        // 익셉션 로그 삭제
        folder = new File( Environment.getExternalStorageDirectory().toString() + File.separator +LOG_EXCEPTION);
        if(!folder.exists()){
            return;
        }
        LogFiles = folder.listFiles();
        for(File Log : LogFiles){
            Log.delete();
        }

    }

    /**
     * 디바이스 파일 저장용
     * @param text
     */
    public static void fLog(String text) {
            fLog(text , LOG_DEVICE);
        }

    public static void fLog(String text , String Filename){

        if(TextUtils.isEmpty(text)){
            text = "내용 없음";
        }
            Log.i("FileLog" , text);
        File folder = new File( Environment.getExternalStorageDirectory().toString() + File.separator +Filename);
        if(!folder.exists()){
            folder.mkdir();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        File log = new File(folder , "CremaSoundLog_"+sdf.format(new Date(System.currentTimeMillis())));
        if(!log.exists()){
            try {
                log.createNewFile();
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(log)));
                SimpleDateFormat sdflog = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
                pw.print(sdflog.format(new Date(System.currentTimeMillis()))  );
                pw.print("\r\n");
                pw.print(text);
                pw.print("\r\n");
                pw.flush();;
                pw.close();;

            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(log , true)));
                SimpleDateFormat sdflog = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
                pw.print(sdflog.format(new Date(System.currentTimeMillis()))  );
                pw.print("\r\n");
                pw.print(text);
                pw.print("\r\n");
                pw.flush();;
                pw.close();;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    /**
     * 서버 전송용 파일로그
     * @param text
     */
    public static void fLoge(String text){
        fLog(text ,  LOG_EXCEPTION);
    }
    public static void fLog(Exception stackTrace) {
        StringBuilder error = new StringBuilder();
        error.append(stackTrace.getLocalizedMessage());
        error.append("\r\n");
        for(StackTraceElement item : stackTrace.getStackTrace()){
            error.append(item.toString());
            error.append("\r\n");
        }
        fLog(error.toString() ,  LOG_EXCEPTION);
    }

    public static void sqliteExport(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

            String currentDBPath = "/data/com.bdb.crema.cremawall/databases/cremawall.db";
                String backupDBPath = "/CremaWallLog/contacts.sqlite_" + sdf.format(new Date(System.currentTimeMillis()));
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
