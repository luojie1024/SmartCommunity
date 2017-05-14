package com.property.utils;



import com.way.tabui.gokit.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class IconSelectDialog extends Dialog {

	public IconSelectDialog(Context context) {
		super(context, R.style.Theme_dialog);
		setContentView(R.layout.aim_layout_selecticon);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}

}
