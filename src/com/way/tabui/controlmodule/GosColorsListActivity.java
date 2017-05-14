package com.way.tabui.controlmodule;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.way.tabui.gokit.R;

public class GosColorsListActivity extends GosControlModuleBaseActivity {

	ListView lvColor;

	ArrayList<String> colorsList;

	ColorsAdapter colorsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_colorlist);
		// 设置ActionBar
		setActionBar(true, true, R.string.set_group_LED);
		initData();
		initView();
		initEvent();
	}

	private void initView() {
		lvColor = (ListView) findViewById(R.id.lvColor);
		lvColor.setAdapter(colorsAdapter);
	}

	private void initData() {
		String[] colors = getResources().getStringArray(R.array.color);
		colorsList = new ArrayList<String>();
		for (String str : colors) {
			colorsList.add(str);
		}
		colorsAdapter = new ColorsAdapter(this, colorsList);
	}

	private void initEvent() {
		lvColor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				spf.edit().putInt("COLOR", arg2).commit();
				GosDeviceControlActivity.isUpDateUi = false;
				finish();
			}
		});
	}

	class ColorsAdapter extends BaseAdapter {

		Context context;
		ArrayList<String> colorsList;

		public ColorsAdapter(Context context, ArrayList<String> colorsList) {
			super();
			this.context = context;
			this.colorsList = colorsList;
		}

		@Override
		public int getCount() {
			return colorsList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (null == convertView) {
				convertView = View.inflate(context, R.layout.item_gos_colors_list, null);
			}

			TextView tvColorsText = (TextView) convertView.findViewById(R.id.tvColorsText);

			String colorText = colorsList.get(position);
			tvColorsText.setText(colorText);

			ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);
			int i = spf.getInt("COLOR", 0);
			if (position == i) {
				ivChoosed.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

	}
}
