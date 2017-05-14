package com.showmo.demo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/7/5.
 */
public class spUtil {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public spUtil(Context context){
        sp = context.getSharedPreferences("showmoSDKDemo",Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public void setUsername(String username){
        editor.putString("username",username);
        editor.commit();
    }

    public String getUsername(){
        return sp.getString("username","");
    }

    public void setWifi(String wifi){
        editor.putString("wifi",wifi);
        editor.commit();
    }

    public String getWifi(){
        return sp.getString("wifi","");
    }

    public void setWifiPsw(String wifipsw){
        editor.putString("wifipsw",wifipsw);
        editor.commit();
    }

    public String getWifiPsw(){
        return sp.getString("wifipsw","");
    }

    public void setisDemo(boolean isDemo){
        editor.putBoolean("isDemo",isDemo);
        editor.commit();
    }

    public boolean getisDemo(){
        return sp.getBoolean("isDemo",false);
    }
}
