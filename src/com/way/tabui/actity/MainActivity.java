package com.way.tabui.actity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.IntToString;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.showmo.demo.maincotent.MainContentActivity;
import com.showmo.demo.util.spUtil;
import com.way.tabui.GosApplication;
import com.way.tabui.cevicemodule.GosDeviceListActivity;
import com.way.tabui.controlmodule.GosControlModuleBaseActivity;
import com.way.tabui.gokit.R;
import com.way.tabui.pushmodule.GosPushManager;
import com.way.util.DataCache;
import com.way.util.ExitAppReceiver;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * 智慧社区
 * 注意继承自FragmentActivity，才会有getSupportFragmentManager()函数
 * @author way
 * 
 */

public class MainActivity extends GosControlModuleBaseActivity implements
		OnPageChangeListener {
	// HashMap<String, Object> deviceStatu;
	/** The isUpDateUi */
	protected static boolean isUpDateUi = true;
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
	
	private boolean gasstua=false;
	private boolean smokestua=false;
	private boolean gatestua=false;
	private boolean bodystua=false;
	private  boolean isdisconnect=false;
	private String temperature;
	private String humidity;
	private static final int NUM_PAGES = 2;// 总页数，2个Fragment
	public static final int PAGE_PERSONAL = 0;// 第一个界面ID
	public static final int PAGE_FILE_SYSTEM = 1;// 第二个界面ID
	private static final int ROTATE_ANIM_DURATION = 300;// 左下角切换动画的时间

	private int mCurPage = 0;// 当前页
	private ViewPager mViewPager;// 父容器由一个ViewPager实现
	private PagerAdapter mPagerAdapter;// ViewPager适配器
	private ImageButton mSwitchImageButton;// 左下角切换Paper按钮
	private ImageView mAnimView;// 动画View
	private Animation mRotateRightAnim;// 向右旋转动画
	private Animation mRotateLeftAnim;// 向左旋转动画
	
//	private ImageView imagas,imasmoke,imabody,imagate,imatehu;
	//Ronrey Add
	private String firstmac;
	private boolean isoffline;
	private boolean isworked;
	private TextView te ;
	private StartThread thread;
	private StopThread stopthread;
	 private MyReceiver receiver=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Intent intent =getIntent();
		isoffline =intent.getBooleanExtra("isoffline", false);
		isworked =intent.getBooleanExtra("isworked", false);
		
		if(isoffline){
			firstmac="NULL";
			stopthread=new StopThread();
			stopthread.start();
			stopthread.interrupt();			
			}
		else{
			initDevice();
			if(!isworked){
			thread=new StartThread();
			thread.start();
			thread.interrupt();
			}
			}
		
		initBroadcostData();
		  
		init();
		setContentView(R.layout.activity_main);
		initView();		
		initAnim();
		loadData();
		
		String waitingText = (String) getText(R.string.waiting_device_ready);
		super.onCreate(savedInstanceState);
		
	}
	
	
	private class StartThread extends Thread{
		@Override
		public void run() {
			startsevice();
		}
	}
	
	private class StopThread extends Thread{
		@Override
		public void run() {

			stopsevice();
		}
	}
	void startsevice(){
		Intent reIntent = new Intent(this, GizService.class);
		Bundle bundle = new Bundle();
		bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
		reIntent.putExtras(bundle);
		startService(reIntent);
	}
	
	void stopsevice(){
		Intent reIntent = new Intent(this, GizService.class);
		stopService(reIntent);
	}
	IXmSystem xmSystem;
	spUtil sp;
	 private void init(){
	        xmSystem = XmSystem.getInstance();
	        xmSystem.xmInit(MainActivity.this, "CN", new OnXmSimpleListener() {
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
	
	 public void loginSuc(XmAccount info){
	        Intent intent = new Intent(MainActivity.this,MainContentActivity.class);
	        Bundle bundle = new Bundle();
	        bundle.putSerializable("user", info);
	        intent.putExtras(bundle);
	        startActivity(intent);
	        //finish();
	    }
	 

	 private void initReceiver(){
		//数据接收者
	      receiver=new MyReceiver();
		  IntentFilter filter=new IntentFilter();
		  filter.addAction("com.way.tabui.actity.GizService");
		  filter.addAction("com.way.tabui.actity.GizServiceTOAST");
		  MainActivity.this.registerReceiver(receiver,filter);
	 }
	 private void initBroadcostData(){
		 //发送一次广播访问服务获取初始数据
		  Intent intent2=new Intent();
		  intent2.setAction("com.way.tabui.actity.MainActivity");
		  sendBroadcast(intent2);
	 }
	 
	public GizWifiDevice device;
	private HashMap<String, Object> deviceStatu;
	private String title;
	
	public void initDevice() {
		
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		deviceStatu = new HashMap<String, Object>();
		if(!isoffline)
		firstmac =device.getMacAddress();
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
      if(resultCode == 1001)
      {
    	   isoffline= data.getBooleanExtra("isoffline", false);
    	   if(!isoffline){//看是否在线登录
    		  
        	device = (GizWifiDevice) data.getParcelableExtra("GizWifiDevice");
    		deviceStatu = new HashMap<String, Object>();
    		if(device!=null){//设备是否为空
    		if(!isWorked("com.way.tabui.actity.GizService")){//服务是否在工作
//    			Toast.makeText(getApplicationContext(), "服务异常中断，重启了服务...", Toast.LENGTH_SHORT).show();
    			thread=new StartThread();
    			thread.start();
    			thread.interrupt();
    			initBroadcostData();
    		}
    		else{
    		  if(!firstmac.equals(device.getMacAddress())){//是否和上次在线设备一样。不一样则会重新启动服务
    			stopthread=new StopThread();
   				stopthread.start();
   				stopthread.interrupt();
   				gasstua=false;
   				smokestua=false;
   				gatestua=false;
   			 	bodystua=false;
   			 	temperature="null";
   			 	humidity="null";
    			firstmac =device.getMacAddress();
    			thread=new StartThread();
    			thread.start();
    			thread.interrupt();
    			initBroadcostData();
    		}
//    		  else if(data.getBooleanExtra("isresult", false)){
//    			stopthread=new StopThread();
//   				stopthread.start();
//   				stopthread.interrupt();
//   				thread=new StartThread();
//    			thread.start();
//    			thread.interrupt();
//    		}		
        	}
    	   }
    	   }else{
    		   stopthread=new StopThread();
  				stopthread.start();
  				stopthread.interrupt();
  				device=null;
   			 gasstua=false;
   			 smokestua=false;
   			 gatestua=false;
   			 bodystua=false;
   			 temperature="null";
   			 humidity="null";
    		   
    	   }
        }
    	  
      if(resultCode == 1002){
    	  handler.sendEmptyMessage(DISCONNECT);
      }
      
    }
	
	public boolean Getgas(){
		return gasstua;
	}
	public boolean Getsmoke(){
		return smokestua;
	}
	public boolean Getgate(){
		return gatestua;
	}
	public boolean Getbody(){
		return bodystua;
	}
	public boolean Getisoffline(){
		return isoffline;
	}
	
	public String Getfirstmac(){
		return firstmac;
	}
	public String Gettem(){
		return temperature;
	}
	public String Gethum(){
		return humidity;
	}
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			
//			Toast.makeText(getApplicationContext(),""+msg , Toast.LENGTH_LONG).show();
			switch (msg.what) {

			case UPDATE_UI:
				isUpDateUi = true;
				//if((Boolean) deviceStatu.get(KEY_Gas)==true){	
				break;
//			case RESP:
//				String data = msg.obj.toString();
//				try {
//					showDataInUI(data);
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				break;
//			case LOG:
//				StringBuilder sb = new StringBuilder();
//				JSONObject jsonObject;
//				int logText = 1;
//				try {
//					jsonObject = new JSONObject(msg.obj.toString());
//					for (int i = 0; i < jsonObject.length(); i++) {
//						if (jsonObject.getBoolean(jsonObject.names().getString(i)) != false) {
//							sb.append(jsonObject.names().getString(i) + " " + logText + "\r\n");
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//				if (sb.length() != 0) {
//					Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
//				}
//				break;
			case TOAST:
				String info = msg.obj + "";
				Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
				break;
//			case HARDWARE:
//			//	showHardwareInfo((String) msg.obj);
//				break;

			case DISCONNECT:
				String disconnectText = (String) getText(R.string.disconnect);
				Toast.makeText(getApplicationContext(), disconnectText, Toast.LENGTH_SHORT).show();	
				Intent intent = new Intent(MainActivity.this,GosDeviceListActivity.class);
//				spf.edit().putString("msgobjx", "notrue").commit();
				
				startActivity(intent);
				finish();
				break;
				}
			

				}
		};
		
		
	 public class MyReceiver extends BroadcastReceiver {
		     @Override
		     public void onReceive(Context context, Intent intent) {
		    	 String action = intent.getAction();
			      Message msg = new Message();
			      if(action.equals("com.way.tabui.actity.GizService")){
			    	temperature=intent.getStringExtra("temperature");
				  	humidity=intent.getStringExtra("humidity");
				  	if(spf.getBoolean("issafe", true)){
					gasstua=intent.getBooleanExtra("gasstua", false);					
					gatestua=intent.getBooleanExtra("gatestua", false);
					bodystua=intent.getBooleanExtra("bodystua", false);	
					smokestua=intent.getBooleanExtra("smokestua", false);
				  	}else{
				  		gasstua=false;
				  		smokestua=false;
				  		gatestua=false;
				  		bodystua=false;
				  	}
				  	}	
			      if(action.equals("com.way.tabui.actity.GizServiceTOAST")){
			    		
			    		String mes=intent.getStringExtra("Toastdata");
//			    		if(mes.equals("连接设备失败"))
//			    		{
			    		msg.what = TOAST;
			    		msg.obj=mes;
						handler.sendMessage(msg);
//						}
			    	}
		    	 
		     }
		 }
     ProgressDialog progressDialog;		
		 
	boolean isback =false;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		initReceiver();
		initBroadcostData();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		MainActivity.this.unregisterReceiver(receiver);
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		MainActivity.this.unregisterReceiver(receiver);
//		MainActivity.this.unregisterReceiver(exitReceiver);
		super.onDestroy();
	}
	
	//装载数据
	private void loadData(){
		//智能家居
		String[][] smDataArray = new String[][]{{"1","摄像头"},{"2","手机开门"},{"3","网关"},{"4","灯光/插座/开关"},{"5","智能窗帘"},{"6","智能空调/电视"},{"7","燃气报警"},{"8","温湿度检测"},{"9","人体移动"},{"10","烟雾报警"},{"11","门磁"},{"12","警报记录"},{"13","计量插座"}};
		DataCache.networkStateDataCache.put("smDataArray", smDataArray);
		//我的娱乐
		String[][] mpDataArray = new String[][]{{"1","虚拟家庭"},{"2","超级秀场"},{"3","附近的人"}};
		DataCache.networkStateDataCache.put("mpDataArray", mpDataArray);
		//我的物业
		String[][] mwyDataArray = new String[][]{{"1","物业公告"},{"2","物业报修"},{"3","物业信箱"},{"4","物业代办"},{"5","缴费查询"},{"6","跑腿代办"}};
		DataCache.networkStateDataCache.put("mwyDataArray", mwyDataArray);
		//私下预定
		String[][] pbDataArray = new String[][]{{"1","健康护理"},{"2","美容美体"},{"3","足疗按摩"},{"4","家政服务"},{"5","美工设计"},{"6","用车跑腿"}};
		DataCache.networkStateDataCache.put("pbDataArray", pbDataArray);
		//生活服务
		String[][] lsDataArray = new String[][]{{"1","二手物品"},{"2","房屋交易"},{"3","招聘求职"},{"4","菜市场"},{"5","超市"},{"6","家政服务"},{"7","外卖"}};
		DataCache.networkStateDataCache.put("lsDataArray", lsDataArray);
		//系统设置
		String[][] ssDataArray = new String[][]{{"1","我的账号"},{"2","注册"},{"3","登陆"},{"4","检查更新"},{"5","设置"},{"6","我的音乐"},{"7","游戏"},{"8","退出APP"}};
//		,{"5","机智云设备设置"}
		DataCache.networkStateDataCache.put("ssDataArray", ssDataArray);
		
	}
	/**
	 * 初始化Views
	 */
	private void initView() {
		
		sp = new spUtil(this);
		mAnimView = (ImageView) findViewById(R.id.anim_icon);
		mSwitchImageButton = (ImageButton) findViewById(R.id.switch_btn);
			
		mViewPager = (ViewPager) findViewById(R.id.vp_pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mCurPage = PAGE_FILE_SYSTEM;
		mViewPager.setCurrentItem(mCurPage);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setPageTransformer(true, new DepthPageTransformer());
//		mViewPager.getChildAt(mViewPager.getCurrentItem());
	}

	/**
	 * 初始化动画
	 */
	private void initAnim() {
		mRotateRightAnim = new RotateAnimation(0.0f, 180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateRightAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateRightAnim.setFillAfter(true);
		mRotateLeftAnim = new RotateAnimation(180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateLeftAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateLeftAnim.setFillAfter(true);
		
		mRotateRightAnim.setAnimationListener(new AnimationListener() {

			
			public void onAnimationStart(Animation animation) {
			}

			
			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				mAnimView.clearAnimation();
				mAnimView.setVisibility(View.GONE);
				mSwitchImageButton.setVisibility(View.VISIBLE);
				mSwitchImageButton
						.setImageResource(R.drawable.ic_viewpager_switch_feedlist);
			}
		});
		mRotateLeftAnim.setAnimationListener(new AnimationListener() {

		
			public void onAnimationStart(Animation animation) {
				
			}

		
			public void onAnimationRepeat(Animation animation) {
			}

			
			public void onAnimationEnd(Animation animation) {
				mAnimView.clearAnimation();
				mAnimView.setVisibility(View.GONE);
				mSwitchImageButton.setVisibility(View.VISIBLE);
				mSwitchImageButton
						.setImageResource(R.drawable.ic_viewpager_switch_fsystem);
			}
		});
	}
	 ProgressDialog dialog;
	    public void showLoadingDialog(){
	        dialog = new ProgressDialog(this);
	        dialog.setMessage("请稍后...");
	        dialog.show();
	    }
	     public void closeLoadingDialog(){
	        dialog.dismiss();
	    }

	/**
	 * 点击左下角切换按钮的事件处理
	 * 需要事先在布局中声明 android:onClick="switchPage"
	 * @param view
	 */
	public void switchPage(View view) {
		if (mCurPage == PAGE_FILE_SYSTEM) {
			mViewPager.setCurrentItem(PAGE_PERSONAL);
		} else if (mCurPage == PAGE_PERSONAL) {
			mViewPager.setCurrentItem(PAGE_FILE_SYSTEM);
		}
	}

	/**
	 * 开始动画
	 * @param pager
	 * 当前页
	 */
	private void startAmin(int pager) {
		if (pager == PAGE_FILE_SYSTEM) {
			mSwitchImageButton.setVisibility(View.INVISIBLE);
			mAnimView.setVisibility(View.VISIBLE);
			mAnimView.startAnimation(mRotateLeftAnim);
		} else if (pager == PAGE_PERSONAL) {
			mSwitchImageButton.setVisibility(View.INVISIBLE);
			mAnimView.setVisibility(View.VISIBLE);
			mAnimView.startAnimation(mRotateRightAnim);
		}
		mCurPage = pager;
	}

	/**
	 * ViewPager的适配器
	 * @author way
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		/**
		 * 这里可以将Fragment缓存一下，减少加载次数，提高用户体验，我未作处理
		 */
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case PAGE_PERSONAL:
			MainPersonalFragment mainper =	new MainPersonalFragment();
				return mainper;
			case PAGE_FILE_SYSTEM:
				MainFileSystemFragment mainFileSys= new MainFileSystemFragment();
				return mainFileSys;
			default:
				return null;
			}
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 ((ViewPager) container).removeView(mViewPager.getChildAt(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//		     MyFragment f = (MyFragment) super.instantiateItem(container, position);
//		     String title = mList.get(position);
//		     f.setTitle(title);
			Log.e("test", "instantiateItem");
		     return super.instantiateItem(container, position);
		}
	}

	
	public void onPageScrollStateChanged(int arg0) {
	}

	
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	
	public void onPageSelected(int arg0) {
		startAmin(arg0);//手势滑动ViewPager时，也要播放一下动画
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backBy2Click(); 
			return true;
		}
//		if(isExit)
		return super.onKeyDown(keyCode, event);
//		else
			
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void backBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出；
			String doubleClick;
//			doubleClick = (String) getText(R.string.doubleclick_exit);//
			Toast.makeText(this, "再次点击将返回桌面", Toast.LENGTH_SHORT).show();
			
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			try {
				spf.edit().putString("msgobj", device.getMacAddress()).commit();
			} catch (Exception e) {
				// TODO: handle exception
				spf.edit().putString("msgobj", "").commit();
			}
		      	Intent home = new Intent(Intent.ACTION_MAIN);
		        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        home.addCategory(Intent.CATEGORY_HOME);
		        startActivity(home);
			
//			finish();
//			System.exit(0); 
		}
	}
	//String softssid, uid, token;
//	public static int loginStatus;
	
	
}
