package com.way.tabui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.pushmodule.GosPushManager;
import com.way.tabui.settingsmodule.GosSettiingsActivity;

public class GosApplication extends Application {

   GosPushManager gosPushManager;
   public SharedPreferences spf;
	
   public void onCreate() {
//		
//		setDevice(device);
		spf = getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
		
		try {
			GosConstant.App_ID=spf.getString("appid", "a61ed92da3764cca848f3dbab8481149");
			GosConstant.App_Screct=spf.getString("appscrect", "57c13265403549ac83d828e50639c37a");
			GosConstant.device_ProductKey=spf.getString("prroductkey", "330b43e5cd9b4aa9a03fc97c5f6f52a4");
			// 启动SDK
            GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), GosConstant.App_ID,
                    GosConstant.App_Screct, null,null, false);
			// 只能选择支持其中一种
			 gosPushManager=new GosPushManager(GizPushType.GizPushJiGuang,this);//极光推送
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "设备配置错误，请重新配置绑定", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getApplicationContext(), GosSettiingsActivity.class);
			startActivity(intent);
		}
		super.onCreate();
	};
}
