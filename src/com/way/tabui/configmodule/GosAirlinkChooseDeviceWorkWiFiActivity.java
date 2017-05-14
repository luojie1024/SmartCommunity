package com.way.tabui.configmodule;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.commonmodule.NetUtils;
import com.way.tabui.configmodule.GosModeListActivity.ModeListAdapter;
import com.way.tabui.gokit.R;

@SuppressLint("InflateParams")
public class GosAirlinkChooseDeviceWorkWiFiActivity extends
		GosConfigModuleBaseActivity implements OnClickListener {

	private AlertDialog create;
	private ArrayList<ScanResult> wifiList;

	/** wifi信息 */
	public WifiInfo wifiInfo;

	/** The et SSID */
	private EditText etSSID;

	/** The et Psw */
	private EditText etPsw;

	/** The btn Next */
	private Button btnNext, btnAll, btnlC;

	/** The ll ChooseMode */
	private LinearLayout llChooseMode;

	/** The cb Laws */
	private CheckBox cbLaws;

	/** The tv Mode */
	private TextView tvMode;

	/** The img WiFiList */
	private ImageView imgWiFiList;

	/** 配置用参数 */
	private String workSSID, workSSIDPsw;

	/** The data */
	List<String> modeList;

	/** The Adapter */
	ModeListAdapter modeListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_airlink_choose_device_workwifi);
		// 设置ActionBar
		setActionBar(true, true, R.string.add_device);
		initData();
		initView();
		ininEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();

		tvMode.setText(modeList.get(spf.getInt("MODE", 0)));
		// 预设workSSID && workSSIDPsw
		if (!TextUtils.isEmpty(workSSID)) {
			etSSID.setText(workSSID);
			if (checkworkSSIDUsed(workSSID)) {
				if (!TextUtils.isEmpty(spf.getString("workSSIDPsw", ""))) {
					etPsw.setText(spf.getString("workSSIDPsw", ""));
				}
			}
		} else {
			etSSID.setText(NetUtils.getCurentWifiSSID(this));
		}
	}

	private void initView() {
		tvMode = (TextView) findViewById(R.id.tvMode);
		etSSID = (EditText) findViewById(R.id.etSSID);
		etPsw = (EditText) findViewById(R.id.etPsw);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
		btnNext = (Button) findViewById(R.id.btnNext);
		btnlC = (Button) findViewById(R.id.btnlC);
		btnAll = (Button) findViewById(R.id.btnAll);
		llChooseMode = (LinearLayout) findViewById(R.id.llChooseMode);
		imgWiFiList = (ImageView) findViewById(R.id.imgWiFiList);
	}

	private void ininEvent() {
		btnNext.setOnClickListener(this);
		btnlC.setOnClickListener(this);
		btnAll.setOnClickListener(this);
		imgWiFiList.setOnClickListener(this);
		llChooseMode.setOnClickListener(this);

		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
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

	private void initData() {
		workSSID = spf.getString("workSSID", "");

		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
	}

	boolean isGiz, islc;

	private void tointent() {
		workSSID = etSSID.getText().toString();
		workSSIDPsw = etPsw.getText().toString();

		if (TextUtils.isEmpty(workSSID)) {
			Toast.makeText(GosAirlinkChooseDeviceWorkWiFiActivity.this,
					R.string.choose_wifi_list_title, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(workSSIDPsw)) {
			new AlertDialog.Builder(this)
					.setTitle(R.string.prompt)
					.setMessage(R.string.workwifi_isnull)
					.setNegativeButton(R.string.cancel, null)
					.setPositiveButton(R.string.besure,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									toAirlinkReady();
								}
							}).show();
		} else {
			toAirlinkReady();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnNext:
			isGiz = true;
			islc = false;
			tointent();
			break;
		case R.id.btnlC:
			isGiz = false;
			islc = true;
			tointent();
			break;
		case R.id.btnAll:
			isGiz = true;
			islc = true;
			tointent();
			break;

		case R.id.imgWiFiList:
			AlertDialog.Builder dia = new AlertDialog.Builder(
					GosAirlinkChooseDeviceWorkWiFiActivity.this);
			View view = View.inflate(
					GosAirlinkChooseDeviceWorkWiFiActivity.this,
					R.layout.alert_gos_wifi_list, null);
			ListView listview = (ListView) view.findViewById(R.id.wifi_list);
			List<ScanResult> rsList = NetUtils.getCurrentWifiScanResult(this);
			List<String> localList = new ArrayList<String>();
			localList.clear();
			wifiList = new ArrayList<ScanResult>();
			wifiList.clear();
			for (ScanResult sss : rsList) {

				if (sss.SSID.contains(GosConstant.SoftAP_Start)) {
				} else {
					if (localList.toString().contains(sss.SSID)) {
					} else {
						localList.add(sss.SSID);
						wifiList.add(sss);
					}
				}
			}
			WifiListAdapter adapter = new WifiListAdapter(wifiList);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ScanResult sResult = wifiList.get(arg2);
					String sSID = sResult.SSID;
					etSSID.setText(sSID);
					etPsw.setText("");
					create.dismiss();
				}
			});
			dia.setView(view);
			create = dia.create();
			create.show();

			break;

		case R.id.llChooseMode:
			Intent intent = new Intent(this, GosModeListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void toAirlinkReady() {
		// TODO
		spf.edit().putString("workSSID", workSSID).commit();
		spf.edit().putString("workSSIDPsw", workSSIDPsw).commit();

		Intent intent = new Intent(this, GosAirlinkReadyActivity.class);
		intent.putExtra("isGiz", isGiz);
		intent.putExtra("islc", islc);
		startActivity(intent);
		finish();
	}

	// 检查当前使用的WiFi是否曾经用过
	protected boolean checkworkSSIDUsed(String workSSID) {
		if (spf.contains("workSSID")) {
			if (spf.getString("workSSID", "").equals(workSSID)) {
				return true;
			}
		}
		return false;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			quitAlert(this);
			break;
		}
		return true;
	}

	class WifiListAdapter extends BaseAdapter {

		ArrayList<ScanResult> xpgList;

		public WifiListAdapter(ArrayList<ScanResult> list) {
			this.xpgList = list;
		}

		@Override
		public int getCount() {
			return xpgList.size();
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
				view = LayoutInflater.from(
						GosAirlinkChooseDeviceWorkWiFiActivity.this).inflate(
						R.layout.item_gos_wifi_list, null);
				holder = new Holder(view);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}

			String ssid = xpgList.get(position).SSID;
			holder.getTextView().setText(ssid);

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
}
