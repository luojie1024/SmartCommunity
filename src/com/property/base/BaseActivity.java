package com.property.base;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.I_BroadcastReg;
import org.kymjs.kjframe.ui.I_KJActivity;
import org.kymjs.kjframe.ui.I_SkipActivity;
import org.kymjs.kjframe.ui.KJActivityStack;
import org.kymjs.kjframe.ui.KJFragment;
import org.kymjs.kjframe.ui.SupportFragment;
import org.kymjs.kjframe.utils.KJLoger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import com.ab.activity.AbActivity;
import com.google.gson.Gson;

public abstract class BaseActivity extends AbActivity implements
		OnClickListener, I_BroadcastReg, I_KJActivity, I_SkipActivity {

	/**
	 * 当前Activity状态
	 */
	public static enum ActivityState {
		RESUME, PAUSE, STOP, DESTROY
	}

	public Activity aty;
	/** Activity状态 */
	public ActivityState activityState = ActivityState.DESTROY;

	/***************************************************************************
	 * 
	 * print Activity callback methods
	 * 
	 ***************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		aty = this;
		KJActivityStack.create().addActivity(this);
		KJLoger.state(this.getClass().getName(), "---------onCreat ");
		super.onCreate(savedInstanceState);
		setRootView(); // 必须放在annotate之前调用
		AnnotateUtil.initBindView(this);
		initializer();
		registerBroadcast();
	}

	@Override
	protected void onStart() {
		super.onStart();
		KJLoger.state(this.getClass().getName(), "---------onStart ");
	}

	@Override
	protected void onResume() {
		super.onResume();
		activityState = ActivityState.RESUME;
		KJLoger.state(this.getClass().getName(), "---------onResume ");
	}

	@Override
	protected void onPause() {
		super.onPause();
		activityState = ActivityState.PAUSE;
		KJLoger.state(this.getClass().getName(), "---------onPause ");
	}

	@Override
	protected void onStop() {
		super.onStop();
		activityState = ActivityState.STOP;
		KJLoger.state(this.getClass().getName(), "---------onStop ");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		KJLoger.state(this.getClass().getName(), "---------onRestart ");
	}

	@Override
	protected void onDestroy() {
		activityState = ActivityState.DESTROY;
		KJLoger.state(this.getClass().getName(), "---------onDestroy ");
		super.onDestroy();
		KJActivityStack.create().finishActivity(this);
		unRegisterBroadcast();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls) {
		showActivity(aty, cls);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Intent it) {
		showActivity(aty, it);
		aty.finish();
	}

	/**
	 * skip to @param(cls)，and call @param(aty's) finish() method
	 */
	@Override
	public void skipActivity(Activity aty, Class<?> cls, Bundle extras) {
		showActivity(aty, cls, extras);
		aty.finish();
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Intent it) {
		aty.startActivity(it);
	}

	/**
	 * show to @param(cls)，but can't finish activity
	 */
	@Override
	public void showActivity(Activity aty, Class<?> cls, Bundle extras) {
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(aty, cls);
		aty.startActivity(intent);
	}

	public static final int WHICH_MSG = 0X37210;

	protected KJFragment currentKJFragment;
	protected SupportFragment currentSupportFragment;

	/**
	 * 一个私有回调类，线程中初始化数据完成后的回调
	 */
	private interface ThreadDataCallBack {
		void onSuccess();
	}

	private static ThreadDataCallBack callback;

	// 当线程中初始化的数据初始化完成后，调用回调方法
	private static Handler threadHandle = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			if (msg.what == WHICH_MSG) {
				callback.onSuccess();
			}
		};
	};

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/**
	 * 在线程中初始化数据，注意不能在这里执行UI操作
	 */
	@Override
	public void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	@Override
	public void initData() {
		gson = new Gson();
	}

	@Override
	public void initWidget() {
	}

	// 仅仅是为了代码整洁点
	private void initializer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		initData();
		initWidget();
	}

	/** listened widget's click method */
	@Override
	public void widgetClick(View v) {
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	protected <T extends View> T bindView(int id) {
		return (T) findViewById(id);
	}

	@Override
	public void registerBroadcast() {
	}

	@Override
	public void unRegisterBroadcast() {
	}

	protected void get(HttpCallBack callBack, String url) {
		KJHttp kjh = new KJHttp();
		kjh.get(url, callBack);
	}

	protected Gson gson;

	protected void post(HttpCallBack callBack, HttpParams params, String url) {
		KJHttp kjh = new KJHttp();
		kjh.post(url, params, false, callBack);
	}

}
