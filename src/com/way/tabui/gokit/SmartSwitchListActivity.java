package com.way.tabui.gokit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.DatabaseAdapter;
import com.way.adapter.DatebaseHelper;
import com.way.adapter.SmartSwitchListAdapter;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.controlmodule.GosControlModuleBaseActivity;
import com.way.tabui.view.SlideListView2;
import com.way.util.GizMetaData;
import com.way.util.SwitchInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 灯光/插座/开关操作界面
 */
public class SmartSwitchListActivity extends GosControlModuleBaseActivity {
	/** The GizWifiDevice device */
	private GizWifiDevice device;
	/** The device statu. */
	private HashMap<String, Object> deviceStatu;
	/** The isUpDateUi */
	protected static boolean isUpDateUi = true;

	protected static final int OPEN = 1;
	protected static final int CLOSE = 0;
	protected static final int UPDATA = 99;
	protected static final int DELETE = 100;


	private LinearLayout bt_addCurtain;
	private View lldevice;
	private ScrollView svListGroup;
	private SlideListView2 listview_air_con_mes;
	private DatabaseAdapter dbAdapter;
	private SmartSwitchListAdapter adapter;
	private DatebaseHelper dbHelper;
	ArrayList<SwitchInfo> giz = new ArrayList<SwitchInfo>();
	private String MacAddress, name, address;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_switch_list);
		initDevice();
		MacAddress = device.getMacAddress();
		setActionBar(true, true, "灯光/插座/开关");
		dbAdapter = new DatabaseAdapter(this);
		dbHelper = new DatebaseHelper(this);
		switchInfo = new SwitchInfo();
		setProgressDialog();
		progressDialog.show();
		initView();
		initList();
	}

	@Override
	protected void onResume() {
		if((adapter.setDate(MacAddress))==null){
			bt_addCurtain.setVisibility(View.VISIBLE);
			svListGroup.setVisibility(View.GONE);
		}else {
			bt_addCurtain.setVisibility(View.GONE);
			svListGroup.setVisibility(View.VISIBLE);
		}
		initevent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		deviceStatu = new HashMap<String, Object>();
		Log.i("device", "initDevice:----> "+device.getMacAddress());
	}
	private void initList(){
		adapter = new SmartSwitchListAdapter(handler,SmartSwitchListActivity.this);
		adapter.setHandler(handler);
		listview_air_con_mes.setAdapter(adapter);

	}

	private void initView() {
		bt_addCurtain = (LinearLayout) findViewById(R.id.bt_add_device);
		listview_air_con_mes = (SlideListView2) findViewById(R.id.slideListView1);
		lldevice = findViewById(R.id.lldevice);
		svListGroup = (ScrollView) findViewById(R.id.svListGroup);
		progressDialog.cancel();
		listview_air_con_mes.initSlideMode(SlideListView2.MOD_RIGHT);
	}

	private void initevent() {
		// TODO: 点击列表开启灯泡控制
		listview_air_con_mes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				name = adapter.getmList().get(position).getName();
				//address地址代码
				address = adapter.getmList().get(position).getAddress();
				type = adapter.getmList().get(position).getType();
				Intent intent = new Intent(SmartSwitchListActivity.this, SmartSwitchActivity.class);
				intent.putExtra("name",name);
				intent.putExtra("address",address);
				intent.putExtra("type",type);
				Bundle bundle = new Bundle();
				//传设备
				bundle.putParcelable("GizWifiDevice",device);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1000);
			}
		});

		bt_addCurtain.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkNetwork(SmartSwitchListActivity.this)) {
					Toast.makeText(SmartSwitchListActivity.this, R.string.network_error,
					Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(SmartSwitchListActivity.this,
					SmartSwitchTypeListActivity.class);
					intent.putExtra("bindgiz", MacAddress);
					intent.putExtra("tablename", GizMetaData.SwitchTable.TABLE_NAME);
					startActivity(intent);
				}
			}
		});

	}

	int position;
	Dialog dialog;
	SwitchInfo switchInfo;
	private void setDeviceInfo() {
		dialog = new AlertDialog.Builder(this).setView(new EditText(this))
		.create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_curtain_set_mes);
		final EditText etAlias;
		final EditText etBrand;
		etAlias = (EditText) window.findViewById(R.id.etAlias);
		etBrand = (EditText) window.findViewById(R.id.etBrand);
		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);
		if (switchInfo.getName() != null) {
			etAlias.setText(switchInfo.getName());
		}
		etBrand.setText("" + switchInfo.getAddress());
		// }

		llNo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		//滑动修改
		llSure.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				;
				try {
					String name = etAlias.getText().toString();
					String  address = etBrand.getText().toString();
					switchInfo.setAddress(address);
					switchInfo.setName(name);


					if((adapter.updateList(switchInfo).setDate(MacAddress))==null){
						bt_addCurtain.setVisibility(View.VISIBLE);
						svListGroup.setVisibility(View.GONE);
					}else {
						bt_addCurtain.setVisibility(View.GONE);
						svListGroup.setVisibility(View.VISIBLE);
					}
					Toast.makeText(getApplicationContext(), "修改成功",
					Toast.LENGTH_SHORT).show();
					dialog.cancel();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "修改失败",
					Toast.LENGTH_SHORT).show();
					dialog.cancel();
				}

			}
		});
	}

	//滑动删除
	protected void deleteAlert(Context context) {
		String title, message, nbtext, pbtext;
		title = (String) getText(R.string.prompt);
		message = "确定要删除么？";
		nbtext = "取消";
		pbtext = "确定";
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton(nbtext, null);
		builder.setPositiveButton(pbtext,
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if((adapter.deleteDate(index).setDate(MacAddress))==null){
					bt_addCurtain.setVisibility(View.VISIBLE);
					svListGroup.setVisibility(View.GONE);
				}else {
					bt_addCurtain.setVisibility(View.GONE);
					svListGroup.setVisibility(View.VISIBLE);
				}
			}
		});
		builder.show();
	}

	int index;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			int position =msg.arg1;
			ArrayList<SwitchInfo> mlist =adapter.getmList();
			switch (msg.what){
				case UPDATA:
					index = msg.arg1;
					switchInfo= (SwitchInfo) msg.obj;
					setDeviceInfo();
					break;
				case DELETE:
					index = msg.arg1;
					deleteAlert(SmartSwitchListActivity.this);
					break;
			}
		}

	};





	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.i("==", hashMap.toString());
		// Log.i("Apptest", hashMap.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.add_devices, menu);
		return true;
	}


	//右上角增加设备
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent;
		switch (item.getItemId()) {
			case android.R.id.home:

				finish();
				break;

			case R.id.add_device:
				if (!checkNetwork(SmartSwitchListActivity.this)) {
					Toast.makeText(SmartSwitchListActivity.this, R.string.network_error,
					Toast.LENGTH_SHORT).show();
				} else {
					intent = new Intent(SmartSwitchListActivity.this,
					SmartSwitchTypeListActivity.class);
					intent.putExtra("bindgiz", MacAddress);
					intent.putExtra("tablename", GizMetaData.SwitchTable.TABLE_NAME);
					startActivity(intent);
				}
				break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(SmartSwitchListActivity.this, MainActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
			intent.putExtras(bundle);
			intent.putExtra("isoffline", false);

			// receiver.clearAbortBroadcast();
			setResult(1001, intent);

			finish();
		}
		return false;
	}
}
