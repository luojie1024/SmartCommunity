package com.way.tabui.gokit;

import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.actity.SmartOCActivity;
import com.way.tabui.actity.UpdataActivity;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.AirMesinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AirMateActivity extends GosBaseActivity {

	private GizWifiDevice device = null;
	private DatabaseAdapter dbAdapter;
	private ImageButton ib_pre, ib_ceshi, ib_next;
	private TextView tv_pro, tv_brand;
	private int min, max, brand, index;
	private String name = "Null";
	private Button bt_diybrand;
	/** 型号代码 */
	private int sendtype = 131072;// 02 xx xx

	private int OPEN = 327432;

	private int CLOSE = 262152;

	/** 空调命令 */
	private static final String KEY_Sendair = "Send_aircon";

	private int windex = 1;

	private boolean isstart = true;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_air_mate);
		setActionBar(true, true,
				getResources().getString(R.string.title_activity_air_mate)
						.toString());
		dbAdapter = new DatabaseAdapter(this);
		initDevice();
		initView();
		initData();
		initEvent();
	}

	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
	}

	private void initView() {
		ib_pre = (ImageButton) findViewById(R.id.ib_pre);
		ib_next = (ImageButton) findViewById(R.id.ib_next);
		ib_ceshi = (ImageButton) findViewById(R.id.ib_ceshi);

		tv_pro = (TextView) findViewById(R.id.tv_pro);
		tv_brand = (TextView) findViewById(R.id.tv_brand);

		bt_diybrand = (Button) findViewById(R.id.bt_diybrand);
	//	ib_pre.setEnabled(false);

	}

	private void initData() {
		Intent intent = getIntent();
		min = intent.getIntExtra("min", 0);
		max = intent.getIntExtra("max", 1000);
		name = intent.getStringExtra("name") + "空调";
		brand = min;
		index = 1;
		setProText();
	}

	// Thread myThread = new Thread(new MyThread());

	private void initEvent() {

		bt_diybrand.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setBrandInfo();
			}
		});
		ib_pre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (brand == min) {
					Toast.makeText(getApplicationContext(), "当前已经是第一个遥控码", Toast.LENGTH_SHORT).show();
					//ib_pre.setEnabled(false);
					//ib_pre.setVisibility(View.GONE);
				} else {
					brand--;
					index--;
					setProText();
					//ib_next.setEnabled(true);
					//ib_next.setVisibility(View.VISIBLE);
				}
			}
		});

		ib_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (brand == max) {
					Toast.makeText(getApplicationContext(), "当前为最后一个遥控码", Toast.LENGTH_SHORT).show();
					//ib_next.setEnabled(false);
					//ib_next.setVisibility(View.GONE);
				} else {
					brand++;
					index++;
					setProText();
				//	ib_pre.setEnabled(true);
					//ib_pre.setVisibility(View.VISIBLE);
				}
			}
		});

		ib_ceshi.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean isopen = false;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// isonclick=false;
					if (!isopen) {
						isstart = true;
						initTimer();
						Log.i("==", "Thread is start");
						isopen = true;
					}
					break;

				case MotionEvent.ACTION_UP:
					// isonclick=false;
					isstart = false;
					initTimer();
					// myThread.interrupt();
					isopen = false;
					windex = 1;
					boundAlert(AirMateActivity.this);
					break;
				}

				return false;
			}
		});

	

	}

	// boolean isonclick;
	protected void boundAlert(Context context) {
		String title, message, nbtext, pbtext;
		title = (String) getText(R.string.prompt);
		message = "设备有响应么？";
		nbtext = "否";
		pbtext = "是";
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton(nbtext,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (brand != max) {
							brand++;
							index++;
							setProText();
						} else {
							ib_next.setEnabled(false);
						}
					}
				});

		builder.setPositiveButton(pbtext,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AirMesinfo airMesinfo = new AirMesinfo(name, brand, 22,
								0, 0, 0, device.getMacAddress(), "null", 0);
						dbAdapter.addairmes(airMesinfo);
						Toast.makeText(getApplicationContext(), "添加完毕",
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.putExtra("issave", true);
						setResult(1001, intent);
						finish();
					}
				});
		builder.show();
	}

	private void setProText() {
		int sum;
		sum = max - min + 1;
		tv_pro.setText("测试按键（" + index + "/" + sum + ")");
		tv_brand.setText("当前测试遥控码:" + brand);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			// 要做的事情
			switch (msg.what) {
			case 1:
				try {
					sendJson(KEY_Sendair, sendtype + brand);
					vSimple();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				windex++;
				break;
			case 2:
				try {
					sendJson(KEY_Sendair, OPEN);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				windex++;
				break;
			case 3:
				vSimple();
				try {
					sendJson(KEY_Sendair, CLOSE);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				windex++;

				break;

			case 4:

				if (brand != max) {
					brand++;
					index++;
					setProText();
				} else {
					ib_next.setEnabled(false);
					Toast.makeText(getApplicationContext(), "已经到最后一个遥控码", Toast.LENGTH_SHORT).show();
				}
				windex = 1;
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void setBrandInfo() {
		dialog = new AlertDialog.Builder(this).setView(new EditText(this))
				.create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setContentView(R.layout.alert_diy_air_brand);

		final EditText etminbrand;
		final EditText etmaxbrand;

		etminbrand = (EditText) window.findViewById(R.id.etminbrand);
		etmaxbrand = (EditText) window.findViewById(R.id.etmaxbrand);

		LinearLayout llNo, llSure;

		llNo = (LinearLayout) window.findViewById(R.id.llNo);
		llSure = (LinearLayout) window.findViewById(R.id.llSure);

		etminbrand.setText("" + min);

		etmaxbrand.setText("" + max);

		llNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		llSure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int minbrand;
				int maxbrand;
				try {
					minbrand = Integer
							.parseInt(etminbrand.getText().toString());
					maxbrand = Integer
							.parseInt(etmaxbrand.getText().toString());
					if (minbrand <= maxbrand) {
						min = minbrand;
						max = maxbrand;
						brand = min;
						index = 1;
						setProText();
						Toast.makeText(getApplicationContext(), "修改成功",
								Toast.LENGTH_SHORT).show();
						dialog.cancel();
					} else {
						Toast.makeText(getApplicationContext(), "起始值不可超过结束值",
								Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(getApplicationContext(), "数据错误,修改失败",
							Toast.LENGTH_SHORT).show();
					dialog.cancel();
				}

			}
		});
	}

	// public class MyThread implements Runnable {
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// while (isstart) {
	// try {
	// Thread.sleep(500);// 线程暂停10秒，单位毫秒
	// Message message = new Message();
	// message.what = windex;
	// handler.sendMessage(message);// 发送消息
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// Log.i("==", "线程出错"+e.toString());
	// }
	// }
	// }
	// }
	private Thread thread;

	public void initTimer() {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (isstart) {
					Message mas = new Message();
					mas.what = windex;
					handler.sendMessage(mas);
					if (windex == 3) {
						try {
							Thread.sleep(1200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.i("==", "线程出错" + e.toString());
						}
					} else {
						try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							Log.i("==", "线程出错" + e.toString());
						}
					}
				}
			}
		});
		if (isstart) {
			thread.start();
		} else {
			thread.interrupt();
		}

	}

	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.i("==", hashMap.toString());
		// Log.i("Apptest", hashMap.toString());
	}

	private void vSimple() {
		Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(40);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
