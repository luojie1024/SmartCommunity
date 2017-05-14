package com.way.tabui.controlmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.way.tabui.actity.GizService;
import com.way.tabui.gokit.R;

@SuppressLint({ "HandlerLeak", "ResourceAsColor" })
public class GosDeviceControlActivity extends GosControlModuleBaseActivity implements OnClickListener {

	/** The Constant TOAST. */
	protected static final int TOAST = 0;

	/** The Constant SETNULL. */
	protected static final int SETNULL = 1;

	/** The Constant UPDATE_UI. */
	protected static final int UPDATE_UI = 2;

	/** The Constant LOG. */
	protected static final int LOG = 3;

	/** The Constant RESP. */
	protected static final int RESP = 4;

	/** The Constant HARDWARE. */
	protected static final int HARDWARE = 5;

	/** The Disconnect */
	protected static final int DISCONNECT = 6;

	/*
	 * ===========================================================
	 * 以下key值对应http://site.gizwits.com/v2/datapoint?product_key={productKey}
	 * 中显示的数据点名称，sdk通过该名称作为json的key值来收发指令，demo中使用的key都是对应机智云实验室的微信宠物屋项目所用数据点
	 * ===========================================================
	 */
	/** led红灯开关 0=关 1=开. */
	private static final String KEY_RED_SWITCH = "LED_OnOff";//LED_OnOff
	/** 指定led颜色值 0=自定义 1=黄色 2=紫色 3=粉色. */
	private static final String KEY_LIGHT_COLOR = "LED_Color";//LED_Color
	/** led灯红色值 0-254. */
	private static final String KEY_LIGHT_RED = "LED_R";//LED_R
	/** led灯绿色值 0-254. */
	private static final String KEY_LIGHT_GREEN = "LED_G";//LED_G
	/** led灯蓝色值 0-254. */
	private static final String KEY_LIGHT_BLUE = "LED_B";
	/** 电机转速 －5～－1 电机负转 0 停止 1～5 电机正转. */
	private static final String KEY_SPEED = "Motor_Speed";
	/** 红外探测 0无障碍 1有障碍. */
	private static final String KEY_INFRARED = "Infrared";
	/** 环境温度. */
	private static final String KEY_TEMPLATE = "Temperature";
	/** 环境湿度. */
	private static final String KEY_HUMIDITY = "Humidity";
/**传输字符*/
	private static final String KEY_Mes = "Send_Mes";
	
	private static final String KEY_Gate ="gate1";
	private static final String KEY_Smoke ="smoke1";
	private static final String KEY_Gas ="gas1";
	private static final String KEY_Body ="body1";
	private static final String KEY_Sendcom ="Send_com";
	
	/** The sw red. */
	private Switch swRed;

	/** The sw infrared. */
	private Switch swInfrared;

	/** The sp color. */
	// private Spinner spColor;

	/** The ll color */
	private LinearLayout llColor;

	/** The tv red. */
	private TextView tvRed;

	/** The tv green. */
	private TextView tvGreen;

	/** The tv blue. */
	private TextView tvBlue;

	/** The tv speed. */
	private TextView tvSpeed;

	/** The tv template. */
	private TextView tvTemplate;

	/** The tv humidity. */
	private TextView tvHumidity;

	/** The tv ColorText */
	private TextView tvColorText;

	/** The sb red. */
	private SeekBar sbRed;

	/** The sb green. */
	private SeekBar sbGreen;

	/** The sb blue. */
	private SeekBar sbBlue;

	/** The sb speed. */
	private SeekBar sbSpeed;

	ImageView redsub, redadd, greensub, greenadd, bluesub, blueadd, speedsub, speedadd;
	
	private EditText ed_sendmes;
	
	private Button bt_sendmes;
	
	private Spinner sp_mes;
	
	private ImageView im_gate,im_smoke,im_gas,im_body;
	
	private TextView tv_gate,tv_smoke,tv_gas,tv_body;

	/** The GizWifiDevice device */
	private GizWifiDevice device,se_device;

	/** The device statu. */
	private HashMap<String, Object> deviceStatu;

	/** The First */
	// private boolean isFirst = true;

	/** The isUpDateUi */
	protected static boolean isUpDateUi = true;

	/** The Title */
	private String title;

	/** The colors list */
	ArrayList<String> colorsList;
	
	ArrayList<String> mesList;

	private boolean gasstua=false;
	private boolean smokestua=false;
	private boolean gatestua=false;
	private boolean bodystua=false;
	private String temperature;
	private String humidity;
	private StartThread startThread;
	private StopThread stopThread;
	private MyReceiver receiver;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_control);
		initDevice();
		 receiver=new MyReceiver();
		  IntentFilter filter=new IntentFilter();
		  filter.addAction("com.way.tabui.actity.GosDeviceListActivityReceviver");
		  filter.addAction("com.way.tabui.actity.GizService");
		  filter.addAction("com.way.tabui.actity.GizServiceTOAST");
		  filter.addAction("com.way.tabui.actity.GizServiceHARDWARE");
		  filter.addAction( "com.way.tabui.actity.GizServiceLOG");
		  GosDeviceControlActivity.this.registerReceiver(receiver,filter);
		if(!isWorked("com.way.tabui.actity.GizService")){
//			Toast.makeText(getApplicationContext(), "启动服务...", Toast.LENGTH_SHORT).show();
			 startThread=new StartThread();
			 startThread.start();
			 startThread.interrupt();
		}
		sendbroadcast();
		setActionBar(true, true, title);
		initData();
		initViews();
		initEvents();
		
		
//		if(device.getListener()==null);
//		device.setListener(gizWifiDeviceListener);
//		device.getDeviceStatus();
	}
	private class StartThread extends Thread{
		@Override
		public void run() {
			startsevice();
		}
	}
	
	void startsevice(){
		Intent reIntent = new Intent(this, GizService.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
		reIntent.putExtras(bundle);
		startService(reIntent);
	}
	
	private class StopThread extends Thread{
		@Override
		public void run() {
			stopsevice();
		}
	}
	
	void stopsevice(){
		Intent reIntent = new Intent(this, GizService.class);
		stopService(reIntent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		if (!isUpDateUi) {
			try {
				sendJson(KEY_LIGHT_COLOR, spf.getInt("COLOR", 0));
				tvColorText.setText(colorsList.get(spf.getInt("COLOR", 0)).toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		GosDeviceControlActivity.this.unregisterReceiver(receiver);
		super.onDestroy();
	}
	/**
	 * 初始化控件.
	 */
	private void initViews() {
		tvColorText = (TextView) findViewById(R.id.tvColorText);
		llColor = (LinearLayout) findViewById(R.id.ll_color);
		// spColor = (Spinner) findViewById(R.id.sp_color);
		swRed = (Switch) findViewById(R.id.sw_red);
		swInfrared = (Switch) findViewById(R.id.sw_infrared);
		tvRed = (TextView) findViewById(R.id.tv_red);
		tvGreen = (TextView) findViewById(R.id.tv_green);
		tvBlue = (TextView) findViewById(R.id.tv_blue);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvTemplate = (TextView) findViewById(R.id.tv_template);
		tvHumidity = (TextView) findViewById(R.id.tv_humidity);
		sbRed = (SeekBar) findViewById(R.id.sb_red);
		sbGreen = (SeekBar) findViewById(R.id.sb_green);
		sbBlue = (SeekBar) findViewById(R.id.sb_blue);
		sbSpeed = (SeekBar) findViewById(R.id.sb_speed);

		sp_mes=(Spinner) findViewById(R.id.sp_mes);
		im_gate=(ImageView) findViewById(R.id.im_gate);
		im_smoke=(ImageView) findViewById(R.id.im_smoke);
		im_gas=(ImageView) findViewById(R.id.im_gas);
		im_body=(ImageView) findViewById(R.id.im_body);
		
		tv_gate=(TextView) findViewById(R.id.tv_gate);
		tv_smoke=(TextView) findViewById(R.id.tv_smoke);
		tv_gas=(TextView) findViewById(R.id.tv_gas);
		tv_body=(TextView) findViewById(R.id.tv_body);
		
		ed_sendmes=(EditText) findViewById(R.id.ed_sendcom);
		
		bt_sendmes=(Button) findViewById(R.id.bt_sendcom);
		
		redadd = (ImageView) findViewById(R.id.redadd);
		redsub = (ImageView) findViewById(R.id.redsub);
		greenadd = (ImageView) findViewById(R.id.greenadd);
		greensub = (ImageView) findViewById(R.id.greensub);
		blueadd = (ImageView) findViewById(R.id.blueadd);
		bluesub = (ImageView) findViewById(R.id.bluesub);
		speedadd = (ImageView) findViewById(R.id.speedadd);
		speedsub = (ImageView) findViewById(R.id.speedsub);
		
		//
		
		

		String waitingText = (String) getText(R.string.waiting_device_ready);
		setProgressDialog(waitingText, true, false);
		
//		progressDialog.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					if (progressDialog.isShowing()) {
//						GosDeviceControlActivity.this.finish();
//						device.setSubscribe(false);
//						device.setListener(null);
//						return true;
//					}
//				}
//
//				return false;
//			}
//		});
		progressDialog.show();
	}

	/**
	 * 初始化监听器.
	 */
	private void initEvents() {
		redadd.setOnClickListener(this);
		redsub.setOnClickListener(this);
		greenadd.setOnClickListener(this);
		greensub.setOnClickListener(this);
		blueadd.setOnClickListener(this);
		bluesub.setOnClickListener(this);
		speedadd.setOnClickListener(this);
		speedsub.setOnClickListener(this);
		//
		
		swRed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					sendJson(KEY_RED_SWITCH, swRed.isChecked());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		llColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GosDeviceControlActivity.this, GosColorsListActivity.class);
				startActivity(intent);
			}
		});

		sbRed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_RED, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvRed.setText(progress + "");

			}
		});
		sbBlue.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_BLUE, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvBlue.setText(progress + "");

			}
		});
		sbGreen.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_LIGHT_GREEN, seekBar.getProgress());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvGreen.setText(progress + "");

			}
		});
		sbSpeed.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				try {
					sendJson(KEY_SPEED, seekBar.getProgress() - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvSpeed.setText((progress - 5) + "");
			}
		});
		
  bt_sendmes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					sendJson(KEY_Sendcom, ed_sendmes.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		sp_mes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				try {
					sendJson(KEY_Mes,mesList.get(position).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
  

	

	}

	
	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
//		Toast.makeText(getApplicationContext(), intent.getParcelableExtra("GizWifiDevice").toString(), Toast.LENGTH_LONG).show();
		deviceStatu = new HashMap<String, Object>();
		if (TextUtils.isEmpty(device.getAlias())) {
			title = device.getProductName();
		} else {
			title = device.getAlias();
		}
	}

	private void sendbroadcast(){
		 Intent intent=new Intent();
	     intent.setAction("com.way.tabui.actity.GosDeviceControlActivity");
	     sendBroadcast(intent);
	}
	
	String color, blue, red, green, speed;
	boolean isred, isInfrared;
	
	public class MyReceiver extends BroadcastReceiver {
	     @Override
	     public void onReceive(Context context, Intent intent) {
	      String action = intent.getAction();
	      Message msg = new Message();
//	  	Toast.makeText(getApplicationContext(), "action:"+action, Toast.LENGTH_SHORT).show();
	      
	    if(action.equals("com.way.tabui.actity.GizService")){
	    	temperature=intent.getStringExtra("temperature");
			humidity=intent.getStringExtra("humidity");   
			gasstua=intent.getBooleanExtra("gasstua", false);					
			gatestua=intent.getBooleanExtra("gatestua", false);
			bodystua=intent.getBooleanExtra("bodystua", false);	
			smokestua=intent.getBooleanExtra("smokestua", false);	
			isred=intent.getBooleanExtra("isred", false);
			isInfrared=intent.getBooleanExtra("isInfrared", false);
			color=intent.getStringExtra("color");
			red=intent.getStringExtra("red");
			green=intent.getStringExtra("green");
		    blue=intent.getStringExtra("blue");
		    speed=intent.getStringExtra("speed");
			msg.what = UPDATE_UI;
			handler.sendMessage(msg);
		    }
	    	if(action.equals("com.way.tabui.actity.GosDeviceListActivityReceviver")){
	    		 se_device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
	    		 if(!(device.getMacAddress()).equals(se_device.getMacAddress())){
	    			 stopThread=new StopThread();
	    			 stopThread.start();
	    			 stopThread.interrupt();
	    			 startThread=new StartThread();
	    			 startThread.start();
	    			 startThread.interrupt();
	    		 }
	    		 progressDialog.cancel();
	    	 }
	    	
	    	if(action.equals("com.way.tabui.actity.GizServiceTOAST")){
	    		msg.what = TOAST;
	    		msg.obj=intent.getStringExtra("Toastdata");
				handler.sendMessage(msg);
	    	}
	    	if(action.equals("com.way.tabui.actity.GizServiceHARDWARE")){
	    		msg.what =HARDWARE;
	    		msg.obj=intent.getStringExtra("Hardwaredata");
				handler.sendMessage(msg);
	    	}
	    	if(action.equals("com.way.tabui.actity.GizServiceLOG")){
	    		msg.what =LOG;
	    		msg.obj=intent.getStringExtra("Logdata");
				handler.sendMessage(msg);
	    	}
	     }
	 }
	/**
	 * 发送指令。格式为json。
	 * <p>
	 * 例如 {"entity0":{"attr2":74},"cmd":1}
	 * 其中entity0为gokit所代表的实体key，attr2代表led灯红色值，cmd为1时代表写入
	 * 。以上命令代表改变gokit的led灯红色值为74.
	 * 
	 * @param key
	 *            数据点对应的的json的key
	 * @param value
	 *            需要改变的值
	 * @throws JSONException
	 *             the JSON exception
	 */
	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.v("==",""+hashMap.get(key));
	//	Log.i("Apptest", hashMap.toString());
	}

	/** The handler. */
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			
			case UPDATE_UI:
				isUpDateUi = true;
				tvColorText.setText(colorsList.get(Integer.parseInt(color)));
//				spf.edit().putInt("COLOR", Integer.parseInt(deviceStatu.get(KEY_LIGHT_COLOR).toString())).commit();
				swRed.setChecked(isred);
				swInfrared.setChecked(isInfrared);
				tvBlue.setText(blue);
				tvGreen.setText(green);
				tvRed.setText(red);
				tvSpeed.setText(speed);
				tvTemplate.setText(temperature);
				tvHumidity.setText(humidity);
				if(gatestua){
					im_gate.setImageResource(R.drawable.ic_gate_a);
     				tv_gate.setText("门磁：开");
     				tv_gate.setTextColor(R.color.text_red);
				}else{
					im_gate.setImageResource(R.drawable.ic_gate_b);
					tv_gate.setText("门磁：关");
					tv_gate.setTextColor(R.color.black);
				}
				if(smokestua){
					im_smoke.setImageResource(R.drawable.ic_smoke_a);
     				tv_smoke.setText("烟雾：报警中");
     				tv_smoke.setTextColor(R.color.text_red);
				}else{
					im_smoke.setImageResource(R.drawable.ic_smoke_b);
					tv_smoke.setText("烟雾：正常");
					tv_smoke.setTextColor(R.color.black);
				}
				if(gasstua){
					im_gas.setImageResource(R.drawable.ic_gas_a);
     				tv_gas.setText("燃气：报警中");
     				tv_gas.setTextColor(R.color.text_red);
				}else{
					im_gas.setImageResource(R.drawable.ic_gas_b);
					tv_gas.setText("燃气：正常");
					tv_gas.setTextColor(R.color.black);
				}
				if(bodystua){
					im_body.setImageResource(R.drawable.ic_body_a);
     				tv_body.setText("人体感应：有人");
     				tv_body.setTextColor(R.color.text_red);
				}else{
					im_body.setImageResource(R.drawable.ic_body_b);
					tv_body.setText("人体感应：无人");
					tv_body.setTextColor(R.color.black);
				}
				
				if (blue != null) {
					sbBlue.setProgress(Integer.parseInt(blue));
				} else {
					sbBlue.setProgress(0);
				}

				if (green != null) {
					sbGreen.setProgress(Integer.parseInt(green));
				} else {
					sbBlue.setProgress(0);
				}

				if (red != null) {
					sbRed.setProgress(Integer.parseInt(red));
				} else {
					sbBlue.setProgress(0);
				}

				if (speed != null) {
					sbSpeed.setProgress(5 + Integer.parseInt(speed));
				} else {
					sbSpeed.setProgress(5);
				}
				break;
			case RESP:
//				String data = msg.obj.toString();
//				try {
//					showDataInUI(data);
//				} catch (JSONException e) {
//                	e.printStackTrace();
//				}
				break;
			case LOG:
				StringBuilder sb = new StringBuilder();
				JSONObject jsonObject;
				int logText = 1;
				try {
					jsonObject = new JSONObject(msg.obj.toString());
					for (int i = 0; i < jsonObject.length(); i++) {
						if (jsonObject.getBoolean(jsonObject.names().getString(i)) != false) {
							sb.append(jsonObject.names().getString(i) + " " + logText + "\r\n");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (sb.length() != 0) {
					Toast.makeText(GosDeviceControlActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
				}
				break;
			case TOAST:
				progressDialog.cancel();	
				String info = msg.obj.toString();
				if(info.equals((String) getText(R.string.set_info_successful))){
					Log.i("==", "title:"+title);
					actionBar.setTitle(title);
				}
				Toast.makeText(GosDeviceControlActivity.this, info, Toast.LENGTH_SHORT).show();
				break;
			case HARDWARE:
				showHardwareInfo((String) msg.obj);
				break;
			case DISCONNECT:
				String disconnectText = (String) getText(R.string.disconnect);
				Toast.makeText(GosDeviceControlActivity.this, disconnectText, Toast.LENGTH_SHORT).show();
				Intent i = getBaseContext().getPackageManager() 
						.getLaunchIntentForPackage(getBaseContext().getPackageName()); 
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						startActivity(i);
			     device.setSubscribe(false);
			     device.setListener(null);
				finish();
				break;
			}

		}
	};
//	@SuppressWarnings("rawtypes")
//	private void showDataInUI(String data) throws JSONException {
//		Log.i("revjsonn", data);
//		JSONObject receive = new JSONObject(data);
//		Iterator actions = receive.keys();
//		while (actions.hasNext()) {
//			String param = actions.next().toString();
//			Object value = receive.get(param);
//			deviceStatu.put(param, value);
//		}
//		Message msg = new Message();
//		msg.obj = data;
//		msg.what = UPDATE_UI;
//		handler.sendMessage(msg);
//	}

	/**
	 * 展示设备硬件信息
	 * 
	 * @param hardwareInfo
	 */
	private void showHardwareInfo(String hardwareInfo) {
		String hardwareInfoTitle = (String) getText(R.string.hardwareInfo);
		new AlertDialog.Builder(this).setTitle(hardwareInfoTitle).setMessage(hardwareInfo)
				.setPositiveButton(R.string.besure, null).show();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.redadd:
			int redNum1 = sbRed.getProgress();
			if (redNum1 < 254) {
				redNum1++;
				try {
					sendJson(KEY_LIGHT_RED, redNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.redsub:
			int redNum2 = sbRed.getProgress();
			if (redNum2 > 0) {
				redNum2--;
				try {
					sendJson(KEY_LIGHT_RED, redNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.greenadd:
			int greenNum1 = sbGreen.getProgress();
			if (greenNum1 < 254) {
				greenNum1++;
				try {
					sendJson(KEY_LIGHT_GREEN, greenNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.greensub:
			int greenNum2 = sbGreen.getProgress();
			if (greenNum2 > 0) {
				greenNum2--;
				try {
					sendJson(KEY_LIGHT_GREEN, greenNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.blueadd:
			int blueNum1 = sbBlue.getProgress();
			if (blueNum1 < 254) {
				blueNum1++;
				try {
					sendJson(KEY_LIGHT_BLUE, blueNum1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.bluesub:
			int blueNum2 = sbBlue.getProgress();
			if (blueNum2 > 0) {
				blueNum2--;
				try {
					sendJson(KEY_LIGHT_BLUE, blueNum2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.speedadd:
			int speedNum1 = sbSpeed.getProgress();
			if (speedNum1 < 10) {
				speedNum1++;
				try {
					sendJson(KEY_SPEED, speedNum1 - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.speedsub:
			int speedNum2 = sbSpeed.getProgress();
			if (speedNum2 > 0) {
				speedNum2--;
				try {
					sendJson(KEY_SPEED, speedNum2 - 5);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (device.isLAN()) {
			getMenuInflater().inflate(R.menu.devicecontrol_lan, menu);
		} else {
			getMenuInflater().inflate(R.menu.devicecontrol, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		
		switch (menu.getItemId()) {
		case android.R.id.home:	
			//设定返回值和返回标记
			setResult(11,backintent());
//			device.setSubscribe(false);
//			device.setListener(null);
			finish();
			break;
		// // 取消
		// case R.id.action_Cancle:

		// break;
		// 设置设备信息
		case R.id.action_setDeviceInfo:
			setDeviceInfo();
			break;

		// 获取设备硬件信息
		case R.id.action_getHardwareInfo:
			device.getHardwareInfo();
			break;

		// 获取设备状态
		case R.id.action_getStatu:
			String getingStatuText = (String) getText(R.string.getStatu);
			progressDialog.setMessage(getingStatuText);
			progressDialog.show();
			device.getDeviceStatus();
			break;

		default:
			break;
		}
		super.onOptionsItemSelected(menu);
		return true;
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 3];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 3] = hexArray[v >>> 4];
			hexChars[j * 3 + 1] = hexArray[v & 0x0F];
			hexChars[j * 3 + 2] = ' ';
		}
		return new String(hexChars);
	}

	private void setDeviceInfo() {

		final Dialog dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
		dialog.show();

		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_gos_set_device_info);

		final EditText etAlias;
		final EditText etRemark;
		etAlias = (EditText) window.findViewById(R.id.etAlias);
		etRemark = (EditText) window.findViewById(R.id.etRemark);

		LinearLayout llNo, llSure;
		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		if (!TextUtils.isEmpty(device.getAlias())) {
			etAlias.setText(device.getAlias());
		}
		if (!TextUtils.isEmpty(device.getRemark())) {
			etRemark.setText(device.getRemark());
		}

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				title=etAlias.getText().toString();
				device.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
				dialog.cancel();
				String loadingText = (String) getText(R.string.loadingtext);
				progressDialog.setMessage(loadingText);
				progressDialog.show();
			}
		});
	}

	private void initData() {
		String[] colors = getResources().getStringArray(R.array.color);
		colorsList = new ArrayList<String>();
		for (String str : colors) {
			colorsList.add(str);
		}
		String[] message = getResources().getStringArray(R.array.message);
		mesList = new ArrayList<String>();
		for (String str0 : message) {
			mesList.add(str0);
		}
		
		
		
	}

	private Intent backintent(){
//		spf.edit().putString("msgobj", "istrue").commit();
		Intent intent= new Intent();
		 Bundle bundle = new Bundle();
		 intent.putExtra("isoffline", false);
		bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
		intent.putExtras(bundle);
	   	 return intent;
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			//设定返回值和返回标记
//			spf.edit().putString("msgobj", "istrue").commit();
//			Intent intent= new Intent();
//			 Bundle bundle = new Bundle();
//			 intent.putExtra("isoffline", false);
//			bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
//			intent.putExtras(bundle);
			setResult(11, backintent());
//			device.setSubscribe(false);
//			device.setListener(null);
			finish();
		}
		return false;
	}

}
