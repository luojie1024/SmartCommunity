package com.way.tabui.settingsmodule;

import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.actity.MainActivity.MyReceiver;
import com.way.tabui.gokit.R;

public class GosAboutActivity extends Activity {

	
	TextView tv_MCUversion;
	Button bt_gethardware;

	/** the tv appCode */
	TextView tv_AppVersion;

	/** The ActionBar */
	ActionBar actionBar;
	String MCUversion="获取版本中...";
	String HardWardInfo="获取版本中...";
	public GizWifiDevice device;
	boolean isoffline;
	MyReceiver receiver;
	String title;
	private  TextView textView1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_about);
		
		
		receiver=new MyReceiver();
		  IntentFilter filter=new IntentFilter();
		  filter.addAction("com.way.tabui.actity.GizServiceHARDWARE");
		 GosAboutActivity.this.registerReceiver(receiver,filter);
		 
		 Intent intent =getIntent();
		 isoffline=intent.getBooleanExtra("isoffline", false);
		 title=intent.getStringExtra("title");
		 
		if(title.equals("检查更新")){
			setActionBar(true, true, R.string.check);
			if(!isoffline){
			device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
			device.getHardwareInfo();
			}else{
				MCUversion="目前为离线登入状态";
			}
		}
		
		else
			setActionBar(true, true, R.string.about);
	
		initView();
		initdata();
		initEvent();
	}

	/**
	 * Inits the view.
	 */
	private void initView() {
		textView1=(TextView) findViewById(R.id.textView1);
		tv_MCUversion = (TextView) findViewById(R.id.versionCode);
		tv_AppVersion = (TextView) findViewById(R.id.appCode);
		bt_gethardware=(Button) findViewById(R.id.bt_gethardware);
	}

	@Override
	public void onResume() {
		super.onResume();
//		tv_SDKVersion.setText("V" + GizWifiSDK.sharedInstance().getVersion().toString());
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		GosAboutActivity.this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	private void initdata(){
		if(!title.equals("检查更新")||isoffline){
			textView1.setVisibility(View.GONE);
			tv_MCUversion.setVisibility(View.GONE);
			bt_gethardware.setVisibility(View.GONE);
		}else{
			textView1.setVisibility(View.VISIBLE);
			tv_MCUversion.setVisibility(View.VISIBLE);
			bt_gethardware.setVisibility(View.VISIBLE);
		}
		
		tv_MCUversion.setText(MCUversion);
		tv_AppVersion.setText(getAppVersionName(this));
	}
	
	private void initEvent(){
		bt_gethardware.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showHardwareInfo(HardWardInfo);
			}
		});
	}
		
	
	
	
	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("Apptest", "Exception", e);  
	    }  
	    return versionName;  
	}  
	
	 public class MyReceiver extends BroadcastReceiver {
	     @Override
	     public void onReceive(Context context, Intent intent) {
	    	MCUversion = intent.getStringExtra("MCUversion");
	    	HardWardInfo= intent.getStringExtra("Hardwaredata");
	    	initdata();
	     }
	 }
	     
	 /**
		 * 展示设备硬件信息
		 * 
		 * @param hardwareInfo
		 */
		private void showHardwareInfo(String hardwareInfo) {
			String hardwareInfoTitle = (String) getText(R.string.hardwareInfo);
			new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo)
					.setPositiveButton(R.string.besure, null).show();
		}
		
	/**
	 * 设置ActionBar（工具方法*开发用*）
	 * 
	 * @param HBE
	 * @param DSHE
	 * @param Title
	 */
	protected void setActionBar(Boolean HBE, Boolean DSHE, int Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
