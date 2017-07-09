package com.way.tabui.gokit;

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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.AirMesinfo;
import com.way.util.AirControlUtil;
import com.way.util.ConvertUtil;
import com.way.util.ToastUtil;

import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;


public class AirMateActivity extends GosBaseActivity {

          private GizWifiDevice device = null;
          private DatabaseAdapter dbAdapter;
          private ImageButton ib_pre, ib_ceshi, ib_next;
          private TextView tv_pro, tv_brand;
          private EditText ed_macaddress;
          private int min, max, brand, index;
          private String name = "Null";
          private Button bt_diybrand;
          /**
           * 型号代码
           */
          private int sendtype = 131072;// 02 xx xx

          private int OPEN = 327432;

          private int CLOSE = 262152;

          private byte[] MATE_OPEN={(byte)0x04,(byte)0xFF,(byte)0x08,(byte)0x08,(byte)0x00};
          private byte[] MATE_CLOSE={(byte)0x04,(byte)0x00,(byte)0x08,(byte)0x08,(byte)0x00};
          private byte[] SET_TYPE ={(byte)0x02,(byte)0x00,(byte)0x00,(byte)0x08,(byte)0x00};
          private byte[] CONTROL_HEAD;
          /**
           * 空调命令
           */
          private static final String KEY_Sendair = "kuozhan";

          private int windex = 1;

          private boolean isstart = true;
          private Dialog dialog;

          private String device_id = "";

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

                    ed_macaddress = (EditText) findViewById(R.id.et_macaddress);
                    //	ib_pre.setEnabled(false);

          }

          private void initData() {
                    Intent intent = getIntent();
                    min = intent.getIntExtra("min", 0);
                    max = intent.getIntExtra("max", 1000);
                    name = intent.getStringExtra("name") + "空调";
                    //品牌号从最小号开始
                    brand = min;
                    index = 1;
                    setProText();
          }

          // Thread myThread = new Thread(new MyThread());

          private void initEvent() {

                    bt_diybrand.setOnClickListener(new OnClickListener() {

                              @Override
                              public void onClick(View v) {
                                        setBrandInfo();
                              }
                    });
                    ib_pre.setOnClickListener(new OnClickListener() {

                              @Override
                              public void onClick(View v) {
                                        if (brand == min) {
                                                  Toast.makeText(getApplicationContext(), "当前已经是第一个遥控码", Toast.LENGTH_SHORT).show();
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
                    /**
                     * description:选定设备触摸事件
                     * auther:joahluo
                     * updata:2017/6/24 20:55:
                     * version:1.0
                     */
                    ib_ceshi.setOnClickListener(new OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                        if (ed_macaddress.getText().length() != 8) {
                                                  ToastUtil.ToastShow(AirMateActivity.this, "请输入正确的设备ID");
                                        }
                              }
                    });

                    /**FIXME
                     * description:选定设备触摸事件，长按按钮，连续发送
                     * auther:joahluo
                     * updata:2017/6/24 20:57
                     * version:1.0
                     */
                    ib_ceshi.setOnTouchListener(new OnTouchListener() {

                              @Override
                              public boolean onTouch(View v, MotionEvent event) {
                                        //当设备ID输入真确是，才允许进行设备设置控制
                                        device_id=ed_macaddress.getText().toString().toUpperCase();
                                        if (device_id.length() == 8) {
                                                  //获得数据头
                                                  CONTROL_HEAD= AirControlUtil.getAirControlHead(device_id);
                                                  boolean isopen = false;
                                                  switch (event.getAction()) {
                                                            case MotionEvent.ACTION_DOWN:
                                                                      if (!isopen) {
                                                                                isstart = true;
                                                                                //获得设备ID并转化为大写
                                                                                device_id = ed_macaddress.getText().toString().toUpperCase();
                                                                                initTimer();
                                                                                Log.i("==", "Thread is start");
                                                                                isopen = true;
                                                                      }
                                                                      break;

                                                            case MotionEvent.ACTION_UP:
                                                                      // isonclick=false;
                                                                      isstart = false;
                                                                      //获得设备ID并转化为大写
                                                                      device_id = ed_macaddress.getText().toString().toUpperCase();
                                                                      initTimer();
                                                                      // myThread.interrupt();
                                                                      isopen = false;
                                                                      windex = 1;
                                                                      boundAlert(AirMateActivity.this);
                                                                      break;
                                                  }
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
                    //没有响应
                    builder.setNegativeButton(nbtext,
                         new DialogInterface.OnClickListener() {

                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                             if (brand != max) {
                                                       brand++;
                                                       index++;
                                                       setProText();
                                             } else {
                                                       ib_next.setEnabled(false);
                                             }
                                   }
                         });
                    //有响应
                    builder.setPositiveButton(pbtext,
                         new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                             AirMesinfo airMesinfo = new AirMesinfo(name, brand, 22,
                                                  0, 0, 0, device.getMacAddress(), "null", 0, device_id);
                                             //增加空调信息
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

          /**
           * FIXME
           * description:发送控制命令
           * auther：joahluo
           * time：2017/6/25 9:56
           */
          Handler handler = new Handler() {
                    public void handleMessage(Message msg) {
                              // 要做的事情
                              switch (msg.what) {
                                        case 1:
                                                  try {
                                                            //机型信息
                                                            sendJson(KEY_Sendair, AirControlUtil.getAirControlDate(CONTROL_HEAD,brand));
                                                            vSimple();
                                                  } catch (JSONException e) {
                                                            e.printStackTrace();
                                                  }

                                                  windex++;
                                                  break;
                                        case 2:
                                                  try {
                                                            // TODO: 2017/7/7  开启空调异或校验
                                                            MATE_OPEN[4]= (byte) (MATE_OPEN[0]^MATE_OPEN[1]^MATE_OPEN[2]^MATE_OPEN[3]);
                                                            sendJson(KEY_Sendair, ConvertUtil.byteMerger(CONTROL_HEAD,MATE_OPEN));
                                                  } catch (JSONException e) {
                                                            e.printStackTrace();
                                                  }
                                                  windex++;
                                                  break;
                                        case 3:
                                                  vSimple();
                                                  try {
                                                            // TODO: 2017/7/7  关闭空调异或校验
                                                            MATE_CLOSE[4]= (byte) (MATE_CLOSE[0]^MATE_CLOSE[1]^MATE_CLOSE[2]^MATE_CLOSE[3]);
                                                            sendJson(KEY_Sendair,ConvertUtil.byteMerger(CONTROL_HEAD,MATE_CLOSE));
                                                  } catch (JSONException e) {
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

          private Thread thread;

          public void initTimer() {

                    thread = new Thread(new Runnable() {

                              @Override
                              public void run() {
                                        while (isstart) {
                                                  Message mas = new Message();
                                                  mas.what = windex;
                                                  handler.sendMessage(mas);
                                                  if (windex == 3) {
                                                            try {
                                                                      Thread.sleep(1200);
                                                            } catch (InterruptedException e) {
                                                                      Log.i("==", "线程出错" + e.toString());
                                                            }
                                                  } else {
                                                            try {
                                                                      Thread.sleep(800);
                                                            } catch (InterruptedException e) {
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
