package com.way.tabui.gokit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.AirMesAdapter;
import com.way.adapter.DatabaseAdapter;
import com.way.adapter.DatebaseHelper;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.view.SlideListView2;
import com.way.util.AirMesinfo;
import com.way.util.GizMetaData;
import com.way.util.StringUtils;

import java.util.ArrayList;

public class AirConditionListActivity extends GosBaseActivity {

	private LinearLayout bt_add_air;
	private View lldevice;
	private ScrollView svListGroup;
	private SlideListView2 listview_air_con_mes;
	private DatabaseAdapter dbAdapter;
	private AirMesAdapter adapter;
	private DatebaseHelper dbHelper;
	ArrayList<AirMesinfo> mlist = new ArrayList<AirMesinfo>();
	/** 传过来正在连接中的的设备 */
	private GizWifiDevice device = null;
	ProgressDialog progressDialog;
	protected static final int UPDATA = 99;
	protected static final int DELETE = 100;
	AirMesinfo airMesinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_con_mes);
		progressDialog = new ProgressDialog(this);
		setActionBar(true, true, "设备列表");
		initDevice();
		dbAdapter = new DatabaseAdapter(this);
		dbHelper = new DatebaseHelper(this);
		airMesinfo = new AirMesinfo();
		initView();
	}

	int index;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA:
				index = msg.arg1;
				airMesinfo = (AirMesinfo) msg.obj;
				setDeviceInfo();
				break;
			case DELETE:
				index = msg.arg1;
				deleteAlert(AirConditionListActivity.this);
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		setProgressDialog("获取数据中...");
		progressDialog.show();
		initData();
		initList();
		initEvent();
		progressDialog.cancel();
		super.onResume();
	}

	public void setProgressDialog(String text) {
		progressDialog.setMessage(text);
		progressDialog.setCanceledOnTouchOutside(false);
	}

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
						dbAdapter.deleteAirmes(index);
						setProgressDialog("刷新中...");
						progressDialog.show();
						initData();
						initList();
						initEvent();
						progressDialog.cancel();
					}
				});
		builder.show();
	}

	private void initData() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String whereClause = GizMetaData.Aircondition.GIZ_BINDGIZ + "=?";
		String[] whereArgs = { device.getMacAddress() };
		String[] columns = { GizMetaData.Aircondition._ID,
				GizMetaData.Aircondition.AIR_NAME,
				GizMetaData.Aircondition.AIR_BRAND,
				GizMetaData.Aircondition.AIR_TEM,
				GizMetaData.Aircondition.AIR_MODE,
				GizMetaData.Aircondition.AIR_WS,
				GizMetaData.Aircondition.AIR_WD,
				GizMetaData.Aircondition.GIZ_BINDGIZ,
				GizMetaData.Aircondition.GIZ_USERID,
				GizMetaData.Aircondition.GIZ_FLAG };
		// 参数说明(是否去除重复记录,表明,要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，排序条件)
		Cursor c = db.query(true, GizMetaData.Aircondition.TABLE_NAME, columns,
				whereClause, whereArgs, null, null,
				GizMetaData.Aircondition._ID + " DESC", null);
		if (c.getCount() == 0) {
			bt_add_air.setVisibility(View.VISIBLE);
			svListGroup.setVisibility(View.GONE);
		} else {
			bt_add_air.setVisibility(View.GONE);
			svListGroup.setVisibility(View.VISIBLE);
		}
		c.close();
		db.close();
		mlist = dbAdapter.findbybindgizairmes(device.getMacAddress());
		System.out.println(123);
	}

	private void initList() {
		adapter = new AirMesAdapter(mlist, AirConditionListActivity.this);
		adapter.setHandler(handler);
		listview_air_con_mes.setAdapter(adapter);
	}

	private void initEvent() {
		listview_air_con_mes.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(AirConditionListActivity.this,
						SmartAirConditionActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", device);
				intent.putExtras(bundle);
				intent.putExtra("id", mlist.get(position).get_id());
				intent.putExtra("name", mlist.get(position).getName());
				intent.putExtra("brand", mlist.get(position).getBrand());
				intent.putExtra("temperature", mlist.get(position)
						.getTemperature());
				intent.putExtra("mod", mlist.get(position).getMode());
				intent.putExtra("speed", mlist.get(position).getSpeed());
				intent.putExtra("direction", mlist.get(position).getDirection());
				intent.putExtra("opcl", mlist.get(position).getFlag());
				intent.putExtra("device_id", mlist.get(position).getDevice_id());
				startActivity(intent);
			}
		});
		listview_air_con_mes.initSlideMode(SlideListView2.MOD_RIGHT);

		bt_add_air.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AirConditionListActivity.this,
						AirConBrandActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", device);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	int position;
	Dialog dialog;

	private void setDeviceInfo() {
		dialog = new AlertDialog.Builder(this).setView(new EditText(this))
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_air_con_set_mes);

		final EditText etAlias;
		final EditText etBrand;
		final EditText etAddress;

		etAlias = (EditText) window.findViewById(R.id.et_Alias);
		etBrand = (EditText) window.findViewById(R.id.et_Brand);
		etAddress = (EditText) window.findViewById(R.id.et_address);

		LinearLayout llNo, llSure;

		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);
		if (airMesinfo.getName() != null) {
			etAlias.setText(airMesinfo.getName());
		}
		// if (mlist.get(position).getBrand()!=null) {
		etBrand.setText("" + airMesinfo.getBrand());
		// }
		etAddress.setText(""+airMesinfo.getDevice_id());
		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = etAlias.getText().toString();
				int brand;
				String device_id;
				brand = Integer.parseInt(etBrand.getText().toString());
				device_id=etAddress.getText().toString().toUpperCase();
				if (StringUtils.checkNum(device_id)) {
					try {
						AirMesinfo airMesinfo2 = new AirMesinfo(
						airMesinfo.get_id(), name, brand, airMesinfo
						.getTemperature(), airMesinfo.getMode(),
						airMesinfo.getSpeed(), airMesinfo.getDirection(),
						airMesinfo.getBindgiz(), airMesinfo.getUserid(),
						airMesinfo.getFlag(), device_id);
						dbAdapter.updateAirmes(airMesinfo2);
						Toast.makeText(getApplicationContext(), "修改成功",
						Toast.LENGTH_SHORT).show();
						setProgressDialog("刷新数据中...");
						progressDialog.show();
						initData();
						initList();
						initEvent();
						progressDialog.cancel();
						dialog.cancel();

					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), "修改失败",
						Toast.LENGTH_SHORT).show();
						dialog.cancel();
					}
				} else {
					Toast.makeText(getApplicationContext(), "修改失败！地址必须为不带任何标点符号的8位16进制数",
					Toast.LENGTH_SHORT).show();
				}

			}
		});
	}

	private void initView() {
		bt_add_air = (LinearLayout) findViewById(R.id.bt_add_device);
		listview_air_con_mes = (SlideListView2) findViewById(R.id.slideListView1);
		lldevice = findViewById(R.id.lldevice);
		svListGroup = (ScrollView) findViewById(R.id.svListGroup);
	}

	public void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_devices, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:
			finish();
			break;
		case R.id.add_device:
			Intent intent = new Intent(AirConditionListActivity.this,
					AirConBrandActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice", device);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1000);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
