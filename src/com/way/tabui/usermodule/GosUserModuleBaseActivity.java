package com.way.tabui.usermodule;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.way.tabui.commonmodule.GosBaseActivity;

import android.view.MenuItem;

public class GosUserModuleBaseActivity extends GosBaseActivity {

	private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

		/** 用于用户登录 */
		public void didUserLogin(int error, String errorMessage, String uid, String token) {
			GosUserModuleBaseActivity.this.didUserLogin(error, errorMessage, uid, token);
		};

		/** 用于手机验证码 */
		public void didRequestSendPhoneSMSCode(int result, String errorMessage) {
			GosUserModuleBaseActivity.this.didRequestSendPhoneSMSCode(result, errorMessage);
		};

		/** 用于用户注册 */
		public void didRegisterUser(int error, String errorMessage, String uid, String token) {
			GosUserModuleBaseActivity.this.didRegisterUser(error, errorMessage, uid, token);
		};

		/** 用于重置密码 */
		public void didChangeUserPassword(int error, String errorMessage) {
			GosUserModuleBaseActivity.this.didChangeUserPassword(error, errorMessage);
		};	


		/** 用于解绑推送 */
		public void didChannelIDUnBind(GizWifiErrorCode result) {
			GosUserModuleBaseActivity.this.didChannelIDUnBind(result);
		};
		
	};

	/**
	 * 用户登录回调
	 * 
	 * @param error
	 *            结果记录
	 * @param errorMessage
	 *            错误信息
	 * @param uid
	 *            用户ID
	 * @param token
	 *            授权令牌
	 */
	protected void didUserLogin(int error, String errorMessage, String uid, String token) {
	};

	/**
	 * 手机验证码回调
	 * 
	 * @param result
	 *            结果记录
	 * @param errorMessage
	 *            错误信息
	 */
	protected void didRequestSendPhoneSMSCode(int result, String errorMessage) {
	};

	/**
	 * 用户注册回调
	 * 
	 * @param error
	 *            结果记录
	 * @param errorMessage
	 *            错误信息
	 * @param uid
	 *            用户ID
	 * @param token
	 *            授权口令
	 */
	protected void didRegisterUser(int error, String errorMessage, String uid, String token) {
	};

	/**
	 * 重置密码回调
	 * 
	 * @param error
	 *            结果记录
	 * @param errorMessage
	 *            错误信息
	 */
	protected void didChangeUserPassword(int error, String errorMessage) {
	};
	
	
	/**
	 * 解绑推送回调
	 * 
	 * @param result
	 */
	protected void didChannelIDUnBind(GizWifiErrorCode result) {
	};


	@Override
	protected void onResume() {
		super.onResume();
		// 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
		GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
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
