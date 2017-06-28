package com.way.tabui.gokit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.DatabaseAdapter;
import com.way.adapter.DatebaseHelper;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.ControlProtocol;
import com.way.util.SwitchInfo;

import org.json.JSONException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.way.util.ConvertUtil.hexStringToByte;

public class SmartSwitchActivity extends GosBaseActivity {

	private Button btn_open1, btn_colse1,btn_open2, btn_colse2,btn_open3, btn_colse3,btn_open4, btn_colse4;
	private TextView tv_mes;
	private LinearLayout li_main_curtain;
	/** The GizWifiDevice device */
	private GizWifiDevice device;
	/** The device statu. */
	private HashMap<String, Object> deviceStatu;
	private int status1=0,status2=0,status3=0;
	
	//窗帘控制指令关键字
	private static final String KEY_Sendair = "kuozhan";

	private SwitchInfo switchInfo;
	private DatabaseAdapter dbAdapter;
	private DatebaseHelper dbHelper;
	//传递的参数
	private String name,address;
	private  int type;


	/** 指令代码0:开 1：停止  2：关 3：换向 */
	private byte[] BYTES_ID = new byte[4];
	private byte[] BYTES_BESE=new byte[7];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_switch);

		initDB();
		initData();
		initView();
		upData();
		initEvent();
	}

	/**
	 * description:初始化传递过来的数据
	 * auther：joahluo
	 * time：2017/6/28 15:13
	 */
	private void initData() {
		//获取传递的参数
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		type=intent.getIntExtra("type",1);
		address=intent.getStringExtra("address");
		name=intent.getStringExtra("name");
		System.out.println("name:"+name+"\naddress"+address+"\ntype:"+type);

		BYTES_ID = hexStringToByte(address);
		//填充ID
		for (int i=0;i<4;i++) {
			BYTES_BESE[i+1]=BYTES_ID[i];
		}
		//填充长度
		BYTES_BESE[6]=0;
	}

	/**
	 * description:初始化数据库
	 * auther：joahluo
	 * time：2017/6/28 15:16
	 */
	private void initDB() {
		//操作数据库
		dbAdapter = new DatabaseAdapter(this);
		//创建数据库
		dbHelper = new DatebaseHelper(this);
		//数据
		switchInfo = new SwitchInfo();
	}


	/**
	 * description:更新数据
	 * auther：joahluo
	 * time：2017/6/28 15:17
	 */
	private void upData() {
		//根据ID地址查询状态信息
		switchInfo=dbAdapter.findSwitchInfoStatus(address);
		//更新UI
		upDataUI();
	}

	/**
	 * description:更新UI
	 * auther：joahluo
	 * time：2017/6/28 15:18
	 */
	private void upDataUI() {
		status1 = switchInfo.getStatus1();
		status2 = switchInfo.getStatus2();
		status3 = switchInfo.getStatus3();
		//填充类型
		switch (type) {
			case 1:
				if (status1 == 1) {
					btn_open1.setTextColor(getResources().getColor(R.color.golden));
					btn_colse1.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse1.setTextColor(getResources().getColor(R.color.golden));
					btn_open1.setTextColor(getResources().getColor(R.color.color_blue));
				}
				BYTES_BESE[0]=ControlProtocol.DevType.SWITCH_ONE;
				btn_open2.setVisibility(View.GONE);
				btn_colse2.setVisibility(View.GONE);
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 2:
				if (status1 == 1) {
					btn_open1.setTextColor(getResources().getColor(R.color.golden));
					btn_colse1.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse1.setTextColor(getResources().getColor(R.color.golden));
					btn_open1.setTextColor(getResources().getColor(R.color.color_blue));
				}
				if (status2 == 1) {
					btn_open2.setTextColor(getResources().getColor(R.color.golden));
					btn_colse2.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse2.setTextColor(getResources().getColor(R.color.golden));
					btn_open2.setTextColor(getResources().getColor(R.color.color_blue));
				}
				BYTES_BESE[0]=ControlProtocol.DevType.SWITCH_TWO;
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 3:
				if (status1 == 1) {
					btn_open1.setTextColor(getResources().getColor(R.color.golden));
					btn_colse1.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse1.setTextColor(getResources().getColor(R.color.golden));
					btn_open1.setTextColor(getResources().getColor(R.color.color_blue));
				}
				if (status2 == 1) {
					btn_open2.setTextColor(getResources().getColor(R.color.golden));
					btn_colse2.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse2.setTextColor(getResources().getColor(R.color.golden));
					btn_open2.setTextColor(getResources().getColor(R.color.color_blue));
				}
				if (status3 == 1) {
					btn_open3.setTextColor(getResources().getColor(R.color.golden));
					btn_colse3.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse3.setTextColor(getResources().getColor(R.color.golden));
					btn_open3.setTextColor(getResources().getColor(R.color.color_blue));
				}
				BYTES_BESE[0]=ControlProtocol.DevType.SWITCH_THREE;
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 4:
				if (status1 == 1) {
					btn_open4.setTextColor(getResources().getColor(R.color.golden));
					btn_colse4.setTextColor(getResources().getColor(R.color.color_blue));
				} else {
					btn_colse4.setTextColor(getResources().getColor(R.color.golden));
					btn_open4.setTextColor(getResources().getColor(R.color.color_blue));
				}
				btn_open1.setVisibility(View.GONE);
				btn_colse1.setVisibility(View.GONE);
				btn_open2.setVisibility(View.GONE);
				btn_colse2.setVisibility(View.GONE);
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				BYTES_BESE[0]= ControlProtocol.DevType.PLUG_FIVE;
				break;
		}
	}

	
	private void initView() {
		btn_open1 = (Button) findViewById(R.id.btn_open1);
		btn_colse1 = (Button) findViewById(R.id.btn_close1);
		btn_open2 = (Button) findViewById(R.id.btn_open2);
		btn_colse2 = (Button) findViewById(R.id.btn_close2);
		btn_open3 = (Button) findViewById(R.id.btn_open3);
		btn_colse3 = (Button) findViewById(R.id.btn_close3);
		btn_open4= (Button) findViewById(R.id.btn_open4);
		btn_colse4 = (Button) findViewById(R.id.btn_close4);
	}

	private void initTextColor(){
		btn_open1.setTextColor(getResources().getColor(R.color.color_blue));
		btn_open2.setTextColor(getResources().getColor(R.color.color_blue));
		btn_open3.setTextColor(getResources().getColor(R.color.color_blue));
		btn_open4.setTextColor(getResources().getColor(R.color.color_blue));
		btn_colse1.setTextColor(getResources().getColor(R.color.color_blue));
		btn_colse2.setTextColor(getResources().getColor(R.color.color_blue));
		btn_colse3.setTextColor(getResources().getColor(R.color.color_blue));
		btn_colse4.setTextColor(getResources().getColor(R.color.color_blue));
	}

	private void initEvent(){
		//一位开关
		btn_open1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					if (type == 1) {
						BYTES_BESE[5]=0x1;
					} else if (type == 2) {
						BYTES_BESE[5]=0x3;
					}else if (type == 3) {
						BYTES_BESE[5]=0x7;
					};
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_open1.setTextColor(getResources().getColor(R.color.golden));
					btn_colse1.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_colse1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (type == 1) {
						BYTES_BESE[5]=0x2;
					} else if (type == 2) {
						BYTES_BESE[5]=0x4;
					}else if (type == 3) {
						BYTES_BESE[5]=0x8;
					}
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_colse1.setTextColor(getResources().getColor(R.color.golden));
					btn_open1.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		//二位开关
		btn_open2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (type == 2) {
						BYTES_BESE[5]=0x5;
					} else if (type == 3) {
						BYTES_BESE[5]=0x9;
					}
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_open2.setTextColor(getResources().getColor(R.color.golden));
					btn_colse2.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_colse2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if (type == 2) {
						BYTES_BESE[5]=0x6;
					} else if (type == 3) {
						BYTES_BESE[5]=0xA;
					}
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_colse2.setTextColor(getResources().getColor(R.color.golden));
					btn_open2.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		//三位开关
		btn_open3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					BYTES_BESE[5]=0xB;
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_open3.setTextColor(getResources().getColor(R.color.golden));
					btn_colse3.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_colse3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					BYTES_BESE[5]=0xC;
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_colse3.setTextColor(getResources().getColor(R.color.golden));
					btn_open3.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		//插座开关
		btn_open4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					BYTES_BESE[5]=0xD;
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_open4.setTextColor(getResources().getColor(R.color.golden));
					btn_colse4.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_colse4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					BYTES_BESE[5]=0xE;
					sendJson(KEY_Sendair, BYTES_BESE);
					btn_colse4.setTextColor(getResources().getColor(R.color.golden));
					btn_open4.setTextColor(getResources().getColor(R.color.color_blue));
				} catch (JSONException e) {
					Toast.makeText(getApplicationContext(), "发送失败",
					Toast.LENGTH_SHORT).show();
				}
			}
		});

		
	}


	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.i("==", hashMap.toString());
	}

}
