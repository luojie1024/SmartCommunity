package com.way.tabui.actity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.gokit.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class IntelligentSocketActivity extends GosBaseActivity {

	  private static String LOG_TAG = "IntelligentSocketActivity";
	  
	    Button startBroadCast;
	    Button stopBroadCast;
	    Button bt_readvoltage,bt_close,bt_readcurrent,bt_readpower,bt_readpowercost,bt_readall;
	  //  TextView send_label;
	    TextView receive_label;
	 
	    Thread thread;
	    private int Flag = 0x01;
	    String host_ip;
	    /* 用于 udpReceiveAndTcpSend 的3个变量 */
	    Socket socket = null;
	    MulticastSocket ms = null;
	    DatagramPacket dp;
	    //拉闸
	    byte[] date_close = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE ,(byte)0x68 ,(byte)0xAA,(byte) 0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0x68 ,(byte)0x1C ,(byte)0x10 ,(byte)0x37 ,(byte)0x44 ,(byte)0x44 ,(byte)0x44 ,(byte)0x34 ,(byte)0x89 ,(byte)0x67,(byte)0x45,(byte) 0x4D,(byte) 0x33 ,(byte)0x47,(byte) 0x77,(byte) 0x3B,(byte) 0x3A ,(byte) 0x44,(byte) 0x3C,(byte)0x97 ,(byte) 0x16};
		//合闸
	    byte[] date_open ={(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE ,(byte)0x68 ,(byte)0xAA,(byte) 0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0xAA ,(byte)0x68 ,(byte)0x1C ,(byte)0x10 ,(byte)0x37 ,(byte)0x44 ,(byte)0x44 ,(byte)0x44 ,(byte)0x34 ,(byte)0x89 ,(byte)0x67,(byte)0x45,(byte) 0x4E,(byte) 0x33 ,(byte)0x47,(byte) 0x77,(byte) 0x3B,(byte) 0x3A ,(byte) 0x44,(byte) 0x3C,(byte)0x98 ,(byte) 0x16};
		//wifi连接成功判断
	    byte[] date_wifi = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte) 0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x13,(byte)0x00,(byte)0xDF,(byte)0x16};
		//抄读电压数据
	    byte[] date_voltage = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x11,(byte)0x04,(byte)0x33,(byte)0x34,(byte)0x34,(byte)0x35,(byte)0xB1,(byte)0x16};
	   //抄读用电量
	    byte[] date_powercost={(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x11,(byte)0x04,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0xAD,(byte)0x16};
	    //抄读电流
	    byte[] date_current = {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x11,(byte)0x04,(byte)0x33,(byte)0x34,(byte)0x35,(byte)0x35,(byte)0xB2,(byte)0x16};
	   //抄读功率
	    byte[] date_power =   {(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x11,(byte)0x04,(byte)0x33,(byte)0x33,(byte)0x36,(byte)0x35,(byte)0xB2,(byte)0x16};
	    //wifi连接后反馈对比信息
	    byte[] checkwifi={(byte)0xfe,(byte)0xfe,(byte)0xfe,(byte)0xfe,(byte)0x68,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x68,(byte)0x93,(byte)0x06,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0x33,(byte)0x9b,(byte)0x16};
	    //合拉闸反馈对比信息
	    byte[] checkoc={(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0xFE,(byte)0x68,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0xAA,(byte)0x68,(byte)0x9C,(byte)0x00,(byte)0x68,(byte)0x16};
	    public ActionBar actionBar;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_intelligent_socket);
	 
	        initview();
	 
	//        actionBar = getActionBar();// 初始化ActionBar
//		//	actionBar.setHomeButtonEnabled(true);
//			actionBar.setIcon(R.drawable.back_bt);
		//	actionBar.setTitle("智能插座");
//			actionBar.setDisplayHomeAsUpEnabled(true);
	        
	    //    send_label.append("\n\n");
	        receive_label.append("\n\n");
	        initlistener();
	       boolean  isopen=openWIFI();
	       while(!isopen){
	    	   isopen=openWIFI();
	       }
	      if(isopen){
	        /* 开一个线程接收tcp 连接*/
	        new tcpReceive().start();
	 
	        /* 开一个线程 接收udp多播 并 发送tcp 连接*/
	        new udpReceiveAndtcpSend().start();
	        
	        time();
	        Toast.makeText(IntelligentSocketActivity.this,"验证设备连接配置是否正常中...", Toast.LENGTH_LONG).show();
	       
//	       if (isconnet==false){
//	    	   Toast.makeText(IntelligentSocketActivity.this,"connet="+wififlag, Toast.LENGTH_SHORT).show();
//	       }else{
//	    	   Toast.makeText(IntelligentSocketActivity.this,"wififlag="+wififlag, Toast.LENGTH_SHORT).show();
//	       }
	      }
	    }
	    
	    public void initview(){
	    	startBroadCast = (Button) findViewById(R.id.start);
	    	
	        stopBroadCast = (Button) findViewById(R.id.stop);     
	        bt_close=(Button) findViewById(R.id.bt_close);
	        bt_readcurrent=(Button) findViewById(R.id.bt_readcurrent);
	        bt_readpower=(Button) findViewById(R.id.bt_readpower);
	        bt_readpowercost=(Button) findViewById(R.id.bt_readpowercost);
	        bt_readvoltage=(Button) findViewById(R.id.bt_readvoltage);
	        bt_readall=(Button) findViewById(R.id.bt_readall);
	   //     send_label = (TextView) findViewById(R.id.send_information);
	        receive_label = (TextView) findViewById(R.id.receive_information);
	       
	        startBroadCast.setEnabled(false);
	        stopBroadCast.setEnabled(false);
	        bt_close.setEnabled(false);
	        bt_readcurrent.setEnabled(false);
	        bt_readpower.setEnabled(false);
	        bt_readpowercost.setEnabled(false);
	        bt_readvoltage.setEnabled(false);
	        bt_readall.setEnabled(false);
	        
	        
	        
	    }
	    int count=1;
	    public boolean openWIFI(){// TODO: 2017/6/20 更新方法
			WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
	            //判断wifi是否开启  
	            if ((!wifiManager.isWifiEnabled())&&(count==1)) {  
	              wifiManager.setWifiEnabled(true); 
	              count=0;
	            } 
	            else{
	            WifiInfo wifiInfo = wifiManager.getConnectionInfo();       
	            int ipAddress = wifiInfo.getIpAddress(); 
	            /* 本机的ip地址*/
	            host_ip = intToIp(ipAddress); 
	            } 
	           return  wifiManager.isWifiEnabled() ;
		             
	    }
	    public void initlistener(){

	        startBroadCast.setOnClickListener(listener);
	        stopBroadCast.setOnClickListener(listener);
	        bt_close.setOnClickListener(listener);
	        bt_readvoltage.setOnClickListener(listener);
	        bt_readcurrent.setOnClickListener(listener);
	         bt_readpower.setOnClickListener(listener);
	        bt_readpowercost.setOnClickListener(listener);
	        bt_readall.setOnClickListener(listener);
	    }
	    public void connet(){
	    	createthread("date_Wifi");
//            return wififlag;
	    }
	    
	    public void createthread(String mes){
	    	thread = new udpBroadCast(mes);
            thread.start();
            thread.interrupt();
	    }
	    
	    public View.OnClickListener listener = new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	Flag=v.getId();
	        	
	        	switch (v.getId()) {
				case R.id.start:
					createthread("date_Open");
					break;
				case R.id.stop:
					//startBroadCast.setEnabled(true);
	                //stopBroadCast.setEnabled(false);
					createthread("date_Wifi");
					break;
				case R.id.bt_readvoltage:
					createthread("date_Voltage");
					break;
				case R.id.bt_close:
					createthread("date_Close");
					break;
				case R.id.bt_readcurrent:
					createthread("date_Current");
					break;
				case R.id.bt_readpower:
					createthread("date_Power");
					break;
				case R.id.bt_readpowercost:
					createthread("date_Powercost");
					break;
				case R.id.bt_readall:
					Flag=v.getId();
					bisonclick=true;
					time();
					break;
				default:
					break;
				}
	        }
	    };
	 

	    /* 发送udp多播 */
	    public  class udpBroadCast extends Thread {
	        MulticastSocket sender = null;
	        DatagramPacket dj = null;
	        InetAddress group = null;
	        byte[] data = new byte[64];	 
	     //   private DatagramSocket udpSocket;
	        public udpBroadCast(String dataString) {
	            
	            if(dataString=="date_Close")
				{
					data =date_close;
				}else if(dataString=="date_Open")
				{
					data =date_open;
					
				}else if(dataString=="date_Wifi"){
					
					data =date_wifi;
				
				}else if(dataString=="date_Voltage"){
				
					data =date_voltage;
				
				}else if(dataString=="date_Current"){
					
					data =date_current;
					
				}else if(dataString=="date_Power"){
					
					data =date_power;
					
				}else if(dataString=="date_Powercost"){
					
					data =date_powercost;
					
				}else{
					data = dataString.getBytes();
				}
	         //   receive_label.setText(""+data);
	        }
	 
	        @Override
	        public void run() {
	        	//DatagramPacket dataPacket = null;
	            try {
	            	
	                sender = new MulticastSocket(9000);
//	                udpSocket = new DatagramSocket(9000);
	                group = InetAddress.getByName("255.255.255.255");
	                 dj = new DatagramPacket(data,data.length,group,9000);
	 			
	 				sender.send(dj);
	 				//udpSocket.send(dj);
	               sender.close();
	            	
	            	
	            } catch(IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    //解析电压
	    public  String readofVoltage(byte[] b){
	        
			String   d1= Integer.toHexString((b[18] & 0xFF)-0x33);
			String   d2=Integer.toHexString((b[19] & 0xFF)-0x33);
			if (d1.length() == 1) {
                d1="0"+d1;
            }
		 if (d2.length() == 1) {
                d2="0"+d2;
            }
			String   L=d1.substring(0,1).trim();
			String   E=d1.substring(1).trim();
		    Log.v("==", "d1:"+d1+"d2:"+d2+"L:"+L+"E:"+E);
		    String result="电压："+d2+L+"."+E+"V";
			return result;
		}
	    //解析电流
     public  String readofCurrent(byte[] b){
	        
			String   c1= Integer.toHexString((b[18] & 0xFF)-0x33);
			String   c2=Integer.toHexString((b[19] & 0xFF)-0x33);
			String   c3=Integer.toHexString((b[20] & 0xFF)-0x33);
			String   L=c2.substring(0,1).trim();
			String   E=c2.substring(1).trim();
			 if (c1.length() == 1) {
	                c1="0"+c1;
	            }
			 if (c2.length() == 1) {
	                c2="0"+c2;
	            }
			 if (c3.length() == 1) {
	                c3="0"+c3;
	            }
			if(c3.equals("00"))
				c3="";
		    String result="电流："+c3+L+"."+E+c1+"A";
			return result;
		}
	    //解析功率
     public  String readofPower(byte[] b){
	        
			String   p1= Integer.toHexString((b[18] & 0xFF)-0x33);
			String   p2=Integer.toHexString((b[19] & 0xFF)-0x33);
			String   p3=Integer.toHexString((b[20] & 0xFF)-0x33);
//			String   L=p2.substring(0,1).trim();
//			String   E=p2.substring(1).trim();
			 if (p1.length() == 1) {
	                p1="0"+p1;
	            }
			 if (p2.length() == 1) {
	                p2="0"+p2;
	            }
			 if (p3.length() == 1) {
	                p3="0"+p3;
	            }
			if(p3.equals("00"))
				p3="0";
			if(p1.equals("00"))
				p1="";
		    String result="功率："+p3+"."+p2+p1+"KW";
			return result;
		}
   //解析总用电
     public  String readofPowerCost(byte[] b){
	        
			String   p1= Integer.toHexString((b[18] & 0xFF)-0x33);
			String   p2=Integer.toHexString((b[19] & 0xFF)-0x33);
			String   p3=Integer.toHexString((b[20] & 0xFF)-0x33);
			String   p4=Integer.toHexString((b[21] & 0xFF)-0x33);
			 if (p1.length() == 1) {
	                p1="0"+p1;
	            }
			 if (p2.length() == 1) {
	                p2="0"+p2;
	            }
			 if (p3.length() == 1) {
	                p3="0"+p3;
	            }
			 if (p4.length() == 1) {
	                p4="0"+p4;
	            }
			if(p4.equals("00"))
			{ p4="";
			  if(p3.equals("00"))
				 p3="";
			  if(p2.equals("00"))
					 p2="0"; }
			
		    String result="总用电量："+p4+p3+p2+"."+p1+"KWH";
		    
			return result;
		}
     //检查设备连接正常
	    boolean wififlag=false;
	    
	    public boolean wificheckcon(byte[] b){
	    	for(int i=0;i<checkwifi.length;i++){
				Log.v("==", "checkwifi"+i+"="+checkwifi[i]+"b="+b[i]);
				if (checkwifi[i]!=b[i]) {
					wififlag=false;
					//Toast.makeText(IntelligentSocketActivity.this,"connet="+wififlag, Toast.LENGTH_SHORT).show();
					break;
				}else if(checkwifi[checkwifi.length-1]==b[checkwifi.length-1]){
					wififlag=true;
				}
			}
	    	
			return wififlag;
	    }
	    //检查合开闸命令是否送达
	    public boolean checkocn(byte[] b){
	    	boolean check=false;
	    	for(int i=0;i<checkoc.length;i++){
				Log.v("==", "checkoc"+i+"="+checkoc[i]+"b="+b[i]);
				if (checkoc[i]!=b[i]) {
					check=false;
				 break;
				}else if(checkoc[checkoc.length-1]==b[checkoc.length-1]){
					check=true;
				}
			}
	    	
			return check;
	    	
	    }
	    public  String printHexString(byte[] b,int Flag) {
	        String hexs = "";
	        String hex;
	      
	        for (int i = 0; i < b.length; i++ ) {
	     
	        	hex = Integer.toHexString(b[i] & 0xFF);
	            if (hex.length() == 1) {
	                hex="0"+hex;
	            }        
	            	hexs=hexs+" "+hex;
	           
	        }
	        
	        String   p1= Integer.toHexString((b[14] & 0xFF)-0x33);
	        if (p1.length() == 1) {
                p1="0"+p1;
            }
			String   p2=Integer.toHexString((b[15] & 0xFF)-0x33);
			 if (p2.length() == 1) {
	                p2="0"+p2;
	            }
			String   p3=Integer.toHexString((b[16] & 0xFF)-0x33);
			 if (p3.length() == 1) {
	                p3="0"+p3;
	            }
			String   p4=Integer.toHexString((b[17] & 0xFF)-0x33);
			 if (p4.length() == 1) {
	                p4="0"+p4;
	            }
			 String rd =Integer.toHexString(b[12] & 0xFF);
			 if (rd.length() == 1) {
	                rd="0"+rd;
	            }

	   	 Log.v("==", "rd="+rd);
		 Log.v("==", "P1="+p1+p2+p3+p4);
		if(rd.equals("91")){
			 Log.v("==", "rd1="+rd);
			 Log.v("==", "cP1="+p1+"p2="+p2+"P3="+p3+"p4="+p4);
        if(p1.equals("00")&&p2.equals("01")&&p3.equals("01")&&p4.equals("02")){
        	hexs=readofVoltage(b);
        }else  if(p1.equals("00")&&p2.equals("01")&&p3.equals("02")&&p4.equals("02")){
        	hexs=readofCurrent(b);
        }else if(p1.equals("00")&&p2.equals("00")&&p3.equals("03")&&p4.equals("02")){
        	hexs=readofPower(b);
        }
        else if(p1.equals("00")&&p2.equals("00")&&p3.equals("00")&&p4.equals("00")){
        	hexs=readofPowerCost(b);
        }
		 }else if(rd.equals("93")){
			 if(p1.equals("00")&&p2.equals("00")&&p3.equals("00")&&p4.equals("00")){
				 if(wificheckcon(b))
					 hexs="连接正常";
			 }
		 }else if(rd.equals("9c")){
			 if(checkocn(b))
				 hexs="命令已接收";
			 
		 }
	        Log.v("==", "wififlag1="+wififlag);
	        return hexs;
	        
	    }
	    
	    public String intToIp(int i) {       
	        return (i & 0xFF ) + "." +       
	      ((i >> 8 ) & 0xFF) + "." +       
	      ((i >> 16 ) & 0xFF) + "." +       
	      ( i >> 24 & 0xFF) ;  
	   }   
//	     byte[] datarec;
	    int count_mes=0;
	    /*接收udp多播 并 发送tcp 连接*/
	    public class udpReceiveAndtcpSend extends  Thread {
	        @Override
	        public void run() {
	            byte[] data = new byte[32];
	            
	            try {
	                InetAddress groupAddress = InetAddress.getByName("255.255.255.255");
	                ms = new MulticastSocket(9000);
	                ms.joinGroup(groupAddress);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	 
	            while (true) {
	                try {
	                    dp = new DatagramPacket(data, data.length);
	                    if (ms != null)
	                       ms.receive(dp);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	 
	                if (dp.getAddress() != null) {
	                    final String quest_ip = dp.getAddress().toString();
	 
	                    openWIFI();
	                    
	                    final String messtr=printHexString(data,Flag);
		                  
	                    for(int i=0;i<data.length;i++){
	                    	data[i]=0;	
	                    }
	 
	                   // String host_ip = getLocalIPAddress();
	 
	                     // String host_ip = getLocalHostIp();
	 
	                    System.out.println("host_ip:  --------------------  " + host_ip);
	                    System.out.println("quest_ip: --------------------  " + quest_ip.substring(1));
	 
	                    if( (!host_ip.equals(""))  && host_ip.equals(quest_ip.substring(1)) ) {
	                    	 Log.v("==",host_ip+" and "+quest_ip.substring(1));
	                    	continue;
	                    }
	                    Log.v("==",host_ip+" and "+quest_ip.substring(1));
	                   // final String codeString = new String(data, 0, dp.getLength());
	 
//	                    datarec=data;
	                  
	                   // String messtr2=
	                    receive_label.post(new Runnable() {
	                        @Override
	                        public void run() {
	                            
	                        	if(Flag!=R.id.bt_readall)
	                        	{
	                        	count_mes=0;
	                        	receive_label.setText("");
	                       //     receive_label.append("收到来自: \n" + quest_ip.substring(1) + "\n" +"的udp信息\n");
	                            receive_label.append( messtr + "\n");
	                            }else{
	                            	if(count_mes==0)
	                            	{
	                            		receive_label.setText("");
	                            	//	receive_label.append("收到来自: \n" + quest_ip.substring(1) + "\n" +"的udp信息\n");
	                            	//	receive_label.append("信息内容: "+"\n");
	                            	}
	                            	count_mes++;
	                            	receive_label.append( messtr + "\n");
	                            	if(count_mes==4)
	                            		count_mes=0;
	                            	
	                            }
	                            
	                        }
	                    });
	                    
	                    try {
	                        final String target_ip = dp.getAddress().toString().substring(1);
//	                        send_label.post(new Runnable() {
//	                            @Override
//	                            public void run() {
//	                            	send_label.setText("");
//	                                send_label.append("发送tcp请求到: \n" + target_ip + "\n");
//	                            }
//	                        });
	                        socket = new Socket(target_ip,9000);
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    } finally {
	 
	                        try {
	                            if (socket != null)
	                                socket.close();
	                        } catch (IOException e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	            }
	        }
	    }
	 
	 
	 
	    /* 接收tcp连接 */
	    public class tcpReceive extends  Thread {
	        ServerSocket serverSocket;
	        Socket socket;
	        BufferedReader in;
	        String source_address;
	 
	        @Override
	        public void run() {
	            while(true) {
	                serverSocket = null;
	                socket = null;
	                in = null;
	                try {
	                    Log.i("Tcp Receive"," new ServerSocket ++++++++++");
	                    serverSocket = new ServerSocket(9000);
	 
	                    socket = serverSocket.accept();
	                    Log.i("Tcp Receive"," get socket ++++++++++++++++");
	 
	                    if(socket != null) {
	                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                        StringBuilder sb = new StringBuilder();
	                        sb.append(socket.getInetAddress().getHostAddress());
	 
	                        String line = null;
	                        while ((line = in.readLine()) != null ) {
	                            sb.append(line);
	                        }
	 
	                        source_address = sb.toString().trim();
	                        receive_label.post(new Runnable() {
	                            @Override
	                            public void run() {
	                                receive_label.append("收到来自: "+"\n" +source_address+"\n"+"的tcp请求\n\n");
	                            }
	                        });
	                    }
	                } catch (IOException e1) {
	                    e1.printStackTrace();
	                } finally {
	                    try {
	                        if (in != null)
	                            in.close();
	                        if (socket != null)
	                            socket.close();
	                        if (serverSocket != null)
	                            serverSocket.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }
	        }
	    }
	 
	    public String getLocalHostIp() {
	        String ipaddress = "";
	        try {
	            Enumeration<NetworkInterface> en = NetworkInterface
	                    .getNetworkInterfaces();
	            // 遍历所用的网络接口
	            while (en.hasMoreElements()) {
	                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
	                Enumeration<InetAddress> inet = nif.getInetAddresses();
	                // 遍历每一个接口绑定的所有ip
	                while (inet.hasMoreElements()) {
	                    InetAddress ip = inet.nextElement();
	                    if (!ip.isLoopbackAddress()
	                            && !ip.isLoopbackAddress()) {
	                        return ip.getHostAddress();
	                    }
	                }
	            }
	        }
	        catch(SocketException e)
	        {
	                Log.e("feige", "获取本地ip地址失败");
	                e.printStackTrace();
	        }
	        return ipaddress;
	    }
	 
	    private String getLocalIPAddress() {
	        try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface
	                    .getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf
	                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()) {
	                        return inetAddress.getHostAddress().toString();
	                    }
	                }
	            }
	        } catch (SocketException ex) {
//	           Log.e(LOG_TAG, ex.toString());
	        }
	        return null;
	    }
	 
	    // 按下返回键时，关闭 多播socket ms
	    @Override
	    public void onBackPressed() {
	        ms.close(); 
	        super.onBackPressed();
	    }
	    
	   int  mTimer;
	   private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1){
				if(Flag!=R.id.bt_readall){
				if(mTimer==3)
					connet();
				if(mTimer==5){
					bisonclick=false;
					if(wififlag){
						Toast.makeText(IntelligentSocketActivity.this,"设备连接正常!", Toast.LENGTH_SHORT).show();
						startBroadCast.setEnabled(true);
				        stopBroadCast.setEnabled(true);
				        bt_close.setEnabled(true);
				        bt_readcurrent.setEnabled(true);
				        bt_readpower.setEnabled(true);
				        bt_readpowercost.setEnabled(true);
				        bt_readvoltage.setEnabled(true); 
				        bt_readall.setEnabled(true);
					}else{
						Intent intent=new Intent(IntelligentSocketActivity.this,EsptouchDemoActivity.class);
						startActivity(intent);
						finish();
						//Toast.makeText(IntelligentSocketActivity.this,"connet="+wififlag, Toast.LENGTH_SHORT).show();
					}
				}
				}else{
					if(mTimer==1)
						createthread("date_Powercost");
					if(mTimer==2)
						 createthread("date_Voltage");
					if(mTimer==3)
						createthread("date_Current");
					if(mTimer==4)
						createthread("date_Power");
					if(mTimer==5)
						bisonclick=false;
					
				}
				}
			};
		};
		
 
   private boolean bisonclick=true;
	public void time(){
		thread=new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				mTimer=0;
				while(bisonclick){
					Message mas= new Message();
					mas.what=1;
					mHandler.sendMessage(mas);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mTimer++;
					Log.v("==", "2time"+mTimer);
				}
			}
		});
		if (bisonclick) {
			thread.start();
		} else {
			thread.interrupt();
		}
	}
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.setting, menu);
	        
	        return true;
	    }
	 
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        switch (item.getItemId()) {
			case R.id.action_settings:
				Intent intent=new Intent(IntelligentSocketActivity.this,EsptouchDemoActivity.class);
				startActivity(intent);
				finish();
				break;
			case R.id.action_back:
				finish();
			
			default:
				break;
			} 
	        
	        return super.onOptionsItemSelected(item);
	    }
}
