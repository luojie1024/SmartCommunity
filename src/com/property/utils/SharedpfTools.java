package com.property.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedpfTools {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private static SharedpfTools instance;

	public SharedpfTools(Context context) {
		preferences = context.getSharedPreferences("userinfo",
				context.MODE_PRIVATE);
		editor = preferences.edit();
	}

	public static SharedpfTools getInstance(Context context) {
		if (instance == null) {
			instance = new SharedpfTools(context);
		}
		return instance;
	}

	/**
	 * 用户id
	 */
	public void setUid(String uid) {
		editor.putString("uid", uid);
		editor.commit();
	}

	public String getUid() {
		return preferences.getString("uid", "0");
	}

	/**
	 * 用户名
	 */
	public void setUname(String uname) {
		editor.putString("uname", uname);
		editor.commit();
	}

	public String getUname() {
		return preferences.getString("uname", "0");
	}

	/**
	 * 昵称
	 */
	public void setNickName(String nickname) {
		editor.putString("nickname", nickname);
		editor.commit();
	}

	public String getNickName() {
		return preferences.getString("nickname", "0");
	}

	/**
	 * 手机号
	 */
	public void setMobile(String mobile) {
		editor.putString("mobile", mobile);
		editor.commit();
	}

	public String getMobile() {
		return preferences.getString("mobile", "0");
	}

	/**
	 * 1业主，2员工，3客服
	 */
	public void setRole(String role) {
		editor.putString("role", role);
		editor.commit();
	}

	public String getRole() {
		return preferences.getString("role", "1");
	}

	public void setAccessToken(String accessToken) {
		editor.putString("accessToken", accessToken);
		editor.commit();
	}

	public String getAccessToken() {
		return preferences.getString("accessToken", "0");
	}

	public void setAppsercert(String appsercert) {
		editor.putString("appsercert", appsercert);
		editor.commit();
	}

	public String getAppsercert() {
		return preferences.getString("appsercert", "0");
	}
	
	/**
	 * 退出登录，清除缓存
	 */
	public void clear() {
		editor.remove("uid");
		editor.remove("uanme");
		editor.remove("nickname");
		editor.remove("mobile");
		editor.remove("role");
		editor.commit();
	}
}
