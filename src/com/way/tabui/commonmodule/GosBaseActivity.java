package com.way.tabui.commonmodule;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.way.tabui.actity.MainActivity;
import com.way.tabui.cevicemodule.GosDeviceListActivity;
import com.way.tabui.gokit.R;
import com.way.util.ExitAppReceiver;

public class GosBaseActivity extends FragmentActivity {

	/** 存储器 */
	public SharedPreferences spf;

	/** 等待框 */
	public ProgressDialog progressDialog;

	/** 标题栏 */
	public ActionBar actionBar;

	 private ExitAppReceiver exitReceiver = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		
		spf = getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
		// 初始化
		setProgressDialog();
		
		 exitReceiver = new ExitAppReceiver();
		  IntentFilter filter0=new IntentFilter();
		  filter0.addAction("com.way.util.exit_app");
		  registerReceiver(exitReceiver,filter0);
		
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		 unregisterReceiver(exitReceiver);
		super.onDestroy();
	}

	/**
	 * 添加ProductKey
	 * 
	 * @param productKey
	 * @return
	 */
	public void addProductKey(String productKey) {
		List<String> productKeyList = new ArrayList<String>();
		productKeyList.add(productKey);

		GosConstant.ProductKeyList = productKeyList;
	}

//	/**
//	 * 添加ProductKeys
//	 * 
//	 * @param productkeys
//	 * @return
//	 */
//	public void addProductKey(String[] productkeys) {
//		List<String> productkeyList = new ArrayList<String>();
//		for (String productkey : productkeys) {
//			productkeyList.add(productkey);
//		}
//
//		GosConstant.ProductKeyList = productkeyList;
//	}
//
	/**
	 * 设置ActionBar（工具方法）
	 * 
	 * @param HBE
	 * @param SHAU
	 * @param Icon
	 * @param Title
	 */
	public void setActionBar(Boolean HBE, Boolean SHAU, int Icon, int Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(Icon);
		actionBar.setTitle(Title);
		actionBar.setDisplayHomeAsUpEnabled(SHAU);
	}

	/**
	 * 设置ActionBar（工具方法*开发用*）
	 * 
	 * @param HBE
	 * @param DSHE
	 * @param Title
	 */
	public void setActionBar(Boolean HBE, Boolean DSHE, int Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
	}

	/**
	 * 设置ActionBar（工具方法*开发用*）
	 * 
	 * @param HBE
	 * @param DSHE
	 * @param Title
	 */
	public void setActionBar(Boolean HBE, Boolean DSHE, CharSequence Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
	}

	/**
	 * 设置ProgressDialog
	 */
	public void setProgressDialog() {
		progressDialog = new ProgressDialog(this);
		String loadingText = getString(R.string.loadingtext);
		progressDialog.setMessage(loadingText);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 设置ProgressDialog
	 * 
	 * @param Message
	 * @param Cancelable
	 * @param CanceledOnTouchOutside
	 */
	public void setProgressDialog(String Message, boolean Cancelable, boolean CanceledOnTouchOutside) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(Message);
		progressDialog.setCancelable(Cancelable);
		progressDialog.setCanceledOnTouchOutside(CanceledOnTouchOutside);
	}

	/**
	 * 检查网络连通性（工具方法）
	 * 
	 * @param context
	 * @return
	 */
	public boolean checkNetwork(Context context) {
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conn.getActiveNetworkInfo();
		if (net != null && net.isConnected()) {
			return true;
		}
		return false;
	}

	//检查服务是否工作
		public boolean isWorked(String className) {
				ActivityManager myManager = (ActivityManager) this
						.getApplicationContext().getSystemService(
								Context.ACTIVITY_SERVICE);
				ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
						.getRunningServices(50);
				for (int i = 0; i < runningService.size(); i++) {
					Log.i("==", runningService.get(i).service.getClassName().toString());
					if (runningService.get(i).service.getClassName().toString()
							.equals(className)) {
						return true;
					}
				}
				return false;
			}

	/**
	 * 验证手机格式.（工具方法）
	 */
//	public boolean isMobileNO(String mobiles) {
//		/*
//		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
//		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
//		 */
//		String telRegex = "[1][3578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//		if (TextUtils.isEmpty(mobiles))
//			return false;
//		else
//			return !mobiles.matches(telRegex);
//	}

}
