package com.property.base;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.AnnotateUtil;
import org.kymjs.kjframe.ui.I_BroadcastReg;
import org.kymjs.kjframe.ui.I_SkipActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.gson.Gson;

public abstract class BaseFragment extends Fragment implements OnClickListener, I_BroadcastReg, I_SkipActivity {
	public static final int WHICH_MSG = 0X37211;

	protected View fragmentRootView;
	protected Gson gson;

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

	protected abstract View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle);

	/**
	 * initialization widget, you should look like parentView.findviewbyid(id);
	 * call method
	 * 
	 * @param parentView
	 */
	protected void initWidget(View parentView) {
	}

	/** initialization data */
	public void initData() {
		gson = new Gson();
	}

	/**
	 * initialization data. And this method run in background thread, so you
	 * shouldn't change ui<br>
	 * on initializated, will call threadDataInited();
	 */
	public void initDataFromThread() {
		callback = new ThreadDataCallBack() {
			@Override
			public void onSuccess() {
				threadDataInited();
			}
		};
	}

	/**
	 * 如果调用了initDataFromThread()，则当数据初始化完成后将回调该方法。
	 */
	protected void threadDataInited() {
	}

	/** widget click method */
	public void widgetClick(View v) {
	}

	@Override
	public void onClick(View v) {
		widgetClick(v);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		fragmentRootView = inflaterView(inflater, container, savedInstanceState);
		AnnotateUtil.initBindView(this, fragmentRootView);
		initData();
		initWidget(fragmentRootView);
		new Thread(new Runnable() {
			@Override
			public void run() {
				initDataFromThread();
				threadHandle.sendEmptyMessage(WHICH_MSG);
			}
		}).start();
		return fragmentRootView;
	}

	protected <T extends View> T bindView(int id) {
		return (T) fragmentRootView.findViewById(id);
	}

	protected <T extends View> T bindView(int id, boolean click) {
		T view = (T) fragmentRootView.findViewById(id);
		if (click) {
			view.setOnClickListener(this);
		}
		return view;
	}

	protected void get(HttpCallBack callBack, String url) {
		KJHttp kjh = new KJHttp();
		kjh.removeAllDiskCache();
		kjh.get(url, callBack);
	}

	protected void post(HttpCallBack callBack, HttpParams params, String url) {
		KJHttp kjh = new KJHttp();
		kjh.post(url, params, false, callBack);
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
