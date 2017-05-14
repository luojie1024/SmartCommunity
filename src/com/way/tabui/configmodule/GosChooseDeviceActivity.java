package com.way.tabui.configmodule;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.commonmodule.NetUtils;
import com.way.tabui.gokit.R;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class GosChooseDeviceActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The tv Nodevice */
	TextView tvNodevice;

	/** The list View */
	ListView listView;

	/** 系统WiFi集合 */
	ArrayList<ScanResult> list;

	/** 设备热点集合 */
	ArrayList<ScanResult> softList;

	/** 适配器 */
	Myadapter myadapter;

	/** 计时器 */
	Timer timer;

	int flag = 0;

	private enum handler_key {

		/** 刷新列表 */
		UPDATALIST,

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
			// 更新列表
			case UPDATALIST:
				initData();
				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_choose_device);
		// 设置ActionBar
		setActionBar(true, true, R.string.choosedevice);

		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();
		startTimer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		timer.cancel();
	}

	private void initData() {
		list = new ArrayList<ScanResult>();
		list = (ArrayList<ScanResult>) NetUtils.getCurrentWifiScanResult(GosChooseDeviceActivity.this);
		softList = new ArrayList<ScanResult>();
		ScanResult scanResult;
		for (int i = 0; i < list.size(); i++) {
			scanResult = list.get(i);
			if (scanResult.SSID.length() > GosConstant.SoftAP_Start.length()) {
				if (scanResult.SSID.contains(GosConstant.SoftAP_Start)) {
					softList.add(scanResult);
				}
			}
		}
		myadapter = new Myadapter(softList);
		listView.setAdapter(myadapter);

	}

	private void initView() {
		tvNodevice = (TextView) findViewById(R.id.nodevice);
		listView = (ListView) findViewById(R.id.list_view);

	}

	private void initEvent() {
		tvNodevice.setOnClickListener(this);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(GosChooseDeviceActivity.this, GosConfigCountdownActivity.class);
				intent.putExtra("softSSID", softList.get(position).SSID);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nodevice:
			Intent intent = new Intent(GosChooseDeviceActivity.this, GosDeviceResetActivity.class);
			intent.putExtra("flag", "FLAG");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(handler_key.UPDATALIST.ordinal());
			}
		}, 0, 3000);
	}

	class Myadapter extends BaseAdapter {

		ArrayList<ScanResult> softList;

		public Myadapter(ArrayList<ScanResult> list) {
			this.softList = list;
		}

		@Override
		public int getCount() {
			return softList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			Holder holder;
			if (view == null) {
				view = LayoutInflater.from(GosChooseDeviceActivity.this).inflate(R.layout.item_gos_wifi_list, null);
				holder = new Holder(view);
				view.setTag(holder);
			} else {

				holder = (Holder) view.getTag();
			}
			String ssid = softList.get(position).SSID;

			String itemStart = (String) getText(R.string.itemtext_start);
			String itemEnd = (String) getText(R.string.itemtext_end);
			holder.getTextView().setText(itemStart + ssid.substring(ssid.length() - 4) + itemEnd);

			return view;
		}

	}

	class Holder {
		View view;

		public Holder(View view) {
			this.view = view;
		}

		TextView textView;

		public TextView getTextView() {
			if (textView == null) {
				textView = (TextView) view.findViewById(R.id.SSID_text);
			}
			return textView;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			quitAlert(this);
			break;
		}
		return true;
	}
	
	// 屏蔽掉返回键
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				quitAlert(this);
				return true;
			}
			return false;
		}
}
