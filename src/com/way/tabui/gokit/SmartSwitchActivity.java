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
import com.way.tabui.commonmodule.GosBaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SmartSwitchActivity extends GosBaseActivity {

	private Button btn_open1, btn_colse1,btn_open2, btn_colse2,btn_open3, btn_colse3,btn_open4, btn_colse4;
	private TextView tv_mes;
	private LinearLayout li_main_curtain;
	/** The GizWifiDevice device */
	private GizWifiDevice device;
	/** The device statu. */
	private HashMap<String, Object> deviceStatu;
	
	//窗帘控制指令关键字
	private static final String KEY_Sendair = "kuozhan";

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
		initView();
		initDevice();
		initEvent();
	}
	private void initDevice() {
		//获取传递的参数
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		type=intent.getIntExtra("type",1);
		address=intent.getStringExtra("address");
		name=intent.getStringExtra("name");
		System.out.println("name:"+name+"\naddress"+address+"\ntype:"+type);
//		addresses = Integer.parseInt(intent.getStringExtra("address"));
		BYTES_ID = hexStringToByte(intent.getStringExtra("address"));
		//填充类型
		switch (type) {
			case 1:
				BYTES_BESE[0]=0;
				btn_open2.setVisibility(View.GONE);
				btn_colse2.setVisibility(View.GONE);
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 2:
				BYTES_BESE[0]=1;
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 3:
				BYTES_BESE[0]=2;
				btn_open4.setVisibility(View.GONE);
				btn_colse4.setVisibility(View.GONE);
				break;
			case 4:
				btn_open1.setVisibility(View.GONE);
				btn_colse1.setVisibility(View.GONE);
				btn_open2.setVisibility(View.GONE);
				btn_colse2.setVisibility(View.GONE);
				btn_open3.setVisibility(View.GONE);
				btn_colse3.setVisibility(View.GONE);
				BYTES_BESE[0]=3;
				break;
		}
		//填充ID
		for (int i=0;i<4;i++) {
			BYTES_BESE[i+1]=BYTES_ID[i];
		}
		//填充长度
		BYTES_BESE[6]=0;
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
					}
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

	//java int转byer
	public byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte)((i >> 24) & 0xFF);
		result[1] = (byte)((i >> 16) & 0xFF);
		result[2] = (byte)((i >> 8) & 0xFF);
		result[3] = (byte)(i & 0xFF);
		return result;
	}


	//java 合并两个byte数组
	public byte[] byteMerger(byte[] byte_1, byte[] byte_2){
		byte[] byte_3 = new byte[byte_1.length+byte_2.length];
		System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
		System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
		return byte_3;
	}

	//把16进制字符串转换成字节数组
	public byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}
