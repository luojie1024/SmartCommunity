package com.way.tabui.configmodule;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.way.tabui.gokit.R;
import com.way.tabui.view.GifView;

public class GosDeviceResetActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The cb Select */
	CheckBox cbSelect;

	/** The tv Select */
	TextView tvSelect;

	/** The ll Next */
	LinearLayout llNext;

	/** The flag */
	String flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_gos_device_reset);
		// 设置ActionBar
		setActionBar(true, true, R.string.reset_device);

		initView();
		initEvent();
	}

	private void initView() {
		cbSelect = (CheckBox) findViewById(R.id.cbSelect);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		llNext = (LinearLayout) findViewById(R.id.llNext);

		/** 加载Gif */
		GifView gif = (GifView) findViewById(R.id.softreset);
		gif.setMovieResource(R.drawable.resetsoftap);

		/** 加载标志位 */
		flag = getIntent().getStringExtra("flag").toString();

	}

	private void initEvent() {
		llNext.setOnClickListener(this);
		tvSelect.setOnClickListener(this);
		llNext.setClickable(false);
		llNext.setBackgroundResource(R.drawable.button_shape_gray);

		cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					llNext.setBackgroundResource(R.drawable.button_shape);
					llNext.setClickable(true);
				} else {
					llNext.setBackgroundResource(R.drawable.button_shape_gray);
					llNext.setClickable(false);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llNext:
			if (TextUtils.isEmpty(flag)) {
				Intent intent = new Intent(GosDeviceResetActivity.this, GosChooseDeviceActivity.class);
				startActivity(intent);
			}
			finish();

			break;

		case R.id.tvSelect:
			if (cbSelect.isChecked()) {
				cbSelect.setChecked(false);
			} else {
				cbSelect.setChecked(true);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (TextUtils.isEmpty(flag)) {
				Intent intent = new Intent(GosDeviceResetActivity.this, GosDeviceReadyActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
			this.finish();

			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (TextUtils.isEmpty(flag)) {
				Intent intent = new Intent(GosDeviceResetActivity.this, GosDeviceReadyActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
			this.finish();

		}
		return true;
	}
}
