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
			//如果已经修改配置数据
			boolean ismodify = spf.getBoolean("ismodify", true);
			if (spf.getBoolean("ismodify",false)) {
				GosConstant.App_ID=spf.getString("appid", "4d25e29be7e74a3aa18e2e7921cecb51");
				GosConstant.App_Screct=spf.getString("appscrect", "84d1094f9dfe4911a961e5ef79b8e4f0");
				String prroductkey = spf.getString("prroductkey", "0");
				GosConstant.Product_Key =spf.getString("prroductkey", "2246c7de027244038dc8bae975453eb6");
				String prroductscrect = spf.getString("prroductscrect", "0");
				GosConstant.Product_Secret=spf.getString("prroductscrect", "ff1a9d62c35b4a4b9ca0c8eea0d120a2");
			}
//            GosConstant.Product_Key="69353614e549438ead162509abefd243";
//            GosConstant.App_ID = "84ec3257a927470e97c7d66ff0558dc8";
//            GosConstant.App_Screct="beaca00e79a546f3b393ffdc81fdef72";
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
