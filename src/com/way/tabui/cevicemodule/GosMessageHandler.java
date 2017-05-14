package com.way.tabui.cevicemodule;

import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.widget.RemoteViews;

import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.commonmodule.NetUtils;
import com.way.tabui.configmodule.GosCheckDeviceWorkWiFiActivity;
import com.way.tabui.gokit.R;

public class GosMessageHandler {

	NotificationManager nm;

	protected static final int SHOWDIALOG = 999;

	private Context mcContext;

	private ArrayList<String> newDeviceList = new ArrayList<String>();

	private Handler mainHandler;
	// 做一个单例
	private static GosMessageHandler mInstance = new GosMessageHandler();

	public static GosMessageHandler getSingleInstance() {
		return mInstance;
	}

	public void SetHandler(Handler handler) {
		this.mainHandler = handler;
	}

	public void StartLooperWifi(Context context) {
		this.mcContext = context;
		HandlerThread looperwifi = new HandlerThread("looperwifi");
		looperwifi.start();
		looper = new MyLooperHandler(looperwifi.getLooper());
		looper.post(mRunnable);
	}

	class MyLooperHandler extends Handler {
		public MyLooperHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	}

	/**
	 * 子线程实现
	 */
	private Runnable mRunnable = new Runnable() {

		public void run() {
			if (mcContext == null) {
				return;
			}
			newDeviceList.clear();
			List<ScanResult> currentWifiScanResult = NetUtils.getCurrentWifiScanResult(mcContext);
			int flog = 0;
			for (ScanResult scanResult : currentWifiScanResult) {
				String ssid = scanResult.SSID;
				// 获取系统的NotificationManager服务
				nm = (NotificationManager) mcContext.getSystemService(Context.NOTIFICATION_SERVICE);
				if (ssid.contains(GosConstant.SoftAP_Start) && ssid.length() > GosConstant.SoftAP_Start.length()
						&& !newDeviceList.toString().contains(ssid)) {
					newDeviceList.add(ssid);
					flog++;
					send(ssid, flog);
				}
			}
			if (mainHandler != null && newDeviceList.size() > 0) {
				mainHandler.sendEmptyMessage(SHOWDIALOG);
			}

			looper.postDelayed(mRunnable, 2000);
		}
	};
	private MyLooperHandler looper;

	// 为发送通知的按钮的点击事件定义事件处理方法
	public void send(String ssid, int flog) {
		String ticker, title, text;
		ticker = (String) mcContext.getText(R.string.not_ticker);
		title = (String) mcContext.getText(R.string.not_title);
		text = (String) mcContext.getText(R.string.not_text);
		// 创建一个启动其他Activity的Intent
		Intent intent = new Intent(mcContext, GosCheckDeviceWorkWiFiActivity.class);
		intent.putExtra("softssid", ssid);
		PendingIntent pi = PendingIntent.getActivity(mcContext, 0, intent, 0);
		Notification notify = new Notification();
		// 设置通知图标
		notify.icon = R.drawable.ic_launcher;
		// 设置显示在状态栏的通知提示信息
		notify.tickerText = ticker;
		notify.when = System.currentTimeMillis();
		// 设置打开该通知,该通知自动消失
		notify.flags = Notification.FLAG_AUTO_CANCEL;

		// 构造通知内容布局
		RemoteViews rv = new RemoteViews(mcContext.getPackageName(), R.layout.view_gos_notification);
		// 设置通知内容的标题
		rv.setTextViewText(R.id.tvContentTitle, title);
		// 设置通知内容
		rv.setTextViewText(R.id.tvContentText, ssid + text);
		// 加载通知页面
		notify.contentView = rv;
		// 设置通知将要启动的Intent
		notify.contentIntent = pi;
		// TODO 发送通知
		//nm.notify(flog, notify);
	}

	public ArrayList<String> getNewDeviceList() {
		return newDeviceList;
	}

}
