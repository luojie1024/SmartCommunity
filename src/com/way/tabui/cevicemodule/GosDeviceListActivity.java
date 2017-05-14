package com.way.tabui.cevicemodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import zxing.CaptureActivity;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.way.tabui.GosApplication;
import com.way.tabui.actity.GizService;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.actity.GizService.MyReceiver;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.commonmodule.NetUtils;
import com.way.tabui.configmodule.GosAirlinkChooseDeviceWorkWiFiActivity;
import com.way.tabui.configmodule.GosCheckDeviceWorkWiFiActivity;
import com.way.tabui.controlmodule.GosDeviceControlActivity;
import com.way.tabui.gokit.R;
import com.way.tabui.pushmodule.GosPushManager;
import com.way.tabui.settingsmodule.GosSettiingsActivity;
import com.way.tabui.view.SlideListView2;
import com.way.util.ExitAppReceiver;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

@SuppressLint("HandlerLeak")
public class GosDeviceListActivity extends GosDeviceModuleBaseActivity implements OnClickListener {

	/** The ll NoDevice */
	private LinearLayout llNoDevice;

	/** The img NoDevice */
	private ImageView imgNoDevice;

	/** The tv NoDevice */
	private TextView tvNoDevice;

	/** The ic BoundDevices */
	private View icBoundDevices;

	/** The ic FoundDevices */
	private View icFoundDevices;

	/** The ic OfflineDevices */
	private View icOfflineDevices;

	/** The tv BoundDevicesListTitle */
	private TextView tvBoundDevicesListTitle;

	/** The tv FoundDevicesListTitle */
	private TextView tvFoundDevicesListTitle;

	/** The tv OfflineDevicesListTitle */
	private TextView tvOfflineDevicesListTitle;
	
	private Button bt_offline_login;

	/** The ll NoBoundDevices */
	private LinearLayout llNoBoundDevices;

	/** The ll NoFoundDevices */
	private LinearLayout llNoFoundDevices;

	/** The ll NoOfflineDevices */
	private LinearLayout llNoOfflineDevices;

	/** The slv BoundDevices */
	private SlideListView2 slvBoundDevices;

	/** The slv FoundDevices */
	private SlideListView2 slvFoundDevices;

	/** The slv OfflineDevices */
	private SlideListView2 slvOfflineDevices;

	/** The sv ListGroup */
	private ScrollView svListGroup;

	/** 适配器 */
	GosDeviceListAdapter myadapter;

	/** 设备列表分类 */
	List<GizWifiDevice> boundDevicesList, foundDevicesList, offlineDevicesList;

	/** 设备热点名称列表 */
	ArrayList<String> softNameList;

	Intent intent;

	String softssid, uid, token;

	boolean isItemClicked = false;

	// boolean isLogout = false;
	//
	// public static boolean isAnonymousLoging = false;

	/**
	 * 判断用户登录状态 0：未登录 1：实名用户登录 2：匿名用户登录 3：匿名用户登录中 4：匿名用户登录中断
	 */
	public static int loginStatus;

	int threeSeconds = 3;

	/** 获取设备列表 */
	protected static final int GETLIST = 0;

	/** 刷新设备列表 */
	protected static final int UPDATALIST = 1;

	/** 订阅成功前往控制页面 */
	protected static final int TOCONTROL = 2;

	/** 通知 */
	protected static final int TOAST = 3;

	/** 设备解绑 */
	protected static final int UNBOUND = 99;
   /** 修改设备备注名称*/
	protected static final int RENAME = 100;
	/** 新设备提醒 */
	protected static final int SHOWDIALOG = 999;
	/** 是否来自主界面跳转 */
	boolean ismain;
	/** 所解绑设备是否和在线设备一致 */
	boolean isequally;
	
	/** 传过来正在连接中的的设备 */
	private GizWifiDevice devices=null;
	/** 要解绑的的设备 */
	private GizWifiDevice ubdevice=null;
	/** 修改名字的设备*/
	private GizWifiDevice rndevice;
	
	GosPushManager gosPushManager;

	boolean isback =false;
	boolean isoffline ;
	private MyReceiver receiver =null;
	
	Handler handler = new Handler() {
		private AlertDialog myDialog;
		private TextView dialog_name;

		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GETLIST:
				GizWifiSDK.sharedInstance().getBoundDevices(uid, token, GosConstant.ProductKeyList);
				if (loginStatus == 0) {
					loginStatus = 3;
					GizWifiSDK.sharedInstance().userLoginAnonymous();
					// TODO isAnonymousLoging = true;
				}

				break;

			case UPDATALIST:
				progressDialog.cancel();
				UpdateUI();
				break;

			case UNBOUND:
				progressDialog.show();
				String mac=spf.getString("msgobj", "nodevice");
				ubdevice=(GizWifiDevice) msg.obj;
				if(mac.equals(ubdevice.getMacAddress()))
					isequally=true;
				else
					isequally=false;
				GizWifiSDK.sharedInstance().unbindDevice(uid, token, ubdevice.getDid());
				break;

			case RENAME:

				rndevice=(GizWifiDevice) msg.obj;
				setDeviceInfo();
			break;

			case TOCONTROL:
			
				intent = new Intent(GosDeviceListActivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", (GizWifiDevice) msg.obj);
				intent.putExtras(bundle);
				 intent.putExtra("isoffline",false);
				 if(!ismain){
					 startActivity(intent);
				finish();
				 }else{
					setResult(1001, intent);
					 finish();
				 }
				break;

			case TOAST:
				
				Toast.makeText(GosDeviceListActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
				break;

			case SHOWDIALOG:

				if (!softNameList.toString()
						.contains(GosMessageHandler.getSingleInstance().getNewDeviceList().toString())) {
					AlertDialog.Builder builder = new AlertDialog.Builder(GosDeviceListActivity.this);
					View view = View.inflate(GosDeviceListActivity.this, R.layout.alert_gos_new_device, null);
					Button diss = (Button) view.findViewById(R.id.diss);
					Button ok = (Button) view.findViewById(R.id.ok);
					dialog_name = (TextView) view.findViewById(R.id.dialog_name);
					String foundOneDevice, foundManyDevices;
					foundOneDevice = (String) getText(R.string.not_text);
					foundManyDevices = (String) getText(R.string.found_many_devices);
					if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() < 1) {
						return;
					}
					if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
						String ssid = GosMessageHandler.getSingleInstance().getNewDeviceList().get(0);
						if (!TextUtils.isEmpty(ssid)
								&& ssid.equalsIgnoreCase(NetUtils.getCurentWifiSSID(GosDeviceListActivity.this))) {
							return;
						}
						if (softNameList.toString().contains(ssid)) {
							return;
						}
						softNameList.add(ssid);
						dialog_name.setText(ssid + foundOneDevice);
						softssid = ssid;
					} else {
						for (String s : GosMessageHandler.getSingleInstance().getNewDeviceList()) {
							if (!softNameList.toString().contains(s)) {
								softNameList.add(s);
							}
						}
						dialog_name.setText(foundManyDevices);
					}
					myDialog = builder.create();
					Window window = myDialog.getWindow();
					myDialog.setView(view);
					myDialog.show();
					window.setGravity(Gravity.BOTTOM);
					ok.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (GosMessageHandler.getSingleInstance().getNewDeviceList().size() == 1) {
								Intent intent = new Intent(GosDeviceListActivity.this,
										GosCheckDeviceWorkWiFiActivity.class);
								intent.putExtra("softssid", softssid);
								startActivity(intent);
							} else {
								Intent intent = new Intent(GosDeviceListActivity.this,
										GosCheckDeviceWorkWiFiActivity.class);
								startActivity(intent);
							}
							myDialog.cancel();
						}
					});
					diss.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							myDialog.cancel();
						}
					});
				}
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//initsdk();
		setContentView(R.layout.activity_gos_device_list);
		Intent intentfalg = getIntent();
		ismain=intentfalg.getBooleanExtra("ismain", false);
		isoffline=intentfalg.getBooleanExtra("isoffline", false);
		setActionBar(true, false, R.drawable.reflash_bt, R.string.devicelist_title);
		GosMessageHandler.getSingleInstance().StartLooperWifi(this);
		softNameList = new ArrayList<String>();
//		initReceiver();
		if(ismain)
		getdevices();	
		else{
			login();
			if(isWorked("com.way.tabui.actity.GizService")){
				sendbroadcast();
				count++;
			}else{
			stopsevice();
			}
		}
		
		initData();
		initView();
		initEvent();
		init();
	}
	
	private void sendbroadcast(){
		 Intent intent=new Intent();
	     intent.setAction("com.way.tabui.actity.GosDeviceListActivity");
	     sendBroadcast(intent);
	}
	
	 public class MyReceiver extends BroadcastReceiver {
	     @Override
	     public void onReceive(Context context, Intent intent) {
	    	 String action = intent.getAction();
		      Message msg = new Message();
		      if(action.equals("com.way.tabui.actity.GosDeviceListActivityReceviver")){
	    	   devices = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
	    	   if((devices!=null)&&(!ismain)){
	    		Toast.makeText(getApplicationContext(), "进入后台所连接设备..", Toast.LENGTH_SHORT).show();
	 			Intent rintent =new Intent(GosDeviceListActivity.this,MainActivity.class);
	 			Bundle bundle = new Bundle();
	 			bundle.putParcelable("GizWifiDevice",(GizWifiDevice) devices);
	 			rintent.putExtras(bundle);
	 			rintent.putExtra("isworked", true);
	 			startActivity(rintent);	
	 			finish();
	 			}
	     }
		      if(action.equals("com.way.tabui.actity.GizServiceTOAST")){
		    	  msg.what = TOAST;
		    		msg.obj=intent.getStringExtra("Toastdata");
					handler.sendMessage(msg);
		      }
	     }
	 }
	     	
			
	void stopsevice(){
		Intent reIntent = new Intent(GosDeviceListActivity.this, GizService.class);
		stopService(reIntent);
	}
	
	
	
	 IXmSystem xmSystem;
	 
	 private void init(){
	        xmSystem = XmSystem.getInstance();
	        xmSystem.xmInit(this, "CN", new OnXmSimpleListener() {
	            @Override
	            public void onErr(XmErrInfo info) {
	                Log.v("AAAAA", "init Fail");
	            }

	            @Override
	            public void onSuc() {
	                Log.v("AAAAA", "init Suc");
	            }
	        });
	    }
	 private void login(){
	        String username = "13135367953";
	        String psw = "chen162858";
	        if(username.equals("")||psw.equals("")){
	            Toast.makeText(this,"用户名或密码不能为空！",Toast.LENGTH_LONG).show();
	            return;
	        }
//	        showLoadingDialog();
	        try {
	            xmSystem.xmLogin(username, psw, new OnXmListener<XmAccount>() {
	                @Override
	                public void onSuc(XmAccount outinfo) {
//	                    closeLoadingDialog();
//	                    mHandler.sendEmptyMessage(0x123);
//	                    sp.setUsername(et_username.getText().toString());
//	                    loginSuc(outinfo);
	                }

	                @Override
	                public void onErr(XmErrInfo info) {
//	                    closeLoadingDialog();
//	                    mHandler.sendEmptyMessage(0x124);
	                }
	            });
	        } catch (Exception e) {
	            e.printStackTrace();
//	            closeLoadingDialog();
//	            mHandler.sendEmptyMessage(0x124);
	        }finally {

	        }
	    }


	
	
//	private GizWifiDevice lastdevices;
	public void getdevices(){
		try {
			Intent intents = getIntent();
			devices = (GizWifiDevice) intents.getParcelableExtra("GizWifiDevice");
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		

		
	}

	public void isokintent(){
		        String msgobj=spf.getString("msgobj", "nodevice");
				if((!ismain)&&msgobj.equals(gizd.getMacAddress())){
				Toast.makeText(GosDeviceListActivity.this, "正在进入上次设备...", Toast.LENGTH_SHORT).show();
				gizd.setListener(getGizWifiDeviceListener());
				gizd.setSubscribe(true);
				}
				else{
					count=0;
				}
	}
	@Override
	protected void onResume() {
		
		
		initReceiver();
		JPushInterface.onResume(this);
		progressDialog.show();
		Log.i("Apptest", loginStatus + "onResume");
		// TODO isLogout = false;
		handler.sendEmptyMessage(GETLIST);
		super.onResume();
		// TODO GosMessageHandler.getSingleInstance().SetHandler(handler);
	}
     @Override
    protected void onDestroy() {
	// TODO Auto-generated method stub
//	  count=0;
//    	 unregisterReceiver(receiver);
	  super.onDestroy();
    }

	@Override
	public void onPause() {
		
		unregisterReceiver(receiver);
		JPushInterface.onPause(this);
		Log.i("Apptest", loginStatus + "onPause");
		// TODO GosMessageHandler.getSingleInstance().SetHandler(null);
		super.onPause();
	}
	
	
	private void initsdk(){
		try {
			GosConstant.App_ID=spf.getString("appid", "a61ed92da3764cca848f3dbab8481149");
			GosConstant.App_Screct=spf.getString("appscrect", "57c13265403549ac83d828e50639c37a");
			GosConstant.device_ProductKey=spf.getString("prroductkey", "330b43e5cd9b4aa9a03fc97c5f6f52a4");
			// 启动SDK
			GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), GosConstant.App_ID);
			// 只能选择支持其中一种
			 gosPushManager=new GosPushManager(GizPushType.GizPushJiGuang,this);//极光推送
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "设备配置错误，请重新配置绑定", Toast.LENGTH_LONG).show();
			intent = new Intent(GosDeviceListActivity.this, GosSettiingsActivity.class);
			startActivity(intent);
			
		}
		
	}

	private void initView() {
		
		bt_offline_login =(Button) findViewById(R.id.bt_offline_login);
		if(ismain){
			bt_offline_login.setVisibility(View.GONE);
		}else{
			bt_offline_login.setVisibility(View.VISIBLE);
		}
		
		svListGroup = (ScrollView) findViewById(R.id.svListGroup);
		llNoDevice = (LinearLayout) findViewById(R.id.llNoDevice);
		imgNoDevice = (ImageView) findViewById(R.id.imgNoDevice);
		tvNoDevice = (TextView) findViewById(R.id.tvNoDevice);

		icBoundDevices = findViewById(R.id.icBoundDevices);
		icFoundDevices = findViewById(R.id.icFoundDevices);
		icOfflineDevices = findViewById(R.id.icOfflineDevices);

		slvBoundDevices = (SlideListView2) icBoundDevices.findViewById(R.id.slideListView1);
		slvFoundDevices = (SlideListView2) icFoundDevices.findViewById(R.id.slideListView1);
		slvOfflineDevices = (SlideListView2) icOfflineDevices.findViewById(R.id.slideListView1);

		llNoBoundDevices = (LinearLayout) icBoundDevices.findViewById(R.id.llHaveNotDevice);
		llNoFoundDevices = (LinearLayout) icFoundDevices.findViewById(R.id.llHaveNotDevice);
		llNoOfflineDevices = (LinearLayout) icOfflineDevices.findViewById(R.id.llHaveNotDevice);

		tvBoundDevicesListTitle = (TextView) icBoundDevices.findViewById(R.id.tvListViewTitle);
		tvFoundDevicesListTitle = (TextView) icFoundDevices.findViewById(R.id.tvListViewTitle);
		tvOfflineDevicesListTitle = (TextView) icOfflineDevices.findViewById(R.id.tvListViewTitle);

		String boundDevicesListTitle = (String) getText(R.string.bound_divices);
		tvBoundDevicesListTitle.setText(boundDevicesListTitle);
		String foundDevicesListTitle = (String) getText(R.string.found_devices);
		tvFoundDevicesListTitle.setText(foundDevicesListTitle);
		String offlineDevicesListTitle = (String) getText(R.string.offline_devices);
		tvOfflineDevicesListTitle.setText(offlineDevicesListTitle);

	}

	private void initEvent() {
		imgNoDevice.setOnClickListener(this);
		tvNoDevice.setOnClickListener(this);
		
		bt_offline_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				GizWifiDevice dv1=null;
//				dv1.setListener(getGizWifiDeviceListener());
//				dv1.setSubscribe(false);
				Intent intent = new Intent(GosDeviceListActivity.this,MainActivity.class);
				intent.putExtra("isoffline", true);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice", (GizWifiDevice) dv1);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});

		slvFoundDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				progressDialog.show();
				slvFoundDevices.setEnabled(false);
				slvFoundDevices.postDelayed(new Runnable() {
					@Override
					public void run() {
						slvFoundDevices.setEnabled(true);
					}
				}, 3000);
				GizWifiDevice device = foundDevicesList.get(position);
				device.setListener(getGizWifiDeviceListener());
				device.setSubscribe(true);
//				Toast.makeText(getApplicationContext(), "click ok", Toast.LENGTH_SHORT).show();

			}
		});

		slvBoundDevices.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				progressDialog.show();
				slvBoundDevices.setEnabled(false);
				slvBoundDevices.postDelayed(new Runnable() {
					@Override
					public void run() {
						slvBoundDevices.setEnabled(true);
					}
				}, 3000);
				GizWifiDevice device = boundDevicesList.get(position);
				    count++;
					Message msg = new Message();
					msg.what = TOCONTROL;
					msg.obj = device;
					handler.sendMessage(msg);
			}
		});

		slvBoundDevices.initSlideMode(SlideListView2.MOD_RIGHT);
		slvOfflineDevices.initSlideMode(SlideListView2.MOD_RIGHT);
	}

	private void initData() {
		uid = spf.getString("Uid", "");
		token = spf.getString("Token", "");

		// 可以在此处把关心的设备productKey加入到过滤列表中
		addProductKey(GosConstant.device_ProductKey);
		
		if (uid.isEmpty() && token.isEmpty()) {
			loginStatus = 0;
		}
	}

	private void initReceiver(){
		receiver = new MyReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.way.tabui.actity.GosDeviceListActivityReceviver");
		filter.addAction("com.way.tabui.actity.GizServiceTOAST");
		registerReceiver(receiver, filter);
	}
	
   protected void didDiscovered(GizWifiErrorCode result, java.util.List<GizWifiDevice> deviceList) {
		GosDeviceModuleBaseActivity.deviceslist.clear();
		for (GizWifiDevice gizWifiDevice : deviceList) {
			GosDeviceModuleBaseActivity.deviceslist.add(gizWifiDevice);
		}
		handler.sendEmptyMessage(UPDATALIST);
	}

	protected void didUserLogin(GizWifiErrorCode result, java.lang.String uid, java.lang.String token) {

		// TODO isAnonymousLoging = false;
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			loginStatus = 2;
			this.uid = uid;
			this.token = token;
			spf.edit().putString("Uid", this.uid).commit();
			spf.edit().putString("Token", this.token).commit();
			handler.sendEmptyMessage(GETLIST);
			// TODO 绑定推送
//			GosPushManager.pushBindService(token);
		} else {
			loginStatus = 0;
			tryUserLoginAnonymous();
		}
	}

	protected void didUnbindDevice(GizWifiErrorCode result, java.lang.String did) {
		progressDialog.cancel();
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			String unBoundFailed = (String) getText(R.string.unbound_failed);
			Toast.makeText(this, unBoundFailed + "\n" + result, Toast.LENGTH_SHORT).show();
		}else{
			if(isequally){
				stopsevice();
				devices=null;
				isoffline=true;
				spf.edit().putString("msgobj","null").commit();
			}
			Toast.makeText(this, "解绑成功", Toast.LENGTH_SHORT).show();
		}
	}


	GizWifiDevice gizd;
	GizWifiDevice gizd0;
	String Maca;
	@Override
	protected void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1, boolean arg2) {
		// TODO 控制页面跳转
		progressDialog.cancel();
		Message msg = new Message();
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == arg0) {
			msg.what = TOCONTROL;
			msg.obj = arg1;
		} else {
			if (arg1.isBind()) {
				msg.what = TOAST;
				String setSubscribeFail = (String) getText(R.string.setsubscribe_failed);
				msg.obj = setSubscribeFail + "\n" + arg0;
			}
		}
		handler.sendMessage(msg);
	}

	/**
	 * 推送绑定回调
	 * 
	 * @param result
	 */
	@Override
	protected void didChannelIDBind(GizWifiErrorCode result) {
		Log.i("Apptest", result.toString());
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (!TextUtils.isEmpty(spf.getString("UserName", "")) && !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			getMenuInflater().inflate(R.menu.devicelist_logout, menu);
		} else {
			if (getIntent().getBooleanExtra("ThredLogin", false)) {
				getMenuInflater().inflate(R.menu.devicelist_logout, menu);
			} else {
				getMenuInflater().inflate(R.menu.devicelist_login, menu);
			}
		}

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case android.R.id.home:
			if (checkNetwork(GosDeviceListActivity.this)) {
				progressDialog.show();
				handler.sendEmptyMessage(GETLIST);
			}
			break;
		case R.id.action_QR_code:
			intent = new Intent(GosDeviceListActivity.this, CaptureActivity.class);
			startActivity(intent);
			break;
		case R.id.action_change_user:

			if (item.getTitle() == getText(R.string.login)) {
				logoutToClean();
				break;
			}
			new AlertDialog.Builder(this).setTitle(R.string.prompt).setMessage(R.string.cancel_logout)
					.setPositiveButton(R.string.besure, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							logoutToClean();
						}
					}).setNegativeButton(R.string.no, null).show();
			break;
		case R.id.action_addDevice:
			if (!checkNetwork(GosDeviceListActivity.this)) {
				Toast.makeText(GosDeviceListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
			} else {
				intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.action_site:
			intent = new Intent(GosDeviceListActivity.this, GosSettiingsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice",devices);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.action_exit:
			Intent intent = new Intent(); 
			intent.setAction("com.way.util.exit_app");
			sendBroadcast(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
int   count=0;
	private void UpdateUI() {
		
		if (GosDeviceModuleBaseActivity.deviceslist.isEmpty()) {
			svListGroup.setVisibility(View.GONE);
			llNoDevice.setVisibility(View.VISIBLE);
			return;
		} else {
			llNoDevice.setVisibility(View.GONE);
			svListGroup.setVisibility(View.VISIBLE);
		}

		boundDevicesList = new ArrayList<GizWifiDevice>();
		foundDevicesList = new ArrayList<GizWifiDevice>();
		offlineDevicesList = new ArrayList<GizWifiDevice>();

		
		for (GizWifiDevice gizWifiDevice : GosDeviceModuleBaseActivity.deviceslist) {
			if (GizWifiDeviceNetStatus.GizDeviceOnline == gizWifiDevice.getNetStatus()
					|| GizWifiDeviceNetStatus.GizDeviceControlled == gizWifiDevice.getNetStatus()) {
				if (gizWifiDevice.isBind()) {
					boundDevicesList.add(gizWifiDevice);
//					gizd=gizWifiDevice;
				} else {
					foundDevicesList.add(gizWifiDevice);
				}
			} else {
				offlineDevicesList.add(gizWifiDevice);
			}
		}

		
		if (boundDevicesList.isEmpty()) {
			slvBoundDevices.setVisibility(View.GONE);
			llNoBoundDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, boundDevicesList);
			myadapter.setHandler(handler);
			slvBoundDevices.setAdapter(myadapter);
			llNoBoundDevices.setVisibility(View.GONE);
			slvBoundDevices.setVisibility(View.VISIBLE);
		}
		
//		for(int i=0;i<boundDevicesList.size();i++){
//			if(count==0){
//				gizd =boundDevicesList.get(i);
//				if(gizd.getNetStatus() == GizWifiDeviceNetStatus.GizDeviceOnline||gizd.getNetStatus()==GizWifiDeviceNetStatus.GizDeviceControlled)
//				{			
//					count++;
//					isokintent();
//				}
//				}
//		}

		if (foundDevicesList.isEmpty()) {
			slvFoundDevices.setVisibility(View.GONE);
			llNoFoundDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, foundDevicesList);
			slvFoundDevices.setAdapter(myadapter);
			llNoFoundDevices.setVisibility(View.GONE);
			slvFoundDevices.setVisibility(View.VISIBLE);
		}

		if (offlineDevicesList.isEmpty()) {
			slvOfflineDevices.setVisibility(View.GONE);
			llNoOfflineDevices.setVisibility(View.VISIBLE);
		} else {
			myadapter = new GosDeviceListAdapter(this, offlineDevicesList);
			myadapter.setHandler(handler);
			slvOfflineDevices.setAdapter(myadapter);
			llNoOfflineDevices.setVisibility(View.GONE);
			slvOfflineDevices.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgNoDevice:
		case R.id.tvNoDevice:
			if (!checkNetwork(GosDeviceListActivity.this)) {
				Toast.makeText(GosDeviceListActivity.this, R.string.network_error, Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(GosDeviceListActivity.this, GosAirlinkChooseDeviceWorkWiFiActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	private void tryUserLoginAnonymous() {
		threeSeconds = 3;
		final Timer tsTimer = new Timer();
		tsTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				threeSeconds--;
				if (threeSeconds <= 0) {
					tsTimer.cancel();
					handler.sendEmptyMessage(GETLIST);
					// TODO if (isLogout) {
					// isLogout = false;
					// } else {
					// handler.sendEmptyMessage(GETLIST);
					// }
				} else {
					if (loginStatus == 4) {
						tsTimer.cancel();
					}
					// TODO if (isLogout) {
					// tsTimer.cancel();
					// isLogout = false;
					// }
				}
			}
		}, 1000, 1000);
	}

	int location;
	boolean	flag;
	Dialog dialog;
	
	private void setDeviceInfo() {
		dialog = new AlertDialog.Builder(this).setView(new EditText(this)).create();
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

		 final GizWifiDevice device = rndevice;
	
		if(device.getListener()!=null){
		flag=false;
		}
	else{
		device.setListener(gizWifiDeviceListener);
		flag=true;
	}
		
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
				if(flag){
					device.setListener(null);
				}
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				device.setCustomInfo(etRemark.getText().toString(), etAlias.getText().toString());
				dialog.cancel();
				String loadingText = (String) getText(R.string.loadingtext);
				progressDialog.setMessage(loadingText);
				progressDialog.show();
				
			}
		});
	}
	
	protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener(){
		/** 用于修改设备信息 */
		public void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
			GosDeviceListActivity.this.didSetCustomInfo(arg0, arg1);
		}
	};
	
	protected void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
//		progressDialog.cancel();
		Message msg = new Message();
		msg.what = TOAST;
		String toastText;
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == arg0) {
			toastText = (String) getText(R.string.set_info_successful);
		} else {
			toastText = (String) getText(R.string.set_info_failed) + "\n" + arg0;
		}
		msg.obj = toastText;
		handler.sendMessage(msg);
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
       
        
       if(data!=null)
      { 	
    	   if(resultCode == 11){
            isoffline =data.getExtras().getBoolean("isoffline", false);
        	devices = data.getParcelableExtra("GizWifiDevice");
        	 Intent intent= new Intent();
        	 Bundle bundle = new Bundle();
			 bundle.putParcelable("GizWifiDevice", (GizWifiDevice) devices);
			 intent.putExtra("isoffline",isoffline);
		   	 intent.putExtras(bundle);
		   	 intent.putExtra("isresult", true);
			 setResult(1001, intent);
//			 Toast.makeText(getApplicationContext(), "result:"+resultCode, Toast.LENGTH_SHORT).show();
			 finish();
			 }
            }
//        if(resultCode ==12){
//        	isoffline =data.getBooleanExtra("isoffline", false);
//        	devices = data.getParcelableExtra("GizWifiDevice");
//        	Toast.makeText(getApplicationContext(), "gds2:"+isoffline, Toast.LENGTH_SHORT).show();
//        }
       super.onActivityResult(requestCode, resultCode, data);
    }
	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		
			try {
				if(ismain){
				 Intent intent= new Intent();
				 Bundle bundle = new Bundle();
				 bundle.putParcelable("GizWifiDevice", (GizWifiDevice) devices);
				 intent.putExtra("isoffline",isoffline);
			   	 intent.putExtras(bundle);
				 setResult(1001, intent);
				 finish();
				}
				else
					exitBy2Click();
			} catch (Exception e) {
				// TODO: handle exception
				
				exitBy2Click();
//				finish();
			}
			
//			finish();
//			}else{
				 // 调用双击退出函数
//			}
				
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出；
			String doubleClick;

		    doubleClick = (String) getText(R.string.doubleclick_exit);
			Toast.makeText(this, doubleClick, Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			logoutToClean();
			finish();
			System.exit(0);
		}
	}

	/** 注销函数 */
	private void logoutToClean() {

		// TODO isLogout = true;
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("Token", "").commit();
		GosPushManager.pushUnBindService(token);
			
		
		if (loginStatus == 1) {
			loginStatus = 0;
		} else {
			loginStatus = 4;
		}

	}

}
