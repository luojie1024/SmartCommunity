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
import com.way.util.CurtainInfo;
import com.way.util.GizMetaData;
import com.way.util.Gizinfo;
import com.way.util.SwitchInfo;

public class AddOcdeviceActivity extends GosBaseActivity {

	private EditText ed_name, ed_address;
	private Button bt_addocdevice;
	private DatabaseAdapter dbAdapter;
	private String bindgiz;
    private String tablename;
	private String name;
	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ocdevice);
		dbAdapter = new DatabaseAdapter(this);
		Intent intent = getIntent();
		bindgiz = intent.getStringExtra("bindgiz");
        tablename =intent.getStringExtra("tablename");
		name=intent.getStringExtra("name");
		initview();
		initevent();

	}

	private void initview() {
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_address = (EditText) findViewById(R.id.ed_address);
		bt_addocdevice = (Button) findViewById(R.id.bt_addocdevice);
	}

	/**
	 * description:添加设备，修复地址添加（大写转换）
	 * auther:joahluo
	 * updata:2017/7/6 15:28
	 * version:2.0
	 */
	private void initevent() {
		bt_addocdevice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                if(tablename.equals(GizMetaData.GizTable.TABLE_NAME)){
				Gizinfo gizinfo = new Gizinfo(ed_name.getText().toString(),
						ed_address.getText().toString(), bindgiz, "NULL", 0);
				dbAdapter.add(gizinfo);
                }else if(tablename.equals(GizMetaData.CurtainTable.TABLE_NAME)){
                    CurtainInfo curtainInfo =new CurtainInfo(ed_name.getText().toString(),
                            ed_address.getText().toString(), bindgiz, "NULL", 0);
                    dbAdapter.addCurtianInfo(curtainInfo);
                }else if(tablename.equals(GizMetaData.SwitchTable.TABLE_NAME)) {
					//type表示开关类型
					if ("一位开关".equals(name)) {
						type=1;
					} else if ("二位开关".equals(name)) {
						type=2;
					}else if ("三位开关".equals(name)) {
						type=3;
					}else if ("插座".equals(name)) {
						type=4;
					}
					SwitchInfo switchinfo = new SwitchInfo(ed_name.getText().toString(),
					ed_address.getText().toString().toUpperCase(), bindgiz, "NULL", 0, type);
					dbAdapter.addSwitchInfo(switchinfo);
					finish();
				}
				Toast.makeText(getApplicationContext(), "添加完毕",
						Toast.LENGTH_SHORT).show();

			}
		});

	}

}
