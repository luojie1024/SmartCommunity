package com.way.tabui.gokit;

import java.util.ArrayList;

import com.way.adapter.AllmesAdapter;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.actity.MainActivity.MyReceiver;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.Alertinfo;
import com.way.util.Allmesinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class AllMessageActivity extends GosBaseActivity {

	private  ListView listview_all_mes;
	private ArrayList<Allmesinfo> mList = new ArrayList<Allmesinfo>();
	 private MyReceiver receiver=null;
	 private	boolean gasstua=false;
	 private	boolean smokestua=false;
	 private	boolean gatestua=false;
	 private	boolean bodystua=false;
	 private	boolean lastgasstua=false;
	 private	boolean lastsmokestua=false;
	 private	boolean lastgatestua=false;
	 private	boolean lastbodystua=false;
	 private	boolean isdisconnect=false;
	private	String temperature;
	private	String humidity;
	private AllmesAdapter adapter ;
	private DatabaseAdapter dbAdapter;
	private String MacAddress;
	ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_message);
		//广播接收器
		  receiver=new MyReceiver();
		  IntentFilter filter=new IntentFilter();
		  filter.addAction("com.way.tabui.actity.GizService");
		  AllMessageActivity.this.registerReceiver(receiver, filter);

		  Intent intent = getIntent();
		   temperature=intent.getStringExtra("temperature");
		   humidity=intent.getStringExtra("humidity");	   
		  gasstua=intent.getBooleanExtra("gasstua", false);				
		  gatestua=intent.getBooleanExtra("gatestua", false);			
		  bodystua=intent.getBooleanExtra("bodystua", false);
	      smokestua=intent.getBooleanExtra("smokestua", false);
	      
	      MacAddress =intent.getStringExtra("MacAddress");
	      
	      dbAdapter = new DatabaseAdapter(this);
	      
	      progressDialog = new ProgressDialog(this);
	      
	      setProgressDialog("获取数据中...");
		  initview();
		  progressDialog.show();
		  initdata();
		  initlist();
		  progressDialog.cancel();
	}
	
	public void setProgressDialog(String text) {
		progressDialog.setMessage(text);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		  AllMessageActivity.this.unregisterReceiver(receiver);
		super.onDestroy();
	}
    private void initview(){
    	
    	listview_all_mes=(ListView) findViewById(R.id.listview_all_mes);
    } 
	
    
    private void initdata(){
    	mList=null;
    	mList = new ArrayList<Allmesinfo>();
    	//视频
    	Allmesinfo mesvedio =new Allmesinfo(R.drawable.ic_video, "视频", "监控摄像头", " ");
    	mList.add(mesvedio);
    	mesvedio=null;
    	//温度
    	Allmesinfo mestem = new Allmesinfo(R.drawable.ic_temity, "温度", "温度采集器", ""+temperature+"℃");
    	mList.add(mestem);
    	mestem = null;
    	//湿度
    	Allmesinfo meshum = new Allmesinfo(R.drawable.ic_humity, "湿度", "湿度采集器", ""+humidity+"%");
    	mList.add(meshum);
    	meshum = null;
    	//门磁
    	Allmesinfo mesgate=null;
    	if(gatestua){
    	 mesgate = new Allmesinfo(R.drawable.ic_gate, "门碰", "门磁", "状态：开");
    	}else{
    	 mesgate = new Allmesinfo(R.drawable.ic_gate, "门碰", "门磁", "状态：关");
    	}
    	mList.add(mesgate);
    	mesgate = null;
    	//烟感
    	Allmesinfo messmoke=null;
    	if(smokestua){
    		messmoke = new Allmesinfo(R.drawable.ic_smoke, "烟感", "烟雾采集器", "警报中...");
    	}else{
    		messmoke = new Allmesinfo(R.drawable.ic_smoke, "烟感", "烟雾采集器", "正常");
    	}
    	mList.add(messmoke);
    	messmoke= null;
    	
    	//燃气
    	Allmesinfo messgas=null;
    	if(gasstua){
    		messgas = new Allmesinfo(R.drawable.ic_gas, "燃气", "燃气采集器", "警报中...");
    	}else{
    		messgas = new Allmesinfo(R.drawable.ic_gas, "燃气", "燃气采集器", "正常");
    	}
    	mList.add(messgas);
    	messgas = null;
    	
    	
    	//人体红外
    	Allmesinfo messbody=null;
    	Alertinfo alertinfo = dbAdapter.findbybindgizname(MacAddress, "人体红外");
    	
    	if(bodystua){
    		if(alertinfo!=null){
    			messbody = new Allmesinfo(R.drawable.ic_body, "红外状态", "红外", "有人经过  \n最近活动时间：\n"+alertinfo.getTime());
			} else {
				// TODO: handle exception
				messbody = new Allmesinfo(R.drawable.ic_body, "红外状态", "红外", "有人经过  \n最近活动时间：\n记录时间被删除...");
			}
   		}else{
    		if(alertinfo!=null)
    		messbody = new Allmesinfo(R.drawable.ic_body, "红外状态", "红外", "暂无人经过    \n最近活动时间：\n"+alertinfo.getTime());
    		else
    			messbody = new Allmesinfo(R.drawable.ic_body, "红外状态", "红外", "暂无人经过    \n最近活动时间：\n暂无记录");
    	}
    	mList.add(messbody);
    	messbody = null;
    }
    
    private void initlist(){
    	adapter = new AllmesAdapter(AllMessageActivity.this, mList);
    	listview_all_mes.setAdapter(adapter);
    }
    
    public class MyReceiver extends BroadcastReceiver {
	     @Override
	     public void onReceive(Context context, Intent intent) {
	    	  temperature=intent.getStringExtra("temperature");
			  humidity=intent.getStringExtra("humidity");
			   
				gasstua=intent.getBooleanExtra("gasstua", false);

								
				gatestua=intent.getBooleanExtra("gatestua", false);
			
		
				bodystua=intent.getBooleanExtra("bodystua", false);
					
				smokestua=intent.getBooleanExtra("smokestua", false);	
				
//				isdisconnect =intent.getBooleanExtra("isdisconnect", false);
//				if(isdisconnect){
//					finish();
//				}
				 setProgressDialog("刷新中...");
				 progressDialog.show();
				initdata();
				initlist();
				 progressDialog.cancel();
	     }
	 }

	
}
