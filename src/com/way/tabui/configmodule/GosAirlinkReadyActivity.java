package com.way.tabui.configmodule;

import android.R.bool;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
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

public class GosAirlinkReadyActivity extends GosConfigModuleBaseActivity implements OnClickListener {

	/** The cb Select */
	CheckBox cbSelect;

	/** The tv Select */
	TextView tvSelect,textView1,textView2;

	/** The ll Next */
	LinearLayout llNext;

	boolean isGiz,islc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_gos_airlink_ready);
		// 设置ActionBar
		setActionBar(true, true, R.string.airlink_ready_title);
		Intent  intent =getIntent();
		isGiz = intent.getBooleanExtra("isGiz", false);
		islc = intent.getBooleanExtra("islc", false);
		
		initView();
		initEvent();
	}

	private void initView() {
		cbSelect = (CheckBox) findViewById(R.id.cbSelect);
		tvSelect = (TextView) findViewById(R.id.tvSelect);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		llNext = (LinearLayout) findViewById(R.id.llNext);

		/** 加载Gif */
		GifView gif = (GifView) findViewById(R.id.softreset);
		gif.setMovieResource(R.drawable.airlink);
		
		
		if(isGiz)
		{
			gif.setVisibility(View.VISIBLE);
			textView1.setVisibility(View.VISIBLE);
		}
		if(islc){
			textView2.setVisibility(View.VISIBLE);
		}

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
			Intent intent = new Intent(this, GosAirlinkConfigCountdownActivity.class);
			intent.putExtra("isGiz", isGiz);
			intent.putExtra("islc", islc);
			startActivity(intent);
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
			Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
			this.finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Intent intent = new Intent(this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		this.finish();
		return true;
	}

}
