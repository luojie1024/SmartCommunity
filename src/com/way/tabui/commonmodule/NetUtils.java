/**
 * Project Name:Gokit
 * File Name:NetUtils.java
 * Package Name:com.xpg.gokit.utils
 * Date:2014-11-18 10:06:37
 * Copyright (c) 2014~2015 Xtreme Programming Group, Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.way.tabui.commonmodule;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * 网络工具类.
 *
 * @author Sunny Ding
 * 
 *         *
 */
public class NetUtils {

	/**
	 * 判断当前手机是否连上Wifi.
	 *
	 * @param context
	 *            上下文
	 * @return boolean 是否连上网络
	 * 
	 *         *
	 */
	static public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				if (mWiFiNetworkInfo.isAvailable()) {
					return mWiFiNetworkInfo.isConnected();
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前手机的网络是否可用.
	 *
	 * @param context
	 *            上下文
	 * @return boolean 是否连上网络
	 * 
	 *         *
	 */
	static public boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				if (mMobileNetworkInfo.isAvailable()) {
					return mMobileNetworkInfo.isConnected();
				} else {
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 判断当前网络是手机网络还是WIFI.
	 *
	 * @param context
	 *            上下文
	 * @return ConnectedType 数据类型
	 * 
	 *         *
	 */
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// 获取代表联网状态的NetWorkInfo对象
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			// 判断NetWorkInfo对象是否为空；判断当前的网络连接是否可用
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 获取当前WIFI的SSID.
	 *
	 * @param context
	 *            上下文
	 * @return ssid
	 * 
	 *         *
	 */
	public static String getCurentWifiSSID(Context context) {
		String ssid = "";
		if (context != null) {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			ssid = wifiInfo.getSSID();
			if (ssid.substring(0, 1).equals("\"") && ssid.substring(ssid.length() - 1).equals("\"")) {
				ssid = ssid.substring(1, ssid.length() - 1);
			}
		}
		return ssid;
	}

	/**
	 * 用来获得手机扫描到的所有wifi的信息.
	 *
	 * @param c
	 *            上下文
	 * @return the current wifi scan result
	 */
	static public List<ScanResult> getCurrentWifiScanResult(Context c) {
		WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		wifiManager.startScan();
		return wifiManager.getScanResults();
	}

	static public String getConnectWifiSsid(Context c) {
		WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		return wifiInfo.getSSID();
	}

	// 以下是获得版本信息的工具方法

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}

	// 检测android 应用在前台还是后台

	public static boolean isBackground(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				/*
				 * BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000
				 * PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
				Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance
						+ ",context.getClass().getName()=" + context.getClass().getName());
				if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
					Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
					return true;
				} else {
					Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
}
