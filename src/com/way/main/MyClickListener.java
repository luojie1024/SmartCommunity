package com.way.main;

import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

public interface MyClickListener {

	public void onTogButton(BaseAdapter adapter,View view,int position);
	


}
