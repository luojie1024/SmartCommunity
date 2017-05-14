package com.way.tabui.actity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.way.tabui.gokit.R;

public class TcpActivity extends TabActivity implements
OnClickListener {

private EditText edtIP;
private EditText edtPort;
EditText edtSend;
private EditText edtReceiver;

private Button btnConn;
private Button btnSend;

private CheckBox checkBoxTimer;

private String tag = "MainActivity";

InputStream in;
PrintWriter printWriter = null;
BufferedReader reader;

Socket mSocket = null;
public boolean isConnected = false;

private MyHandler myHandler;

Thread receiverThread;

CheckBoxListener listener;

private class MyReceiverRunnable implements Runnable {

public void run() {

	while (true) {

		Log.i(tag, "---->>client receive....");
		if (isConnected) {
			if (mSocket != null && mSocket.isConnected()) {

				String result = readFromInputStream(in);

				try {

					// String str = "";
					//
					// while ((str = reader.readLine()) != null) {
					// Log.i(tag, "---->> read data:" + str);
					// result += str;
					// }
					if (!result.equals("")) {

						Message msg = new Message();
						msg.what = 1;
						Bundle data = new Bundle();
						data.putString("msg", result);
						msg.setData(data);
						myHandler.sendMessage(msg);
					}

				} catch (Exception e) {
					Log.e(tag, "--->>read failure!" + e.toString());
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

private class MyHandler extends Handler {
@Override
public void handleMessage(Message msg) {
	super.handleMessage(msg);

	receiverData(msg.what);
	if (msg.what == 1) {
		String result = msg.getData().get("msg").toString();
		edtReceiver.append(result);
	}
}
}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_tcp);
init();
}


private void init() {

edtIP = (EditText) this.findViewById(R.id.id_edt_inputIP);
edtPort = (EditText) this.findViewById(R.id.id_edt_inputport);
edtSend = (EditText) this.findViewById(R.id.id_edt_sendArea);
edtReceiver = (EditText) findViewById(R.id.id_edt_jieshou);

checkBoxTimer = (CheckBox) this.findViewById(R.id.id_checkBox_timer);
listener = new CheckBoxListener(this);
checkBoxTimer.setOnCheckedChangeListener(listener);

btnSend = (Button) findViewById(R.id.id_btn_send);
btnSend.setOnClickListener(this);
btnConn = (Button) findViewById(R.id.id_btn_connClose);
btnConn.setOnClickListener(this);

myHandler = new MyHandler();
}

/******************************************************************************/
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

/******************************************************************************/

public void onClick(View v) {

switch (v.getId()) {

// 启动2个工作线程:发送、接收。
case R.id.id_btn_connClose:
	connectThread();
	break;

case R.id.id_btn_send:
	sendData();
	break;
}
}

/**
* 当连接到服务器时,可以触发接收事件.
*/
private void receiverData(int flag) {

if (flag == 2) {
	// mTask = new ReceiverTask();
	receiverThread = new Thread(new MyReceiverRunnable());
	receiverThread.start();

	Log.i(tag, "--->>socket 连接成功!");
	btnConn.setText("断开");

	isConnected = true;
	// mTask.execute(null);
}

}

/**
* 发送数据线程.
*/
private void sendData() {

// sendThread.start();
try {
	String context = edtSend.getText().toString();

	if (printWriter == null || context == null) {

		if (printWriter == null) {
			showInfo("连接失败!");
			return;
		}
		if (context == null) {
			showInfo("连接失败!");
			return;
		}
	}

	printWriter.print(context);
	printWriter.flush();
	Log.i(tag, "--->> client send data!");
} catch (Exception e) {
	Log.e(tag, "--->> send failure!" + e.toString());

}
}

/**
* 启动连接线程.
*/
private void connectThread() {
if (!isConnected) {
	new Thread(new Runnable() {

		public void run() {
			Looper.prepare();
			Log.i(tag, "---->> connect/close server!");

			connectServer(edtIP.getText().toString(), edtPort.getText()
					.toString());
		}
	}).start();
} else {
	try {
		if (mSocket != null) {
			mSocket.close();
			mSocket = null;
			Log.i(tag, "--->>取消server.");
			// receiverThread.interrupt();
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	btnConn.setText("连接");
	isConnected = false;
}
}

// 连接服务器.(网络调试助手的服务器端编码方式:gb2312)
private void connectServer(String ip, String port) {
try {
	Log.e(tag, "--->>start connect  server !" + ip + "," + port);

	mSocket = new Socket(ip, Integer.parseInt(port));
	Log.e(tag, "--->>end connect  server!");

	OutputStream outputStream = mSocket.getOutputStream();

	printWriter = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(outputStream,
					Charset.forName("gb2312"))));
	listener.setOutStream(printWriter);
	// reader = new BufferedReader(new InputStreamReader(
	// mSocket.getInputStream()));

	in = mSocket.getInputStream();
	myHandler.sendEmptyMessage(2);

	showInfo("连接成功!");
} catch (Exception e) {
	isConnected = false;
	showInfo("连接失败！");
	Log.e(tag, "exception:" + e.toString());
}

}

private void showInfo(String msg) {
Toast.makeText(TcpActivity.this, msg, Toast.LENGTH_SHORT).show();

}
}


