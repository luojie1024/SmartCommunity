package com.way.tabui.controlmodule;

import java.util.concurrent.ConcurrentHashMap;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.way.tabui.commonmodule.GosBaseActivity;

import android.view.MenuItem;

public class GosControlModuleBaseActivity extends GosBaseActivity {

	protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

		/** 用于设备订阅 */
		public void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1, boolean arg2) {
			GosControlModuleBaseActivity.this.didSetSubscribe(arg0, arg1, arg2);
		}

		/** 用于设备状态 */
		public void didReceiveData(GizWifiErrorCode arg0, GizWifiDevice arg1, ConcurrentHashMap<String, Object> arg2,
				int arg3) {
			GosControlModuleBaseActivity.this.didReceiveData(arg0, arg1, arg2, arg3);
		}

		/** 用于设备硬件信息 */
		public void didGetHardwareInfo(GizWifiErrorCode arg0, GizWifiDevice arg1,
				ConcurrentHashMap<String, String> arg2) {
			GosControlModuleBaseActivity.this.didGetHardwareInfo(arg0, arg1, arg2);
		}

		/** 用于修改设备信息 */
		public void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
			GosControlModuleBaseActivity.this.didSetCustomInfo(arg0, arg1);
		}

		/** 用于设备状态变化 */
		public void didUpdateNetStatus(GizWifiDevice arg0, GizWifiDeviceNetStatus arg1) {
			GosControlModuleBaseActivity.this.didUpdateNetStatus(arg0, arg1);
		}
	};

	/**
	 * 设备订阅回调
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	protected void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1, boolean arg2) {
	}

	/**
	 * 设备状态回调
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	protected void didReceiveData(GizWifiErrorCode arg0, GizWifiDevice arg1, ConcurrentHashMap<String, Object> arg2,
			int arg3) {
	}

	/**
	 * 设备硬件信息回调
	 * 
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	protected void didGetHardwareInfo(GizWifiErrorCode arg0, GizWifiDevice arg1,
			ConcurrentHashMap<String, String> arg2) {
	}

	/**
	 * 修改设备信息回调
	 * 
	 * @param arg0
	 * @param arg1
	 */
	protected void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
	}

	/**
	 * 设备状态变化回调
	 * 
	 * @param arg0
	 * @param arg1
	 */
	protected void didUpdateNetStatus(GizWifiDevice arg0, GizWifiDeviceNetStatus arg1) {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
