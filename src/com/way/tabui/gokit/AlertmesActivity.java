package com.way.tabui.gokit;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.way.adapter.AlertmesAdapter;
import com.way.adapter.DatabaseAdapter;
import com.way.adapter.DatebaseHelper;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.Alertinfo;
import com.way.util.GizMetaData;

public class AlertmesActivity extends GosBaseActivity {

	private ListView alert_info_listview;
	private TextView tv_nodata;
	private DatabaseAdapter dbAdapter;
	private AlertmesAdapter adapter;
	private DatebaseHelper dbHelper;
	String MacAddress;
	ArrayList<Alertinfo> mlist = new ArrayList<Alertinfo>();
	ActionBar actionBar;
	// MyReceiver receiver;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alertmes);
		setActionBar(true, true, "警报记录");
		Intent intent = getIntent();
		MacAddress = intent.getStringExtra("MacAddress");
		progressDialog = new ProgressDialog(this);
		dbAdapter = new DatabaseAdapter(this);
		dbHelper = new DatebaseHelper(this);
		// receiver=new MyReceiver();
		// IntentFilter filter=new IntentFilter();
		// filter.addAction("com.way.tabui.actity.GizService");
		// AlertmesActivity.this.registerReceiver(receiver, filter);
		initview();

	}

	public void setProgressDialog(String text) {
		progressDialog.setMessage(text);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		setProgressDialog("获取数据中...");
		progressDialog.show();
		initdata();
		initlist();
		initevent();
		progressDialog.cancel();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// AlertmesActivity.this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	public void setActionBar(Boolean HBE, Boolean DSHE, CharSequence Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
	}

	private void initview() {
		alert_info_listview = (ListView) findViewById(R.id.alert_info_listview);
		tv_nodata = (TextView) findViewById(R.id.tv_nodata);
	}

	private void initdata() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String whereClause = GizMetaData.AlertTable.GIZ_BINDGIZ + "=?";
		String[] whereArgs = { MacAddress };
		String[] columns = { GizMetaData.AlertTable._ID,
				GizMetaData.AlertTable.ALERT_NAME,
				GizMetaData.AlertTable.ALERT_TIME,
				GizMetaData.AlertTable.GIZ_BINDGIZ,
				GizMetaData.AlertTable.GIZ_USERID,
				GizMetaData.AlertTable.GIZ_FLAG };
		// 参数说明(是否去除重复记录,表明,要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，排序条件)GizMetaData.AlertTable._ID+" DESC"
		Cursor c = db.query(true, GizMetaData.AlertTable.TABLE_NAME, columns,
				whereClause, whereArgs, null, null, null, null);
		if (c.getCount() == 0) {
			// initSQLdata();
			tv_nodata.setVisibility(View.VISIBLE);
		} else {
			tv_nodata.setVisibility(View.GONE);
		}
		c.close();
		db.close();

		mlist = dbAdapter.findbybindgizalert(MacAddress);
	}

	int alertmesid;

	private void initlist() {
		adapter = new AlertmesAdapter(AlertmesActivity.this, mlist);
		alert_info_listview.setAdapter(adapter);
	}

	private void initevent() {
		alert_info_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				alertmesid = mlist.get(position).getId();
				new AlertDialog.Builder(AlertmesActivity.this)
						.setTitle(R.string.prompt)
						.setMessage("确定要删除此记录吗")
						.setPositiveButton(R.string.besure,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dbAdapter.deletealert(alertmesid);
										Toast.makeText(getApplicationContext(),
												"删除完毕", Toast.LENGTH_SHORT)
												.show();
										setProgressDialog("刷新数据中...");
										progressDialog.show();
										initdata();
										initlist();
										progressDialog.cancel();
									}
								}).setNegativeButton("取消", null).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		getMenuInflater().inflate(R.menu.alert_mes, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		// TODO Auto-generated method stub

		switch (menu.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.del_all:
			new AlertDialog.Builder(AlertmesActivity.this)
					.setTitle(R.string.prompt)
					.setMessage("确定要删除全部记录吗")
					.setPositiveButton(R.string.besure,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setProgressDialog("删除中...");
									progressDialog.show();
									for (int i = 0; i < mlist.size(); i++) {
										alertmesid = mlist.get(i).getId();
										dbAdapter.deletealert(alertmesid);
									}
									progressDialog.cancel();
									Toast.makeText(getApplicationContext(),
											"删除完毕", Toast.LENGTH_SHORT).show();
									initdata();
									initlist();
								}
							}).setNegativeButton("取消", null).show();
			break;
		}
		return super.onOptionsItemSelected(menu);
	}

	boolean isdisconnect;
	// public class MyReceiver extends BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// isdisconnect =intent.getBooleanExtra("isdisconnect", false);
	// if(isdisconnect){
	// finish();
	// }
	// }
	// }
}
