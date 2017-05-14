package com.way.tabui.actity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.property.activity.AnnouncementListActivity;
import com.property.activity.BaoxiushenqingActivity;
import com.property.activity.JiaofeifenleiActivity;
import com.showmo.demo.login.LoginActivity;
import com.showmo.demo.maincotent.MainContentActivity;
import com.showmo.demo.util.spUtil;
import com.way.main.DraggableGridView;
import com.way.main.OnRearrangeListener;
import com.way.tabui.actity.MainActivity.MyReceiver;
import com.way.tabui.cevicemodule.GosDeviceListActivity;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.controlmodule.GosDeviceControlActivity;
import com.way.tabui.gokit.AirConMesActivity;
import com.way.tabui.gokit.AlertmesActivity;
import com.way.tabui.gokit.AllMessageActivity;
import com.way.tabui.gokit.PromailActivity;
import com.way.tabui.gokit.R;
import com.way.tabui.gokit.SmartAirConditionActivity;
import com.way.tabui.gokit.SmartCurtainActivity;
import com.way.tabui.settingsmodule.GosAboutActivity;
import com.way.tabui.settingsmodule.GosSettiingsActivity;
import com.way.util.DataCache;
import com.way.util.ScreenInfo;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * 每个Tab中的fragment
 * 
 * @author way
 * 
 */
public class SampleFragment extends Fragment {

	boolean isoffline = true;
	boolean lastisoffline = true;
	private String lasttem = "";
	private String lasthum = "";
	private String lastfirstmac = "";
	private String firstmac = "";
	private static final int GasAlert = 1;
	private static final int GasSafe = 2;
	private static final int DoorOper = 3;
	private static final int DoorClose = 4;
	private static final int SmokeAlert = 5;
	private static final int SmokeSafe = 6;
	private static final int BodyPass = 7;
	private static final int BodyNot = 8;
	private static final int TeHuChe = 9;
	

	protected boolean gasstua = false;
	protected boolean smokestua = false;
	protected boolean gatestua = false;
	protected boolean bodystua = false;
	protected boolean lastgasstua = false;
	protected boolean lastsmokestua = false;
	protected boolean lastgatestua = false;
	protected boolean lastbodystua = false;
	protected boolean isdisconnect = false;
	private String temperature;
	private String humidity;

	private MyReceiver receiver = null;

	private static final String ARG_TEXT = "text";
	private static final String ARG_PAGER = "pager";
	static Random random = new Random();
	DraggableGridView dgv;
	ArrayList<String> poem = new ArrayList<String>();
	Context context;
	// Ronrey
	LayoutParams lp;
	FrameLayout fl;
	private int count = 0; // 双击计数器
	private long firstTimeNum, secondTimeNum; // 记录两次触摸点击的时间
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合
	// private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点
	private int currentItem = 0; // 当前图片的索引号
	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	public static SampleFragment newInstance(String text, int pager) {
		SampleFragment f = new SampleFragment();
		Bundle args = new Bundle();
		args.putString(ARG_TEXT, text);
		args.putInt(ARG_PAGER, pager);
		f.setArguments(args);
		return f;
	}

	TextView title;
	String titleValue;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View v = inflater.inflate(R.layout.fragment_mywy, container, false);
		titleValue = getArguments().getString(ARG_TEXT);
		((TextView) v.findViewById(R.id.mywy_title)).setText(titleValue);
		title = (TextView) v.findViewById(R.id.mywy_title);
		//
		dgv = ((DraggableGridView) v.findViewById(R.id.vgv));
		fl = (FrameLayout) v.findViewById(R.id.fl);
		setImageView(fl);
		initViewPager(v);
		setListeners();
		title.setText(getArguments().getString(ARG_TEXT));
		int pager = getArguments().getInt(ARG_PAGER);
		if (pager == MainActivity.PAGE_FILE_SYSTEM)// 如果是文件系统的Fragment
			title.setBackgroundColor(Color.parseColor("#ff3995e3"));// 蓝色标题栏
		else
			title.setBackgroundColor(Color.parseColor("#ffde4125"));// 红色标题栏

		//
		String[][] data = null;
		if (titleValue.equals("智能家居")) {
			data = DataCache.networkStateDataCache.get("smDataArray");
		} else if (titleValue.equals("我的娱乐")) {
			data = DataCache.networkStateDataCache.get("mpDataArray");
		} else if (titleValue.equals("我的物业")) {
			data = DataCache.networkStateDataCache.get("mwyDataArray");
		} else if (titleValue.equals("私下预定")) {
			data = DataCache.networkStateDataCache.get("pbDataArray");
		} else if (titleValue.equals("生活服务")) {
			data = DataCache.networkStateDataCache.get("lsDataArray");
		} else if (titleValue.equals("系统设置")) {
			data = DataCache.networkStateDataCache.get("ssDataArray");
		} else {
			data = new String[][] { { "1", "数据查询失败" } };
		}
		// Log.v("==", ""+data.length);

		for (int i = 0; i < data.length; i++) {
			addModule(data[i][1]);
			// Log.v("==", ""+data[i][1]);
		}

		return v;
	}

	// 设置ImageView
	private void setImageView(View iv) {
		lp = iv.getLayoutParams();
		lp.height = ScreenInfo.getScreenHeight(context) / 5;
		iv.setLayoutParams(lp);
	}

	// 初始化viewpager
	private void initViewPager(View v) {
		imageResId = new int[] { R.drawable.ic_sm_lbimage,
				R.drawable.ic_sm_lbimage2, R.drawable.ic_sm_lbimage4 };
		imageViews = new ArrayList<ImageView>();
		// 初始化图片资源
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(v.findViewById(R.id.v_dot0));
		dots.add(v.findViewById(R.id.v_dot1));
		dots.add(v.findViewById(R.id.v_dot2));

		viewPager = (ViewPager) v.findViewById(R.id.vp);
		viewPager.setAdapter(new com.way.adapter.MyAdapter(imageResId,
				imageViews));// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());

	}

	ImageView imagas, imasmoke, imabody, imagate, imatehu;

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	ScrollTask scrollTask = new ScrollTask();

	@Override
	public void onStart() {
		System.out.println("onStart");
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每4秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(scrollTask, 1, 4,
				TimeUnit.SECONDS);

		super.onStart();
	}

	@Override
	public void onPause() {
		System.out.println("onPause");
		scheduledExecutorService.shutdown();

		try {
			// stask.interrupt();
			stopreceiver();
		} catch (Exception e) {
			// TODO: handle exception
		}

		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		try {
			spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
			Context.MODE_PRIVATE);
			boolean issafe=spf.getBoolean("issafe", true);
		if (!getofisoffline()) {
			if (TextUtils.isEmpty(((MainActivity) getActivity()).device
					.getAlias())) {
//				if(issafe){
				title.setText(getArguments().getString(ARG_TEXT)
						+ "-"
						+ ((MainActivity) getActivity()).device
								.getProductName());
//				}else{
//					title.setText(getArguments().getString(ARG_TEXT)
//							+ "-"
//							+ ((MainActivity) getActivity()).device
//									.getProductName()+"-撤防-");
//				}
			} else {
//				if(issafe){
				title.setText(getArguments().getString(ARG_TEXT) + "-"
						+ ((MainActivity) getActivity()).device.getAlias());
//				}else{
//					title.setText(getArguments().getString(ARG_TEXT) + "-"
//							+ ((MainActivity) getActivity()).device.getAlias()+"-撤防-");
//				}
			}
		} else
			title.setText(getArguments().getString(ARG_TEXT) + "-" + "网关离线中");
		} catch (Exception e) {
			// TODO: handle exception
			title.setText(getArguments().getString(ARG_TEXT)+ "-" + "网关离线中");
		}
     //如果是智能家居模块则启动广播接收，用来更新相关的UI
		if (titleValue.equals("智能家居")) {
			try {
				// stask.start();
				starreceiver();
//				spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
//						Context.MODE_PRIVATE);
//				//boolean issafe=spf.getBoolean("issafe", true);
//				if(spf.getBoolean("issafe", true)){
//					imabody.setVisibility(View.VISIBLE);
//					imagas.setVisibility(View.VISIBLE);
//					imagate.setVisibility(View.VISIBLE);
//					imasmoke.setVisibility(View.VISIBLE);
//				}else{
//					//dgv.removeView(imabody);
//					//poem.remove();
//					imabody.setVisibility(View.GONE);
//					imagas.setVisibility(View.GONE);
//					imagate.setVisibility(View.GONE);
//					imasmoke.setVisibility(View.GONE);
//				}
			} catch (Exception e) {
				// TODO: handle exception
				
			}
		}
		super.onResume();
	}

	@Override
	public void onStop() {
		System.out.println("onStop");
		try {
			stopreceiver();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
	}

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	  //设立广播监听者，以用来更新相关UI界面
	private void starreceiver() {
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.way.tabui.actity.GizService");
		getActivity().registerReceiver(receiver, filter);
	}

	private void stopreceiver() {
		getActivity().unregisterReceiver(receiver);
	}

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//获取传过来的数据
			temperature = intent.getStringExtra("temperature");
			humidity = intent.getStringExtra("humidity");
			gasstua = intent.getBooleanExtra("gasstua", false);
			gatestua = intent.getBooleanExtra("gatestua", false);
			bodystua = intent.getBooleanExtra("bodystua", false);
			smokestua = intent.getBooleanExtra("smokestua", false);
			//数据比较，若发生变化则进行相关UI的更新
			if ((lasttem != temperature) || (lasthum != humidity)) {
				Message mas = new Message();
				lasttem = temperature;
				lasthum = humidity;
				mas.what = TeHuChe;
				mHandler.sendMessage(mas);
			}
			if (gasstua) {
				Message mas = new Message();
				mas.what = GasAlert;
				mHandler.sendMessage(mas);
			} else {
				Message mas = new Message();
				mas.what = GasSafe;
				mHandler.sendMessage(mas);
			}
			
			if (gatestua) {
				Message mas = new Message();
				mas.what = DoorOper;
				mHandler.sendMessage(mas);
			} else {
				Message mas = new Message();
				mas.what = DoorClose;
				mHandler.sendMessage(mas);
			}

			
			if (bodystua) {
				Message mas = new Message();
				mas.what = BodyPass;
				mHandler.sendMessage(mas);
			} else {
				Message mas = new Message();
				mas.what = BodyNot;
				mHandler.sendMessage(mas);
			}

			
			if (smokestua) {
				Message mas = new Message();
				mas.what = SmokeAlert;
				mHandler.sendMessage(mas);
			} else {
				Message mas = new Message();
				mas.what = SmokeSafe;
				mHandler.sendMessage(mas);
			}
		   }
		
	}

	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
					Context.MODE_PRIVATE);
			boolean issafe=spf.getBoolean("issafe", true);
			switch (msg.what) {
			case GasAlert:
				if(issafe){
				try {
					imagas.setImageResource(R.drawable.ic_gas_a);
				} catch (Exception e) {
					// TODO: handle exception
				}
				}
				break;
			case GasSafe:
				
				try {
					imagas.setImageResource(R.drawable.ic_gas_b);
				} catch (Exception e) {
					// TODO: handle exception
				}
		
				break;

			case DoorOper:
				if(issafe){
				try {
					imagate.setImageResource(R.drawable.ic_gate_a);
				} catch (Exception e) {
					// TODO: handle exception
				}
				}

				break;
			case DoorClose:
				
				try {
					imagate.setImageResource(R.drawable.ic_gate_b);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;

			case SmokeAlert:
				if(issafe){
				try {
					imasmoke.setImageResource(R.drawable.ic_smoke_a);
				} catch (Exception e) {
					// TODO: handle exception
				}
				}
				

				break;
			case SmokeSafe:
				
				try {
					imasmoke.setImageResource(R.drawable.ic_smoke_b);
				} catch (Exception e) {
					// TODO: handle exception
				}
				

				break;

			case BodyPass:
				if(issafe){
				try {
					imabody.setImageResource(R.drawable.ic_body_a);
				} catch (Exception e) {
					// TODO: handle exception
				}
				}

				break;
			case BodyNot:
				
				try {
					imabody.setImageResource(R.drawable.ic_body_b);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				break;
			case TeHuChe:
				try {
					imatehu.setImageBitmap(getThumb("温度:" + getoftem() + "\n"
							+ "湿度:" + getofhum()));
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;

			// case NetCh:
			// title.setText(getArguments().getString(ARG_TEXT)+"-"+"网关离线");
			// break;
			// case NetCh1:
			// try{
			//
			// title.setText(getArguments().getString(ARG_TEXT)+"-"+((MainActivity)
			// getActivity()).device.getProductName());
			// }catch(Exception e){
			//
			//
			// }
			// break;
			case 0x123:
				Toast.makeText(context, "爱小屏登录成功！", Toast.LENGTH_LONG).show();
				break;
			case 0x124:
				Toast.makeText(context, "登录失败！", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};

	//
	

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				System.out.println("currentItem: " + currentItem
						+ getArguments().getString(ARG_TEXT));
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}
	}

	// 智能灯光单击事件
	private void smart_light() {
		Intent intent = new Intent(context, SmartLightActivity.class);
		startActivity(intent);

	}

	// 爱小屏视频

	private void lovescreen() {
		((MainActivity) getActivity()).showLoadingDialog();
		try {
			((MainActivity) getActivity()).xmSystem.xmLogin("13135367953",
					"chen162858", new OnXmListener<XmAccount>() {
						@Override
						public void onSuc(XmAccount outinfo) {
							((MainActivity) getActivity()).closeLoadingDialog();
							mHandler.sendEmptyMessage(0x123);
							((MainActivity) getActivity()).sp
									.setUsername("13135367953");
							((MainActivity) getActivity()).loginSuc(outinfo);
					
						}

						@Override
						public void onErr(XmErrInfo info) {
							((MainActivity) getActivity()).closeLoadingDialog();
							mHandler.sendEmptyMessage(0x124);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			((MainActivity) getActivity()).closeLoadingDialog();
			mHandler.sendEmptyMessage(0x124);
		} finally {

		}
	}

	// 开关
	private void smart_oc() {
		if (!getofisoffline()) {
			Intent intent = new Intent(context, SmartOCActivity.class);
			intent.putExtra("ismain", true);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice",
					(GizWifiDevice) ((MainActivity) getActivity()).device);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1000);
		} else {
			Toast.makeText(context, "现在为离线状态，无法进入此功能界面..", Toast.LENGTH_SHORT)
					.show();
		}
	}
	//智能空调
	private void smart_condition() {
		if (!getofisoffline()) {
			Intent intent = new Intent(context, AirConMesActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice",
					(GizWifiDevice) ((MainActivity) getActivity()).device);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1000);
		} else {
			Toast.makeText(context, "现在为离线状态，无法进入此功能界面..", Toast.LENGTH_SHORT)
					.show();
		}
	}
	// 查看记录
	private void alert_mes() {

		spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
				Context.MODE_PRIVATE);
		if (!getofisoffline()) {
			Intent intent = new Intent(context, AlertmesActivity.class);
			intent.putExtra("MacAddress",
					((MainActivity) getActivity()).device.getMacAddress());
			// Bundle bundle = new Bundle();
			// bundle.putParcelable("GizWifiDevice", (GizWifiDevice)
			// ((MainActivity)getActivity()).device);
			// intent.putExtras(bundle);
			startActivityForResult(intent, 1000);
		} else {
			Toast.makeText(context, "现在为离线状态，无法进入此功能界面..", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// 可视对讲
	private void see_wat() {
		Intent intent = new Intent(context, PreviewActivity.class);
		startActivity(intent);
	}

	// 物业信箱
	private void pro_mail() {
		Intent intent = new Intent(context, PromailActivity.class);
		startActivity(intent);
	}

	public SharedPreferences spf;

	private void giz_witsetd() {
		spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
				Context.MODE_PRIVATE);
		// spf.edit().putString("msgobjx", "istrue").commit();
		Intent intent = new Intent(context, GosDeviceListActivity.class);
		intent.putExtra("ismain", true);
		Bundle bundle = new Bundle();
		bundle.putParcelable("GizWifiDevice",
				(GizWifiDevice) ((MainActivity) getActivity()).device);
		intent.putExtras(bundle);
		intent.putExtra("isoffline",getofisoffline());
		startActivityForResult(intent, 1000);
	}

	private void intelligent_socket() {
		Intent intent = new Intent(context, IntelligentSocketActivity.class);
		startActivity(intent);

	}

	// 计量
	private void all_mes() {
		if (!getofisoffline()) {
			Intent intent = new Intent(context, AllMessageActivity.class);
			intent.putExtra("gasstua", getofgas());
			intent.putExtra("gatestua", getofgate());
			intent.putExtra("bodystua", getofbody());
			intent.putExtra("smokestua", getofsmoke());
			intent.putExtra("temperature", getoftem());
			intent.putExtra("humidity", getofhum());
			intent.putExtra("MacAddress",
					((MainActivity) getActivity()).device.getMacAddress());
			startActivity(intent);
		} else {
			Toast.makeText(context, "现在为离线状态，无法进入此功能界面..", Toast.LENGTH_SHORT)
					.show();
		}
	}
    //检查更新按钮
	private void check_version() {
		Intent intent = new Intent(context, GosAboutActivity.class);
		intent.putExtra("title", "检查更新");
		Bundle bundle = new Bundle();
		bundle.putParcelable("GizWifiDevice",
				(GizWifiDevice) ((MainActivity) getActivity()).device);
		intent.putExtras(bundle);
		intent.putExtra("isoffline", getofisoffline());
		startActivity(intent);
	}

	//设置
		private void set_Setting() {
		//	if (!getofisoffline()) {
			Intent intent = new Intent(context, GosSettiingsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putParcelable("GizWifiDevice",
					(GizWifiDevice) ((MainActivity) getActivity()).device);
			intent.putExtras(bundle);
			intent.putExtra("isoffline", getofisoffline());
			startActivity(intent);
			//}else
		}
		//窗帘
		private void smart_curtain(){
			spf = getActivity().getSharedPreferences(GosConstant.SPF_Name,
					Context.MODE_PRIVATE);
			if (!getofisoffline()) {
				Intent intent = new Intent(context,SmartCurtainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putParcelable("GizWifiDevice",
						(GizWifiDevice) ((MainActivity) getActivity()).device);
				intent.putExtras(bundle);
				startActivityForResult(intent, 1000);
			} else {
				Toast.makeText(context, "现在为离线状态，无法进入此功能界面..", Toast.LENGTH_SHORT)
						.show();
			}
		}
		
		
		/**
		 * 作者：Jacky 时间：2017/1/14 16:35
		 */
		private void pro_inform() {
//			Intent intent = new Intent(context, InformActivity.class);
			Intent intent = new Intent(context, AnnouncementListActivity.class);
			startActivity(intent);

		}

		private void pro_baoxiu() {
			Intent intent = new Intent(context, BaoxiushenqingActivity.class);
			startActivity(intent);

		}
		
		private void pro_jiaofei() {
			Intent intent = new Intent(context, JiaofeifenleiActivity.class);
			startActivity(intent);

		}
	
	private void setListeners() {
		dgv.setOnRearrangeListener(new OnRearrangeListener() {
			public void onRearrange(int oldIndex, int newIndex) {
				String word = poem.remove(oldIndex);
				if (oldIndex < newIndex)
					poem.add(newIndex, word);
				else
					poem.add(newIndex, word);
			}
		});

		dgv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int location, long arg3) {

				if (poem.get(location) == "灯光/插座/开关")
					smart_oc();

				else if (poem.get(location) == "可视对讲")
					see_wat();
				else if (poem.get(location) == "网关")
					giz_witsetd();
				else if (poem.get(location) == "计量插座")
					all_mes();
				else if (poem.get(location) == "摄像头")
					lovescreen();
				else if (poem.get(location) == "物业信箱")
					pro_mail();
				else if (poem.get(location) == "警报记录")
					alert_mes();
				else if (poem.get(location) == "退出APP") {
					Intent intent = new Intent();
					intent.setAction("com.way.util.exit_app");
					getActivity().sendBroadcast(intent);
				}
				else if (poem.get(location) == "检查更新")
					check_version();
				else if(poem.get(location) == "智能空调/电视")
					smart_condition();
				else if(poem.get(location)=="设置")
					set_Setting();
				else if (poem.get(location) == "物业公告")
					pro_inform();
				else if (poem.get(location) == "物业报修")
					pro_baoxiu();

				else if (poem.get(location) == "缴费查询")
					pro_jiaofei();
				else if(poem.get(location) == "智能窗帘")
					smart_curtain();
				else {
					Toast.makeText(context, "敬请期待", Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	public void getScheduledExecutorService() {
		scheduledExecutorService.shutdown();
	}
	
	// 添加模块
	public void addModule(String modeuleName) {
		ImageView view = new ImageView(context);
		view.setImageBitmap(getThumb(modeuleName));
		if (modeuleName == "温湿度检测") {
			imatehu = view;
			imatehu.setTag("tehu");
			imatehu.setImageBitmap(getThumb("温度:" + getoftem() + "\n" + "湿度:"
					+ getofhum()));

		}
		if (modeuleName == "燃气报警") {
			imagas = view;
			imagas.setTag("gas");
			imagas.setImageResource(R.drawable.ic_gas_b);
			if (getofgas())
				imagas.setImageResource(R.drawable.ic_gas_a);

		}
		if (modeuleName == "门磁") {
			imagate = view;
			imagate.setTag("gate");
			imagate.setImageResource(R.drawable.ic_gate_b);
			if (getofgate())
				imagate.setImageResource(R.drawable.ic_gate_a);

		}
		if (modeuleName == "人体移动") {
			imabody = view;
			imabody.setTag("body");
			imabody.setImageResource(R.drawable.ic_body_b);
			if (getofbody())
				imabody.setImageResource(R.drawable.ic_body_a);

		}
		if (modeuleName == "烟雾报警") {
			imasmoke = view;
			imasmoke.setTag("smoke");
			imasmoke.setImageResource(R.drawable.ic_smoke_b);
			if (getofsmoke())
				imasmoke.setImageResource(R.drawable.ic_smoke_a);

		}
		dgv.addView(view);
	
		poem.add(modeuleName);
	}

	public boolean getofgas() {
		gasstua = ((MainActivity) getActivity()).Getgas();
		return gasstua;
	}

	public boolean getofgate() {
		gatestua = ((MainActivity) getActivity()).Getgate();
		return gatestua;
	}

	public boolean getofsmoke() {
		smokestua = ((MainActivity) getActivity()).Getsmoke();
		return smokestua;
	}

	public boolean getofbody() {
		bodystua = ((MainActivity) getActivity()).Getbody();
		return bodystua;
	}

	public String getoftem() {
		temperature = ((MainActivity) getActivity()).Gettem();
		return temperature;
	}

	public String getofhum() {
		humidity = ((MainActivity) getActivity()).Gethum();
		return humidity;
	}

	public String getoffirstmac() {
		firstmac = ((MainActivity) getActivity()).Getfirstmac();
		return firstmac;
	}

	public boolean getofisoffline() {
		isoffline = ((MainActivity) getActivity()).Getisoffline();
		return isoffline;
	}

	// random.nextInt(128), random.nextInt(128),random.nextInt(128)

	private Bitmap getThumb(String s) {
		Bitmap bmp = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(0, 191, 255));
		paint.setTextSize(20);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		canvas.drawRect(new Rect(0, 0, 150, 150), paint);
		paint.setColor(Color.WHITE);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(s, 75, 75, paint);
		return bmp;
	}
}
