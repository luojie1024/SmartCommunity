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

public class UpdataActivity extends GosBaseActivity {

	private EditText ed_name, ed_address;
	private Button bt_updatadevice;
	private DatabaseAdapter dbAdapter;
	private String address, name, bindgiz;
	private int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updata);
		dbAdapter = new DatabaseAdapter(this);
		initview();
		initdata();
		initevent();
		// Toast.makeText(getApplicationContext(), bindgiz,
		// Toast.LENGTH_SHORT).show();

	}

	private void initdata() {
		Intent intent = getIntent();
		bindgiz = intent.getStringExtra("bindgiz");
		address = intent.getStringExtra("address");
		name = intent.getStringExtra("name");
		id = intent.getIntExtra("id", 0);
		ed_name.setText(name);
		ed_address.setText(address);
	}

	private void initview() {
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_address = (EditText) findViewById(R.id.ed_address);
		bt_updatadevice = (Button) findViewById(R.id.bt_updatadevice);
	}

	private void initevent() {
		bt_updatadevice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (id != 0) {
					Gizinfo gizinfo = new Gizinfo(id, ed_name.getText()
							.toString(), ed_address.getText().toString(),
							bindgiz, "NULL", 0);
					dbAdapter.update(gizinfo);
					Toast.makeText(getApplicationContext(), "修改完毕",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "修改对象错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
