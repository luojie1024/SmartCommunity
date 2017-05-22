package com.way.tabui.actity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.way.adapter.DatabaseAdapter;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;

public class AddOcdeviceActivity extends GosBaseActivity {

	private EditText ed_name, ed_address;
	private Button bt_addocdevice;
	private DatabaseAdapter dbAdapter;
	private String bindgiz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ocdevice);
		dbAdapter = new DatabaseAdapter(this);
		Intent intent = getIntent();
		bindgiz = intent.getStringExtra("bindgiz");
		// Toast.makeText(getApplicationContext(), bindgiz,
		// Toast.LENGTH_SHORT).show();
		initview();
		initevent();

	}

	private void initview() {

		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_address = (EditText) findViewById(R.id.ed_address);
		bt_addocdevice = (Button) findViewById(R.id.bt_addocdevice);
	}

	private void initevent() {
		bt_addocdevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Gizinfo gizinfo = new Gizinfo(ed_name.getText().toString(),
						ed_address.getText().toString(), bindgiz, "NULL", 0);
				dbAdapter.add(gizinfo);
				Toast.makeText(getApplicationContext(), "添加完毕",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

}
