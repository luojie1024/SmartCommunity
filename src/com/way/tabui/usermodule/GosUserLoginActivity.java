package com.way.tabui.usermodule;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.way.tabui.cevicemodule.GosDeviceListActivity;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;
import com.way.tabui.pushmodule.GosPushManager;

@SuppressLint("HandlerLeak")
public class GosUserLoginActivity extends GosUserModuleBaseActivity implements OnClickListener {

	/** The et Name */
	private EditText etName;

	/** The et Psw */
	private EditText etPsw;

	/** The btn Login */
	private Button btnLogin;

	/** The tv Register */
	private TextView tvRegister;

	/** The tv Forget */
	private TextView tvForget;

	/** The ll Pass */
	private LinearLayout llPass;

	/** The cb Laws */
	private CheckBox cbLaws;

	/** The ll QQ */
	private LinearLayout llQQ;

	/** The Tencent */
	Tencent mTencent;

	/** The Scope */
	private String Scope = "get_user_info,add_t";

	/** The IUiListener */
	IUiListener listener;

	// The Intent
	Intent intent;

	// // The String
	// String name, psw;

	private enum handler_key {

		/** 登录 */
		LOGIN,

		/** 自动登录 */
		AUTO_LOGIN,

		/** 第三方登录 */
		THRED_LOGIN,

		/** 登陆成功. */
		LOGIN_SUCCESS,

		/** 登陆失败. */
		LOGIN_FAIL,

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			// 登录
			case LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				GizWifiSDK.sharedInstance().userLogin(etName.getText().toString(), etPsw.getText().toString());
				break;
			// 自动登录
			case AUTO_LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
				break;
			// 第三方登录
			case THRED_LOGIN:
				progressDialog.show();
				GosDeviceListActivity.loginStatus = 0;
				String[] openIDAndToken = (String[]) msg.obj;
				GizWifiSDK.sharedInstance().loginWithThirdAccount(GizThirdAccountType.GizThirdQQ, openIDAndToken[0],
						openIDAndToken[1]);
				break;
			// 登录成功
			case LOGIN_SUCCESS:

				Toast.makeText(GosUserLoginActivity.this, R.string.toast_login_successful, Toast.LENGTH_SHORT).show();
				String[] uidAndToken = (String[]) msg.obj;
				// TODO 绑定推送
//				GosPushManager.pushBindService(uidAndToken[1]);
				if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())) {
					spf.edit().putString("UserName", etName.getText().toString()).commit();
					spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
				}
				spf.edit().putString("Uid", uidAndToken[0]).commit();
				spf.edit().putString("Token", uidAndToken[1]).commit();

				intent = new Intent(GosUserLoginActivity.this, GosDeviceListActivity.class);
				intent.putExtra("ThredLogin", true);
				startActivity(intent);
				break;
			// 登录失败
			case LOGIN_FAIL:
				Toast.makeText(GosUserLoginActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;
			}
		};

	};

	GosPushManager gosPushManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 启动SDK
		GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), GosConstant.App_ID);

		// 只能选择支持其中一种
		 gosPushManager=new GosPushManager(GizPushType.GizPushJiGuang,this);//极光推送
		 
		if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
			Intent mainIntent = getIntent();
			String action = mainIntent.getAction();
			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
				finish();
				return;// finish()之后该活动会继续执行后面的代码！
			}
			// return;
		}
		setContentView(R.layout.activity_gos_user_login);
		mTencent = Tencent.createInstance(GosConstant.Tencent_APP_ID, this.getApplicationContext());
		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
		autoLogin();
	}

	private void autoLogin() {
		if (TextUtils.isEmpty(spf.getString("UserName", ""))) {
			return;
		}
		if (TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			return;
		}
		handler.sendEmptyMessage(handler_key.AUTO_LOGIN.ordinal());

	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		etName.setText("");
		etPsw.setText("");
	}

	private void initView() {
		etName = (EditText) findViewById(R.id.etName);
		etPsw = (EditText) findViewById(R.id.etPsw);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvRegister = (TextView) findViewById(R.id.tvRegister);
		tvForget = (TextView) findViewById(R.id.tvForget);
		llPass = (LinearLayout) findViewById(R.id.llPass);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);

		llQQ = (LinearLayout) findViewById(R.id.llQQ);

	}

	private void initEvent() {
		btnLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		llPass.setOnClickListener(this);

		llQQ.setOnClickListener(this);

		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String psw = etPsw.getText().toString();

				if (isChecked) {
					etPsw.setInputType(0x90);
				} else {
					etPsw.setInputType(0x81);
				}
				etPsw.setSelection(psw.length());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			if (TextUtils.isEmpty(etName.getText().toString())) {
				Toast.makeText(GosUserLoginActivity.this, R.string.toast_name_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(etPsw.getText().toString())) {
				Toast.makeText(GosUserLoginActivity.this, R.string.toast_psw_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
			handler.sendEmptyMessage(handler_key.LOGIN.ordinal());
			break;

		case R.id.tvRegister:
			intent = new Intent(GosUserLoginActivity.this, GosRegisterUserActivity.class);
			startActivity(intent);
			break;
		case R.id.tvForget:
			intent = new Intent(GosUserLoginActivity.this, GosForgetPasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.llPass:
			intent = new Intent(GosUserLoginActivity.this, GosDeviceListActivity.class);
			startActivity(intent);

			break;

		case R.id.llQQ:
			listener = new BaseUiListener() {
				protected void doComplete(JSONObject values) {
					Message msg = new Message();
					try {
						if (values.getInt("ret") == 0) {
							msg.what = handler_key.THRED_LOGIN.ordinal();
							String[] openIDAndToken = { values.getString("openid").toString(),
									(String) values.getString("access_token").toString() };
							msg.obj = openIDAndToken;
							handler.sendMessage(msg);
						} else {
							msg.what = handler_key.LOGIN_FAIL.ordinal();
							String loginFailed = (String) getText(R.string.toast_login_failed);
							msg.obj = loginFailed + "\n loginWithThirdAccount Failed";
							handler.sendMessage(msg);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			mTencent.login(this, Scope, listener);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
	}

	/**
	 * 用户登录回调
	 */
	@Override
	protected void didUserLogin(int error, String errorMessage, String uid, String token) {

		progressDialog.cancel();
		// TODO if (GosDeviceListActivity.isAnonymousLoging) {
		// GosDeviceListActivity.isAnonymousLoging = false;
		// return;
		// }
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
		if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
			return;
		}
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");
		Message msg = new Message();
		if (error != 0) {// 登陆失败
			msg.what = handler_key.LOGIN_FAIL.ordinal();
			String loginFailed = (String) getText(R.string.toast_login_failed);
			msg.obj = loginFailed + "\n" + errorMessage;
			handler.sendMessage(msg);
		} else {// 登陆成功
			GosDeviceListActivity.loginStatus = 1;
			msg.what = handler_key.LOGIN_SUCCESS.ordinal();
			String[] uidAndToken = { uid, token };
			msg.obj = uidAndToken;
			handler.sendMessage(msg);
		}
	}

	/**
	 * 解绑推送回调
	 * 
	 * @param result
	 */
	protected void didChannelIDUnBind(GizWifiErrorCode result) {
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
		}

		Log.i("Apptest", "UnBind:" + result.toString());
	};

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			String doubleClick = (String) getText(R.string.double_click);
			Toast.makeText(this, doubleClick, Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			this.finish();
			System.exit(0);
		}
	}

}
