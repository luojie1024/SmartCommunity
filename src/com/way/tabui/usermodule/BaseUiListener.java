package com.way.tabui.usermodule;

import org.json.JSONObject;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class BaseUiListener implements IUiListener {

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onComplete(Object arg0) {
		doComplete((JSONObject)arg0);
	}

	protected void doComplete(JSONObject values) {
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stu
	}

}
