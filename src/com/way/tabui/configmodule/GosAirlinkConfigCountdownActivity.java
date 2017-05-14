package com.way.tabui.configmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizWifiConfigureMode;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.enumration.GizWifiGAgentType;
import com.way.tabui.gokit.R;
import com.way.tabui.view.RoundProgressBar;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmDevice;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmBinderManager;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmBindListener;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

@SuppressLint("HandlerLeak")
public class GosAirlinkConfigCountdownActivity extends
		GosConfigModuleBaseActivity implements View.OnClickListener {

	/** The tv Time */
	private TextView tvTimer;

	/** The rpb Config */
	private RoundProgressBar rpbConfig;

	/** 倒计时 */
	int secondleft = 120;

	/** The timer */
	Timer timer;

	/** 配置用参数 */
	String workSSID, workSSIDPsw;

	/** The String */
	String timerText;
	IXmSystem xmSystem;
	IXmBinderManager xmBinderManager;

	Button btn_next;

	boolean isGiz, islc;
	List<GizWifiGAgentType> modeList, modeDataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_airlink_config_countdown);
		// 设置ActionBar
		setActionBar(false, false, R.string.configcountDown_title);

		initData();
		initView();
		if (isGiz) {
			startAirlink();
		}

		if (islc) {
			init();
			btn_next.setVisibility(View.VISIBLE);
		}
		handler.sendEmptyMessage(handler_key.START_TIMER.ordinal());
	}

	private void init() {

		xmSystem = XmSystem.getInstance();
		xmBinderManager = xmSystem.xmGetBinderManager();
		xmBinderManager.setOnBindListener(xmBindListener);
		xmBinderManager.beginWork(GosAirlinkConfigCountdownActivity.this,
				workSSID, workSSIDPsw);
		// xmSystem.xmInit(this, "CN", new OnXmSimpleListener() {
		// @Override
		// public void onErr(XmErrInfo info) {
		// Log.v("AAAAA", "init Fail");
		// }
		//
		// @Override
		// public void onSuc() {
		// Log.v("AAAAA", "init Suc");
		// }
		// });

		// try {
		// xmSystem.xmLogin("13135367953", "chen162858", new
		// OnXmListener<XmAccount>() {
		// @Override
		// public void onSuc(XmAccount outinfo) {
		// // handler.sendEmptyMessage(0x123);
		// // sp.setUsername(et_username.getText().toString());
		// xmBinderManager = xmSystem.xmGetBinderManager();
		// xmBinderManager.setOnBindListener(xmBindListener);
		// xmBinderManager.beginWork(GosAirlinkConfigCountdownActivity.this,
		// workSSID, workSSIDPsw);
		//
		// }
		//
		// @Override
		// public void onErr(XmErrInfo info) {
		//
		// // handler.sendEmptyMessage(0x124);
		// }
		// });
		// } catch (Exception e) {
		// e.printStackTrace();
		// // handler.sendEmptyMessage(0x124);
		// }

	}

	private void initView() {
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		rpbConfig = (RoundProgressBar) findViewById(R.id.rpbConfig);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);

	}

	private void initData() {
		workSSID = spf.getString("workSSID", "");
		workSSIDPsw = spf.getString("workSSIDPsw", "");

		Intent intent = getIntent();
		isGiz = intent.getBooleanExtra("isGiz", false);
		islc = intent.getBooleanExtra("islc", false);

		modeDataList = new ArrayList<GizWifiGAgentType>();
		modeDataList.add(GizWifiGAgentType.GizGAgentESP);
		modeDataList.add(GizWifiGAgentType.GizGAgentHF);
		modeDataList.add(GizWifiGAgentType.GizGAgentMXCHIP);
		modeDataList.add(GizWifiGAgentType.GizGAgentRTK);
		modeDataList.add(GizWifiGAgentType.GizGAgentWM);
		modeDataList.add(GizWifiGAgentType.GizGAgentQCA);
		modeDataList.add(GizWifiGAgentType.GizGAgentTI);
		modeDataList.add(GizWifiGAgentType.GizGAgentBL);
		modeDataList.add(GizWifiGAgentType.GizGAgentFSK);
		modeList = new ArrayList<GizWifiGAgentType>();
		modeList.add(modeDataList.get(spf.getInt("MODE", 0)));

	}

	private void startAirlink() {
		GizWifiSDK.sharedInstance().setDeviceOnboarding(workSSID, workSSIDPsw,
				GizWifiConfigureMode.GizWifiAirLink, null, 120, modeList);
	}

	private enum handler_key {

		/**
		 * 倒计时提示
		 */
		TIMER_TEXT,

		/**
		 * 倒计时开始
		 */
		START_TIMER,

		/**
		 * 配置成功
		 */
		SUCCESSFUL,

		/**
		 * 配置失败
		 */
		FAILED,
		/**
		 * 配置摄像头成功
		 */
		SUCCESSFULCL,
		/**
		 * 摄像头已经添加过
		 */
		ADDBYSELF,
		/**
		 * 配置摄像头失败
		 */
		FAILEDCL,
		/**
		 * 配置超时
		 */
		TIMEOUT,

	}

	boolean flaggiz = false;
	boolean flaglc = false;
	/**
	 * The handler.
	 */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];

			switch (key) {
			case TIMER_TEXT:
				tvTimer.setText(timerText);
				break;

			case START_TIMER:
				isStartTimer();
				break;

			case SUCCESSFUL:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加网关设备成功", Toast.LENGTH_SHORT).show();
				flaggiz = true;
				if ((isGiz) && (!islc))
					finish();
				// task.cancel();
				// timer.cancel();
				if (flaggiz && flaglc)
					finish();
				// task.cancel();
				// timer.cancel();
				break;

			case FAILED:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加网关设备失败", Toast.LENGTH_SHORT).show();
				if ((isGiz) && (!islc)) {
					Intent intent = new Intent(
							GosAirlinkConfigCountdownActivity.this,
							GosDeviceReadyActivity.class);
					startActivity(intent);
					finish();
				}
				break;
			case SUCCESSFULCL:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加摄像头设备成功", Toast.LENGTH_SHORT).show();
				flaglc = true;
				if ((!isGiz) && (islc))
					finish();
				// task.cancel();
				// timer.cancel();
				if (flaggiz && flaglc)
					finish();
				// task.cancel();
				// timer.cancel();
				break;

			case ADDBYSELF:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加摄像头设备失败", Toast.LENGTH_SHORT).show();
				break;

			case FAILEDCL:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加摄像头设备失败", Toast.LENGTH_SHORT).show();
				break;
			case TIMEOUT:
				Toast.makeText(GosAirlinkConfigCountdownActivity.this,
						"添加设备超时", Toast.LENGTH_LONG).show();
				finish();
				// task.cancel();
				// timer.cancel();
				break;

			default:
				break;

			}
		}

	};

	// 屏蔽掉返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			quitAlert(this, timer);
			return true;
		}
		return false;
	}

	TimerTask task;

	// 倒计时
	public void isStartTimer() {

		secondleft = 120;
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				secondleft--;
				rpbConfig.setProgress((120 - secondleft) * (100 / 120.0));
				if (secondleft == 116) {
					timerText = (String) getText(R.string.searching_device);
					handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
				} else if (secondleft == 60) {
					timerText = (String) getText(R.string.searched_device);
					handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
				} else if (secondleft == 40) {
					timerText = (String) getText(R.string.tring_join_device);
					handler.sendEmptyMessage(handler_key.TIMER_TEXT.ordinal());
				} else if (secondleft == 0) {
					// timerText = (String) getText(R.string.tring_join_device);
					handler.sendEmptyMessage(handler_key.TIMEOUT.ordinal());
				}
			}
		};
		timer.schedule(task, 1000, 1000);

	}

	protected void didSetDeviceOnboarding(GizWifiErrorCode result, String mac,
			String did, String productKey) {

		if (GizWifiErrorCode.GIZ_SDK_DEVICE_CONFIG_IS_RUNNING == result) {
			return;
		}
		if (timer != null) {
			timer.cancel();
		}
		Message message = new Message();
		if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
			message.what = handler_key.SUCCESSFUL.ordinal();
		} else {
			message.what = handler_key.FAILED.ordinal();
		}
		Log.i("Apptest", result.toString());
		handler.sendMessage(message);
	}

	OnXmBindListener xmBindListener = new OnXmBindListener() {
		@Override
		public void addedByOther(String uuid, String user) {
			xmBinderManager.exitAllWork();
			// finish();
			Log.v("AAAAA", "addedByOther");
		}

		@Override
		public void addedSuccess(XmDevice dev) {
			xmBinderManager.exitAllWork();
			Message message = new Message();
			message.what = handler_key.SUCCESSFULCL.ordinal();
			handler.sendMessage(message);
			Log.v("AAAAA", "addedSuccess");
			// handler.sendEmptyMessage(0x123);
		}

		@Override
		public void addedBySelf(String uuid, String user) {
			xmBinderManager.exitAllWork();
			Message message = new Message();
			message.what = handler_key.ADDBYSELF.ordinal();
			handler.sendMessage(message);
			// finish();
			Log.v("AAAAA", "addedBySelf");
		}

		@Override
		public void onDevConnectMgrErr(String uuid) {
			finish();
			Log.v("AAAAA", "onDevConnectMgrErr");
		}

		@Override
		public void addErr(String uuid, XmErrInfo errinfo) {
			finish();
			Log.v("AAAAA", "addErr");
		}
	};

	private void btn_next() {
		btn_next.setVisibility(View.GONE);
		xmBinderManager.exitSendWork();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			btn_next();
			break;
		}

	}

	@Override
	protected void onDestroy() {
		// setResult(101);
		if (islc) {
			xmBinderManager.exitAllWork();
			xmBinderManager.setOnBindListener(null);
		}
		try {
			task.cancel();
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onDestroy();
	}
}
