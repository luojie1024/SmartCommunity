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

public class SmartCurtainActivity extends GosBaseActivity {

	private Button btn_open, btn_colse,btn_stop,btn_redic;
	private TextView tv_mes;
	private LinearLayout li_main_curtain;
	/** The GizWifiDevice device */
	private GizWifiDevice device;
	/** The device statu. */
	private HashMap<String, Object> deviceStatu;
	
	//窗帘控制指令关键字
	private static final String KEY_Sendair = "kuozhan";
	
	/** 指令代码0:开 1：停止  2：关 3：换向 */
	//private int[]  con_mes={14748160,14748416,14748672,14749952};

	private byte[] BYTES_BESE = {(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x30,(byte) 0x00, (byte) 0x04, (byte) 0xE1,(byte) 0x0A,
	(byte) 0x00, (byte) 0xEF};
//	private byte[] BYTES_END={(byte) 0x00, (byte) 0x04, (byte) 0xE1,(byte) 0x0A,
//	(byte) 0x00, (byte) 0xEF};
	private int addresses;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_curtain);
		initDevice();
		initView();
		initEvent();
	}
	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		addresses = Integer.parseInt(intent.getStringExtra("address"));
		byte[] bytes = intToByteArray(addresses);
		for (int i=0;i<4;i++) {
			BYTES_BESE[1+i]=bytes[i];
		}
	}
	
	private void initView() {
		btn_open = (Button) findViewById(R.id.btn_open);
		btn_colse = (Button) findViewById(R.id.btn_colse);
		btn_stop=(Button) findViewById(R.id.btn_stop);
		btn_redic=(Button) findViewById(R.id.btn_redic);
		
		tv_mes=(TextView) findViewById(R.id.tv_mes);
		
		li_main_curtain=(LinearLayout) findViewById(R.id.li_main_curtain);
	}

	private void initTextColor(){
		btn_open.setTextColor(getResources().getColor(R.color.color_blue));
		btn_redic.setTextColor(getResources().getColor(R.color.color_blue));
		btn_colse.setTextColor(getResources().getColor(R.color.color_blue));
		btn_stop.setTextColor(getResources().getColor(R.color.color_blue));
	}
	private void initEvent(){
		btn_open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BYTES_BESE[8]=10;
					sendJson(KEY_Sendair, BYTES_BESE);
					changemes("开");
					initTextColor();
					btn_open.setTextColor(getResources().getColor(R.color.golden));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "发送失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btn_stop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					BYTES_BESE[8]=11;
					sendJson(KEY_Sendair, BYTES_BESE);
					changemes("停止");
					initTextColor();
					btn_stop.setTextColor(getResources().getColor(R.color.golden));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "发送失败",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		btn_colse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BYTES_BESE[8]=12;
					sendJson(KEY_Sendair, BYTES_BESE);
					changemes("关");
					initTextColor();
					btn_colse.setTextColor(getResources().getColor(R.color.golden));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "发送失败",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		btn_redic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					BYTES_BESE[8]=17;
					sendJson(KEY_Sendair, BYTES_BESE);
					changemes("换向");
					initTextColor();
					btn_redic.setTextColor(getResources().getColor(R.color.golden));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "发送失败",
							Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
	}
	
	private void changemes(String mes){
		tv_mes.setText("状态："+mes);
		tv_mes.setVisibility(View.VISIBLE);
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
}
