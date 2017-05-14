package com.way.tabui.pushmodule;

import java.util.Set;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.way.tabui.commonmodule.BaiDuPushReceiver;
import com.way.tabui.commonmodule.GosConstant;

import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class GosPushManager {

	static GizPushType gizPushType;

	static Context context;

	public GosPushManager(GizPushType gizPushType, Context context) {
		super();
		GosPushManager.gizPushType = gizPushType;
		GosPushManager.context = context;

		if (GizPushType.GizPushJiGuang == gizPushType) {
			jPush();
		} else if (GizPushType.GizPushBaiDu == gizPushType) {
			bDPush();
		}
	}

	/** Channel_ID */
	public static String Channel_ID;

	/**
	 * 此方法完成了初始化JPush SDK等功能 但仍需在MainActivity的onResume和onPause添加相应方法
	 * JPushInterface.onResume(context); JPushInterface.onPause(context);
	 * 
	 * @param context
	 */
	public void jPush() {
		// 设置JPush调试模式
		JPushInterface.setDebugMode(true);

		// 初始化JPushSDK
		JPushInterface.init(context);

	}

	public void bDPush() {
		PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, GosConstant.BaiDuPush_AppKey);

	}

	/**
	 * 向云端绑定推送
	 * 
	 * @param Token
	 * @param Channel_ID
	 * @param gizPushType
	 */
	public static void pushBindService(String token) {

		if (GizPushType.GizPushJiGuang == gizPushType) {
			// 获取JPush的RegistrationID,即Channel_ID
			Channel_ID = JPushInterface.getRegistrationID(context);

			// 设定JPush类型
			JPushInterface.setAlias(context, Channel_ID, new TagAliasCallback() {
				@Override
				public void gotResult(int arg0, String arg1, Set<String> arg2) {
					if (arg0 == 0) {
						// Toast.makeText(context, arg1,
						// Toast.LENGTH_SHORT).show();
						Log.i("Apptest", "Alias: " + arg1);
					} else {
						Log.e("Apptest", "Result: " + arg0);
					}
				}
			});

		} else if (GizPushType.GizPushBaiDu == gizPushType) {
			// 获取BDPush的Channel_ID
			Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
		} else {
			return;
		}

		// TODO 绑定推送
		Log.i("Apptest", Channel_ID + "\n" + gizPushType.toString() + "\n" + token);
		GizWifiSDK.sharedInstance().channelIDBind(token, Channel_ID, gizPushType);
	}

	public static void pushUnBindService(String token) {

		if (token.isEmpty()) {
			return;
		}

		if (GizPushType.GizPushJiGuang == gizPushType) {
			// 获取JPush的RegistrationID,即Channel_ID
			Channel_ID = JPushInterface.getRegistrationID(context);
		} else if (GizPushType.GizPushBaiDu == gizPushType) {
			// 获取BDPush的Channel_ID
			Channel_ID = BaiDuPushReceiver.BaiDuPush_Channel_ID;
		} else {
			return;
		}
		// TODO 绑定推送
		Log.i("Apptest", Channel_ID + "\n" + gizPushType.toString() + "\n" + token);
		GizWifiSDK.sharedInstance().channelIDUnBind(token, Channel_ID);
	}
}
