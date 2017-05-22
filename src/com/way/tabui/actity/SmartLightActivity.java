package com.way.tabui.actity;




import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.way.adapter.SmartLightAdapter;
import com.way.main.MyClickListener;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.gokit.R;
import com.way.util.LightInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class SmartLightActivity extends GosBaseActivity implements OnClickListener {

//	private String mUrl;
	private ArrayList<LightInfo> mList=new ArrayList<LightInfo>();
	private EditText text_port,text_url;
	private Button btn_con;
	private ListView smart_light_listview;
	private TextView ceshi;
	private InputStream inputStream = null;
	private BufferedReader bufferedReader = null;
	private StringBuffer msb = new StringBuffer();
	private Socket mSocket = null;
	public boolean isConnected = false;
	private MyHandler myHandler;
	private Thread receiverThread;
	private SmartLightAdapter adapter;
	private PrintWriter printWriter = null;
//	private HashMap<String,Object> map=new HashMap<String, Object>();
	String jsonString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smart_light);
		initView();
	}
	private void initView(){
		text_port=(EditText) findViewById(R.id.text_port);
		text_url=(EditText) findViewById(R.id.text_url);
		btn_con=(Button) findViewById(R.id.btn_con);
		smart_light_listview=(ListView) findViewById(R.id.smart_light_listview);
		ceshi=(TextView) findViewById(R.id.ceshi);
		myHandler = new MyHandler();
		btn_con.setOnClickListener(this);
	}

    public void onClick(View v){
	switch (v.getId()) {
	case R.id.btn_con:
		connectThread();
		break;
	default:
		break;
	}	

		
	}
	private class MyReceiverRunnable implements Runnable {
		public void run() {
			while (true) {
				if (isConnected) {
					if (mSocket != null && mSocket.isConnected()) {
						String result = readFromInputStream(inputStream);
						try {

							if (!result.equals("")) {
								Message msg = new Message();
								msg.what = 1;
								Bundle data = new Bundle();
								data.putString("msg", result);
								msg.setData(data);
								myHandler.sendMessage(msg);
							}

						} catch (Exception e) {
						}
					}
				}
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
		}
	public String readFromInputStream(InputStream in) {
		int count = 0;
		byte[] inDatas = null;
		try {
			while (count == 0) {
				count = in.available();
			}
			inDatas = new byte[count];
			in.read(inDatas);
			return new String(inDatas, "gb2312");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		}
	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			receiverData(msg.what);
			if (msg.what == 1) {
				String result = msg.getData().get("msg").toString();
				mList = GetJson(result);
				adapter=new SmartLightAdapter(SmartLightActivity.this, mList);
				smart_light_listview.setAdapter(adapter);
				adapter.setOnClickListener(new MyClickListener() {
					public void onTogButton(BaseAdapter adapter, View view, int position) {
						// TODO Auto-generated method stub
//						if(((ToggleButton) view).isChecked()){
						mList.get(position).setLight_state(((ToggleButton) view).isChecked());
						   getdata(position);
						   sendData();
						Toast.makeText(SmartLightActivity.this, ""+((ToggleButton) view).isChecked(), Toast.LENGTH_SHORT).show();
					
					}
				});
			}
		}
		}
	private void getdata(int position){
//		JSONArray jsonArray = jsonObject.optJSONArray("info")
//		{"info": [ { "id": "1","name": "客厅","state": true }]}
		jsonString=new String();
		jsonString ="{" +"\"info\":[{\"id\":\""+mList.get(position).getLight_id()+"\",\"name\":\""+
				mList.get(position).getLight_name()+"\",\"state\":"+mList.get(position).isLight_state()+
				"}]}";
		ceshi.setText(jsonString);
//		for(int i=0;i<mList.size();i++){
//		map.put("id", mList.get(i).getLight_id());
//		map.put("name", mList.get(i).getLight_name());
//		map.put("", value)
			
//		}
		
	}
	
	private void receiverData(int flag) {

		if (flag == 2) {
			// mTask = new ReceiverTask();
			receiverThread = new Thread(new MyReceiverRunnable());
			receiverThread.start();
			btn_con.setText("断开");
			isConnected = true;
			// mTask.execute(null);
		}

		}
	private void connectThread() {
		if (!isConnected) {
			new Thread(new Runnable() {
				public void run() {
					Looper.prepare();
					connectServer(text_url.getText().toString(), text_port.getText()
							.toString());
					Log.i("==", "text_url:"+text_url.getText().toString()+"port"+text_port.getText().toString());
				}
			}).start();
		} else {
			try {
				if (mSocket != null) {
					mSocket.close();
					mSocket = null;
					// receiverThread.interrupt();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			btn_con.setText("连接");
			isConnected = false;
		}
		}
	private void connectServer(String ip, String port) {
		try {

			mSocket = new Socket(ip, Integer.parseInt(port));
			OutputStream outputStream = mSocket.getOutputStream();
			printWriter = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(outputStream,
							Charset.forName("gb2312"))));
			bufferedReader = new BufferedReader(new InputStreamReader(
			 mSocket.getInputStream()));
			inputStream = mSocket.getInputStream();
			showInfo("连接成功!");
			myHandler.sendEmptyMessage(2);
			
		} catch (Exception e) {
			isConnected = false;
			showInfo("连接失败！");
			
		}
		}

	private void sendData() {

		// sendThread.start();
		try {

			if (printWriter == null || jsonString == null) {

				if (printWriter == null) {
					showInfo("连接失败!");
					return;
				}
				if (jsonString == null) {
					showInfo("连接失败!");
					return;
				}
			}

			printWriter.print(jsonString);
			printWriter.flush();
//			Log.i(tag, "--->> client send data!");
		} catch (Exception e) {
//			Log.e(tag, "--->> send failure!" + e.toString());
			 e.printStackTrace();

		}
		}
	private void showInfo(String msg) {
		Toast.makeText(SmartLightActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
		}
//	class MyAsy extends AsyncTask<String, Void, String> {
//		@Override
//		protected String doInBackground(String... arg0) {
//			// TODO Auto-generated method stub
//			return GetHttp(mUrl);
//		}
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			mList = GetJson(result);
//			smart_light_listview.setAdapter(new SmartLightAdapter(SmartLightActivity.this, mList));
//
//		}
	
		public ArrayList<LightInfo> GetJson(String json) {
			ArrayList<LightInfo> list = new ArrayList<LightInfo>();
			try {
				JSONObject jsonObject = new JSONObject(json);
				JSONArray jsonArray = jsonObject.getJSONArray("info");
				Log.v("==", "length:" + jsonArray.length());
				for (int i = 0; i < jsonArray.length(); i++) {
					LightInfo lightInfo =new LightInfo();
					
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					lightInfo.setLight_id(jsonObject2.getInt("id"));
					lightInfo.setLight_name(jsonObject2.getString("name"));
					lightInfo.setLight_state(jsonObject2.getBoolean("state"));
					list.add(lightInfo);
//					ceshi.setText(""+list.size());
//					ceshi.setText(ceshi.getText()+""+jsonObject2.getString("state"));
				}
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return list;
		}

	}


