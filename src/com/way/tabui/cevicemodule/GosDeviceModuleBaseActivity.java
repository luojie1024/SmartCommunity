package com.way.tabui.cevicemodule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.way.tabui.commonmodule.GosBaseActivity;

public class GosDeviceModuleBaseActivity extends GosBaseActivity {

	/** 设备列表 */
	protected static List<GizWifiDevice> deviceslist = new ArrayList<GizWifiDevice>();

	private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

		/** 用于设备列表 */
		public void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
			GosDeviceModuleBaseActivity.this.didDiscovered(result, deviceList);
		}

		/** 用于用户匿名登录 */
		public void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {
			GosDeviceModuleBaseActivity.this.didUserLogin(result, uid, token);
		}

		/** 用于设备解绑 */
		public void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
			GosDeviceModuleBaseActivity.this.didUnbindDevice(result, did);
		}

		/** 用于设备绑定 */
		public void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
			GosDeviceModuleBaseActivity.this.didBindDevice(result, did);
		}

		/** 用于绑定推送 */
		public void didChannelIDBind(GizWifiErrorCode result) {
			GosDeviceModuleBaseActivity.this.didChannelIDBind(result);
		}

	};

	/**
	 * 设备列表回调
	 * 
	 * @param result
	 * @param deviceList
	 */
	protected void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
	}

	/**
	 * 用户匿名登录回调
	 * 
	 * @param result
	 * @param uid
	 * @param token
	 */
	protected void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {
	}

	/**
	 * 设备解绑回调
	 * 
	 * @param result
	 * @param did
	 */
	protected void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
	}

	/**
	 * 设备绑定回调
	 * 
	 * @param result
	 * @param did
	 */
	protected void didBindDevice(GizWifiErrorCode result, java.lang.String did) {
	}

	/**
	 * 绑定推送回调
	 * 
	 * @param result
	 */
	protected void didChannelIDBind(GizWifiErrorCode result) {
	}

	/**
	 * 设备监听
	 */
	protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

		// 用于设备订阅

		@Override
		public void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1, boolean arg2) {
			GosDeviceModuleBaseActivity.this.didSetSubscribe(arg0, arg1, arg2);
		}
	};

	public GizWifiDeviceListener getGizWifiDeviceListener() {
		return gizWifiDeviceListener;
	}

	/**
	 * 设备订阅回调
	 * 
	 * @param result
	 * @param device
	 */
	protected void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1, boolean arg2) {
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
		GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
	}

	/**
	 * 
	 * @param result
	 * @param cloudServiceInfo
	 */
	protected void didGetCurrentCloudService(GizWifiErrorCode result,
			ConcurrentHashMap<String, String> cloudServiceInfo) {
	}

}
