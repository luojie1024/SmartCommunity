package com.way.tabui.configmodule;

import java.util.ArrayList;
import java.util.List;

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

public class GosModeListActivity extends GosConfigModuleBaseActivity {

	/** The lv Mode */
	ListView lvMode;

	/** The data */
	List<String> modeList;

	/** The Adapter */
	ModeListAdapter modeListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_modelist);
		// 设置ActionBar
		setActionBar(true, true, R.string.choose_mode_title);
		initData();
		initView();
		initEvent();
	}

	private void initView() {
		lvMode = (ListView) findViewById(R.id.lvMode);

		lvMode.setAdapter(modeListAdapter);// 初始化
	}

	private void initEvent() {

		lvMode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Toast.makeText(GosModeListActivity.this, arg2 + "",
				// Toast.LENGTH_SHORT).show();
				spf.edit().putInt("MODE", arg2).commit();
				finish();
			}
		});
	}

	private void initData() {
		modeList = new ArrayList<String>();
		String[] modes = this.getResources().getStringArray(R.array.mode);
		for (String string : modes) {
			modeList.add(string);
		}
		modeListAdapter = new ModeListAdapter(this, modeList);
	}

	class ModeListAdapter extends BaseAdapter {

		Context context;
		List<String> modeList;

		public ModeListAdapter(Context context, List<String> modeList) {
			super();
			this.context = context;
			this.modeList = modeList;
		}

		@Override
		public int getCount() {
			return modeList.size();
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
				convertView = View.inflate(context, R.layout.item_gos_mode_list, null);
			}
			
			TextView tvModeText = (TextView) convertView.findViewById(R.id.tvModeText);
			
			String modeText = modeList.get(position);
			tvModeText.setText(modeText);
			
			ImageView ivChoosed = (ImageView) convertView.findViewById(R.id.ivChoosed);
			int i = spf.getInt("MODE", 0);
			if (position == i) {
				ivChoosed.setVisibility(View.VISIBLE);
			}
			

			return convertView;
		}

	}

}
