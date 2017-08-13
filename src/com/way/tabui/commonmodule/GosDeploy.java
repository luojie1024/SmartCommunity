package com.way.tabui.commonmodule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.way.tabui.gokit.R;
import com.way.util.AssetsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GosDeploy {

	static Context context;

	public static HashMap<String, Object> infoMap;

	// 配置文件名称
	private static final String fileName = "UIConfig.json";

	// 输出json的路径
	public static String fileOutName = null;

	public GosDeploy(Context context) {
		super();
		GosDeploy.context = context;

		fileOutName = (context.getFilesDir().getAbsolutePath() + fileName);
		copyJson();
		readJSON();
	}

	/*
	 * ======================================================================
	 * 以下Key值用来对应JSON文件中的个配置信息的名称，用于配置App主要参数
	 * ======================================================================
	 */

	/** The App_ID Key */
	private static final String App_ID_Key = "app_id";

	/** The App_Secret Key */
	private static final String App_Secret_Key = "app_secret";

	/** The Product_Key_List Key */
	private static final String Product_Key_List_Key = "product_key";

	/** The Module_Select_Switch Key */
	private static final String Module_Select_On_Key = "wifi_type_select";

	/** The UsingTabSet_Switch Key */
	private static final String UsingTabSet = "UsingTabSet";

	/** The Tencent_App_ID Key */
	private static final String Tencent_App_ID_Key = "tencent_app_id";

	/** The Wechat_App_ID Key */
	private static final String Wechat_App_ID_Key = "wechat_app_id";

	/** The Wechat_App_Secret Key" */
	private static final String Wechat_App_Secret_Key = "wechat_app_secret";

	/** The Push_Type Key */
	private static final String Push_Type_Key = "push_type";

	// /** The Jpush_App_Key Key */
	// private static final String Jpush_App_Key = "jpush_app_key";

	/** The Bpush_App_Key Key */
	private static final String Bpush_App_Key = "bpush_app_key";

	/** The API_URL Key */
	private static final String API_URL_Key = "openAPI_URL";

	/** The Site_URL Key */
	private static final String SITE_URL_Key = "site_URL";

	/** The GDMS_URL Key */
	private static final String GDMS_URL_Key = "push_URL";

	/** The ButtonColor Key */
	private static final String ButtonColor_Key = "buttonColor";

	/** The ButtonTextColor Key */
	private static final String ButtonTextColor_Key = "buttonTextColor";

	/** The NavigationBarColor Key */
	private static final String NavigationBarColor_Key = "navigationBarColor";

	/** The NavigationBarTextColor Key */
	private static final String NavigationBarTextColor_Key = "navigationBarTextColor";

	/** The ConfigProgressViewColor Key */
	private static final String ConfigProgressViewColor_Key = "configProgressViewColor";

	/** The AddDeviceTitle Key */
	private static final String AddDeviceTitle_Key = "addDeviceTitle";

	/** The QQ Key */
	private static final String QQ = "qq";

	/** The Wechat Key */
	private static final String Wechat = "wechat";

	/** The AnonymousLogin Key */
	private static final String AnonymousLogin = "anonymousLogin";

	/** The StatusBarStyle Key */
	// private static final String StatusBarStyle_Key = "statusBarStyle";

	/*
	 * ===================================================================
	 * 以下是解析配置文件后，对各配置信息赋值的方法。
	 * ===================================================================
	 */

	/**
	 * 设置SDK参数--AppID（必填）
	 * 
	 * @return
	 */
	public static String setAppID() {

		return infoMap.get(App_ID_Key).toString();
	}

	/**
	 * 设置SDK参数--AppSecret（必填且必须与上述AppKey匹配）
	 * 
	 * @return
	 */
	public static String setAppSecret() {

		return infoMap.get(App_Secret_Key).toString();
	}

	/**
	 * 用来判断是否需要打开QQ登录
	 * 
	 * @return boolean
	 */
	public static boolean setQQ() {
		return (Boolean) infoMap.get(QQ);
	}

	/**
	 * 用来判断是否需要打开Wechat登录
	 * 
	 * @return boolean
	 */
	public static boolean setWechat() {
		return (Boolean) infoMap.get(Wechat);
	}

	/**
	 * 用来判断是否需要打开匿名登录
	 * 
	 * @return boolean
	 */
	public static boolean setAnonymousLogin() {
		return (Boolean) infoMap.get(AnonymousLogin);
	}

	/**
	 * 设置ProductKey
	 * 
	 * @return
	 */
	public static List<String> setProductKeyList() {
		List<String> productKeyList = new ArrayList<String>();
		JSONArray jsonArray = (JSONArray) infoMap.get(Product_Key_List_Key);

		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				productKeyList.add(jsonArray.getString(i));
				Log.i("Apptest", jsonArray.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return productKeyList;
	}

	/**
	 * 设置模组类型开关
	 * 
	 * @return
	 */
	public static int setModuleSelectOn() {
		int modeOnOff = View.INVISIBLE;

		String moduleSelectOn = infoMap.get(Module_Select_On_Key).toString();
		if (Boolean.parseBoolean(moduleSelectOn)) {
			modeOnOff = View.VISIBLE;
		}

		return modeOnOff;
	}
	
	
	/**
	 * 设置模组类型开关
	 * 
	 * @return
	 */
	public static int setUsingTabSetOn() {
		int modeOnOff = View.INVISIBLE;

		String moduleSelectOn = infoMap.get(UsingTabSet).toString();
		if (Boolean.parseBoolean(moduleSelectOn)) {
			modeOnOff = View.VISIBLE;
		}

		return modeOnOff;
	}

	/**
	 * 设置TencentID
	 * 
	 * @return
	 */
	public static String setTencentAppID() {

		return infoMap.get(Tencent_App_ID_Key).toString();
	}

	/**
	 * 设置WechatAppID
	 * 
	 * @return
	 */
	public static String setWechatAppID() {

		return infoMap.get(Wechat_App_ID_Key).toString();
	}

	/**
	 * 设置WeChatAppSecret
	 * 
	 * @return
	 */
	public static String setWechatAppSecret() {

		return infoMap.get(Wechat_App_Secret_Key).toString();
	}

	/**
	 * 设置推送类型开关（0：不开启，1：极光推送，2：百度推送。默认为0）
	 * 
	 * @return
	 */
	public static int setPushType() {
		int pushType = 0;

		int PushType_FromMap = Integer.parseInt(infoMap.get(Push_Type_Key).toString());
		if (PushType_FromMap == 1) {

			Toast.makeText(context, context.getResources().getString(R.string.push_type_string), Toast.LENGTH_SHORT).show();
			pushType = PushType_FromMap;
		} else if (PushType_FromMap == 2) {
			Toast.makeText(context, context.getResources().getString(R.string.push_type_string), Toast.LENGTH_SHORT).show();
			pushType = PushType_FromMap;
		}

		return pushType;
	}

	/**
	 * 设置BaiDuPushApp
	 * 
	 * @return
	 */
	public static String setBaiDuPushAppKey() {

		return infoMap.get(Bpush_App_Key).toString();
	}

	/**
	 * 设置openAPI_URL
	 * 
	 * @return
	 */
	public static String[] setApiURL() {
		String[] apiURL = { "", "" };

		String apiURL_FromMap = infoMap.get(API_URL_Key).toString();

		if (!TextUtils.isEmpty(apiURL_FromMap)) {
			String[] strings = apiURL_FromMap.split(":");
			if (strings.length == 2) {
				apiURL = strings;
			} else if (strings.length == 1) {
				apiURL[0] = strings[0];
				apiURL[1] = "80";
			}
		}

		return apiURL;
	}

	/**
	 * 设置SITE_URL
	 * 
	 * @return
	 */

	public static String[] setSiteURL() {
		String[] siteURL = { "", "" };

		String siteURL_FromMap = infoMap.get(SITE_URL_Key).toString();

		if (!TextUtils.isEmpty(siteURL_FromMap)) {
			String[] strings = siteURL_FromMap.split(":");
			if (strings.length == 2) {
				siteURL = strings;
			} else if (strings.length == 1) {
				siteURL[0] = strings[0];
				siteURL[1] = "80";
			}
		}

		return siteURL;
	}

	/**
	 * 设置GDMS_URL
	 * 
	 * @return
	 */
	public static String[] setGDMSURL() {
		String[] gdmsURL = { "", "" };

		String gdmsURL_FromMap = infoMap.get(GDMS_URL_Key).toString();

		if (!TextUtils.isEmpty(gdmsURL_FromMap)) {
			String[] strings = gdmsURL_FromMap.split(":");
			if (strings.length == 2) {
				gdmsURL = strings;
			} else if (strings.length == 1) {
				gdmsURL[0] = strings[0];
				gdmsURL[1] = "80";
			}
		}

		return gdmsURL;
	}

	/**
	 * 设置cloudService
	 * 
	 * @return
	 */
	public static ConcurrentHashMap<String, String> setCloudService() {
		String[] apiURl, siteURL, pushUrl;
		apiURl = setApiURL();
		siteURL = setSiteURL();
		pushUrl = setGDMSURL();
		ConcurrentHashMap<String, String> cloudServiceMap = new ConcurrentHashMap<String, String>();

		if (!TextUtils.isEmpty(apiURl[0])) {
			cloudServiceMap.put("openAPIDomain", apiURl[0]);
			cloudServiceMap.put("openAPIPort", apiURl[1]);
		}

		if (!TextUtils.isEmpty(siteURL[0])) {
			cloudServiceMap.put("siteDomain", siteURL[0]);
			cloudServiceMap.put("sitePort", siteURL[1]);
		}

		if (!TextUtils.isEmpty(pushUrl[0])) {
			cloudServiceMap.put("pushDomain", pushUrl[0]);
			cloudServiceMap.put("pushPort", pushUrl[1]);
		}

		return cloudServiceMap;
	}

	/**
	 * 设置Button背景颜色
	 * 
	 * @return
	 */
	public static Drawable setButtonBackgroundColor() {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(100);

		String ButtonColor_FromMap = infoMap.get(ButtonColor_Key).toString();
		if (!TextUtils.isEmpty(ButtonColor_FromMap)) {
			drawable.setColor(Color.parseColor("#" + ButtonColor_FromMap));
		} else {
			drawable.setColor(context.getResources().getColor(R.color.yellow));
		}

		return drawable;
	}

	/**
	 * 设置Button文字颜色
	 * 
	 * @return
	 */
	public static int setButtonTextColor() {
		int buttonTextcolor = context.getResources().getColor(R.color.black);
		String ButtonTextColor_FromMap = infoMap.get(ButtonTextColor_Key).toString();
		if (!TextUtils.isEmpty(ButtonTextColor_FromMap)) {
			buttonTextcolor = Color.parseColor("#" + ButtonTextColor_FromMap);
		}

		return buttonTextcolor;
	}

	/**
	 * 设置导航栏背景颜色
	 * 
	 * @return
	 */
	public static Drawable setNavigationBarColor() {
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);

		int navigationBarColor = context.getResources().getColor(R.color.yellow);

		String NavigationBarColor_FromMap = infoMap.get(NavigationBarColor_Key).toString();
		if (!TextUtils.isEmpty(NavigationBarColor_FromMap)) {
			navigationBarColor = Color.parseColor("#" + NavigationBarColor_FromMap);
		}
		drawable.setColor(navigationBarColor);

		return drawable;
	}

	/**
	 * 设置导航栏文字颜色
	 * 
	 * @return
	 */
	public static int setNavigationBarTextColor() {
		int navigationBarTextColor = context.getResources().getColor(R.color.black);
		String NavigationBarTextColor_FromMap = infoMap.get(NavigationBarTextColor_Key).toString();
		if (!TextUtils.isEmpty(NavigationBarTextColor_FromMap)) {
			navigationBarTextColor = Color.parseColor("#" + NavigationBarTextColor_FromMap);
		}
		return navigationBarTextColor;
	}

	/**
	 * 设置ConfigProgressView颜色
	 * 
	 * @return
	 */
	public static int setConfigProgressViewColor() {
		int configProgressViewColor = context.getResources().getColor(R.color.black);

		String ConfigProgressViewColor_FromMap = infoMap.get(ConfigProgressViewColor_Key).toString();
		if (!TextUtils.isEmpty(ConfigProgressViewColor_FromMap)) {
			configProgressViewColor = Color.parseColor("#" + ConfigProgressViewColor_FromMap);
		}

		return configProgressViewColor;
	}

	/**
	 * 设置添加设备标题
	 * 
	 * @return
	 */
	public static String setAddDeviceTitle() {

		String addDeviceTitle = context.getResources().getString(R.string.addDeviceTitle);
		String AddDeviceTitle_FromMap = infoMap.get(AddDeviceTitle_Key).toString();
		if (!TextUtils.isEmpty(AddDeviceTitle_FromMap)) {
			addDeviceTitle = AddDeviceTitle_FromMap;
		}

		return addDeviceTitle;

	}

	/**
	 * 读取本地的JSON文件
	 */
	private void readJSON() {
		try {
			FileInputStream input = new FileInputStream(fileOutName);
			InputStreamReader inputStreamReader = new InputStreamReader(input, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 加载JSON数据到Map
			setMap(stringBuilder);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private void setMap(StringBuilder stringBuilder) {
		infoMap = new HashMap<String, Object>();
		try {
			JSONObject root = new JSONObject(stringBuilder.toString());
			Iterator actions = root.keys();
			while (actions.hasNext()) {
				String param = actions.next().toString();
				Object value = root.get(param);
				infoMap.put(param, value);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// 拷贝json文件
	private void copyJson() {
		try {
			AssetsUtils.assetsDataToSD(fileOutName, fileName, context);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
