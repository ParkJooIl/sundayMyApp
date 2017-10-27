package com.sundayfactory.testwizet.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

/**
 * Created by jipark on 2017-10-27.
 * 퍼미션을 체크하고 거부할경우 10회 사용시 한번씩 요청한다.
 *
 */

public class permissionUtils {

    private Context c;
    private String[] Permissions = new String[]{Manifest.permission.PACKAGE_USAGE_STATS , Manifest.permission.BIND_ACCESSIBILITY_SERVICE};
    public permissionUtils(Context c){

        this.c = c;
    }
    /**
     * 앱에 필요한 퍼미션 체크
     */
    public boolean Checkpermissions(){
        for(String item : Permissions){
            if(ContextCompat.checkSelfPermission(c , item) == PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    /**
     * 거부한 권한의 이름 반환
     * @return
     */
    public String CheckpermissionsName(){
        for(String item : Permissions){
            if(ContextCompat.checkSelfPermission(c , item) == PackageManager.PERMISSION_DENIED){
                return item;
            }
        }
        return "";
    }

}
