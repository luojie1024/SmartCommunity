package com.way.tabui.gokit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.way.adapter.DatabaseAdapter;
import com.way.adapter.DatebaseHelper;
import com.way.adapter.SmartSwitchListAdapter;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.controlmodule.GosControlModuleBaseActivity;
import com.way.tabui.view.SlideListView2;
import com.way.util.ControlProtocol;
import com.way.util.ConvertUtil;
import com.way.util.GizMetaData;
import com.way.util.StringUtils;
import com.way.util.SwitchInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 灯光/插座/开关操作界面
 */
public class SmartSwitchListActivity extends GosControlModuleBaseActivity {
          /**
           * The GizWifiDevice device
           */
          private GizWifiDevice device;
          /**
           * The device statu.
           */
          private HashMap<String, Object> deviceStatu;
          /**
           * The isUpDateUi
           */
          protected static boolean isUpDateUi = true;

          protected static final int OPEN = 1;
          protected static final int CLOSE = 0;
          protected static final int UPDATA = 99;
          protected static final int DELETE = 100;
          //查询方式
          private static final int UPDATA_STATUS = 1;
          private static final int UPDATA_INFO = 0;

          private LinearLayout bt_addCurtain;
          private View lldevice;
          private ScrollView svListGroup;
          private SlideListView2 listview_air_con_mes;
          private DatabaseAdapter dbAdapter;
          private DatebaseHelper dbHelper;
          private SmartSwitchListAdapter adapter;


          ArrayList<SwitchInfo> giz = new ArrayList<SwitchInfo>();
          private String MacAddress, name, address;
          private int type;
          //发送状态更新广播
          private static final String KUOZHAN = "kuozhan";
          private byte[] SEND_BROAD = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x15};

          //UI更新广播
          public static final String action = "com.luojie.action";

          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_smart_switch_list);
                    initDevice();
                    MacAddress = device.getMacAddress();
                    setActionBar(true, true, "灯光/插座/开关");
                    initDB();
                    initView();
                    initList();
                    //开启广播
                    initStatusListener();
          }


          /**
           * description:初始化数据库
           * auther：joahluo
           * time：2017/6/27 16:34
           */
          private void initDB() {
                    //操作数据库
                    dbAdapter = new DatabaseAdapter(this);
                    //创建数据库
                    dbHelper = new DatebaseHelper(this);
                    //数据
                    switchInfo = new SwitchInfo();
          }


          /**
           * description:设置设备状态监听
           * auther：joahluo
           * time：2017/6/27 16:27
           */
          private void initStatusListener() {
                    //设置设备状态监听
                    device.setListener(mListener);
                    //通知设备上报数据
                    initBroadreceive();
          }


          @Override
          protected void onResume() {
                    if ((adapter.setDate(MacAddress)) == null) {
                              bt_addCurtain.setVisibility(View.VISIBLE);
                              svListGroup.setVisibility(View.GONE);
                    } else {
                              bt_addCurtain.setVisibility(View.GONE);
                              svListGroup.setVisibility(View.VISIBLE);
                    }
                    initevent();
                    super.onResume();
          }

          @Override
          protected void onDestroy() {
                    super.onDestroy();
          }

          private void initDevice() {
                    Intent intent = getIntent();
                    device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
                    deviceStatu = new HashMap<String, Object>();
                    Log.i("device", "initDevice:----> " + device.getMacAddress());
          }

          private void initList() {
                    adapter = new SmartSwitchListAdapter(handler, SmartSwitchListActivity.this);
                    adapter.setHandler(handler);
                    listview_air_con_mes.setAdapter(adapter);

          }

          private void initView() {
                    setProgressDialog();
                    progressDialog.show();
                    bt_addCurtain = (LinearLayout) findViewById(R.id.bt_add_device);
                    listview_air_con_mes = (SlideListView2) findViewById(R.id.slideListView1);
                    lldevice = findViewById(R.id.lldevice);
                    svListGroup = (ScrollView) findViewById(R.id.svListGroup);
                    progressDialog.cancel();
                    listview_air_con_mes.initSlideMode(SlideListView2.MOD_RIGHT);
          }

          /**
           * description：开启广播监听
           * auther：joahluo
           * time：2017/6/27 14:30
           */
          private void initBroadreceive() {
                    //TODO: 发送状态同步广播给服务器
                    try {
                              sendJson(KUOZHAN, SEND_BROAD);
                    } catch (JSONException e) {
                              e.printStackTrace();
                    }
          }

          private void initevent() {
                    // TODO: 点击列表开启灯泡控制
                    listview_air_con_mes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                              @Override
                              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        name = adapter.getmList().get(position).getName();
                                        //address地址代码
                                        address = adapter.getmList().get(position).getAddress();
                                        type = adapter.getmList().get(position).getType();
                                        Intent intent = new Intent(SmartSwitchListActivity.this, SmartSwitchActivity.class);
                                        intent.putExtra("name", name);
                                        intent.putExtra("address", address);
                                        intent.putExtra("type", type);
                                        Bundle bundle = new Bundle();
                                        //传设备
                                        bundle.putParcelable("GizWifiDevice", device);
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, 1000);
                              }
                    });

                    bt_addCurtain.setOnClickListener(new View.OnClickListener() {

                              @Override
                              public void onClick(View v) {
                                        if (!checkNetwork(SmartSwitchListActivity.this)) {
                                                  Toast.makeText(SmartSwitchListActivity.this, R.string.network_error,
                                                       Toast.LENGTH_SHORT).show();
                                        } else {
                                                  Intent intent = new Intent(SmartSwitchListActivity.this,
                                                       SmartSwitchTypeListActivity.class);
                                                  intent.putExtra("bindgiz", MacAddress);
                                                  intent.putExtra("tablename", GizMetaData.SwitchTable.TABLE_NAME);
                                                  startActivity(intent);
                                        }
                              }
                    });

          }

          int position;
          Dialog dialog;
          SwitchInfo switchInfo;

          private void setDeviceInfo() {
                    dialog = new AlertDialog.Builder(this).setView(new EditText(this))
                         .create();
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setContentView(R.layout.alert_curtain_set_mes);
                    final EditText etAlias;
                    final EditText etMAC;
                    etAlias = (EditText) window.findViewById(R.id.et_Alias);
                    etMAC = (EditText) window.findViewById(R.id.et_Brand);
                    LinearLayout llNo, llSure;
                    llNo = (LinearLayout) window.findViewById(R.id.llNo);
                    llSure = (LinearLayout) window.findViewById(R.id.llSure);
                    if (switchInfo.getName() != null) {
                              etAlias.setText(switchInfo.getName());
                    }
                    etMAC.setText("" + switchInfo.getAddress());
                    // }

                    llNo.setOnClickListener(new View.OnClickListener() {

                              @Override
                              public void onClick(View v) {
                                        dialog.cancel();
                              }
                    });

                    //滑动修改
                    llSure.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                        ;
                                        String name = etAlias.getText().toString();
                                        String address = etMAC.getText().toString().toUpperCase();
                                        if (StringUtils.checkNum(address)) {
                                                  try {
                                                            switchInfo.setAddress(address);
                                                            switchInfo.setName(name);
                                                            //数据更新
                                                            if ((adapter.updateList(switchInfo, UPDATA_INFO).setDate(MacAddress)) == null) {
                                                                      bt_addCurtain.setVisibility(View.VISIBLE);
                                                                      svListGroup.setVisibility(View.GONE);
                                                            } else {
                                                                      bt_addCurtain.setVisibility(View.GONE);
                                                                      svListGroup.setVisibility(View.VISIBLE);
                                                            }
                                                            Toast.makeText(getApplicationContext(), "修改成功",
                                                                 Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                  } catch (Exception e) {
                                                            Toast.makeText(getApplicationContext(), "修改失败",
                                                                 Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                  }

                                        } else {
                                                  Toast.makeText(getApplicationContext(), "修改失败！地址必须为不带任何标点符号的8位16进制数",
                                                       Toast.LENGTH_SHORT).show();
                                        }

                              }
                    });
          }

          //滑动删除
          protected void deleteAlert(Context context) {
                    String title, message, nbtext, pbtext;
                    title = (String) getText(R.string.prompt);
                    message = "确定要删除么？";
                    nbtext = "取消";
                    pbtext = "确定";
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(title);
                    builder.setMessage(message);
                    builder.setNegativeButton(nbtext, null);
                    builder.setPositiveButton(pbtext,
                         new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                             if ((adapter.deleteDate(index).setDate(MacAddress)) == null) {
                                                       bt_addCurtain.setVisibility(View.VISIBLE);
                                                       svListGroup.setVisibility(View.GONE);
                                             } else {
                                                       bt_addCurtain.setVisibility(View.GONE);
                                                       svListGroup.setVisibility(View.VISIBLE);
                                             }
                                   }
                         });
                    builder.show();
          }

          int index;
          Handler handler = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                              super.handleMessage(msg);
                              int position = msg.arg1;
                              ArrayList<SwitchInfo> mlist = adapter.getmList();
                              switch (msg.what) {
                                        case UPDATA:
                                                  index = msg.arg1;
                                                  switchInfo = (SwitchInfo) msg.obj;
                                                  setDeviceInfo();
                                                  break;
                                        case DELETE:
                                                  index = msg.arg1;
                                                  deleteAlert(SmartSwitchListActivity.this);
                                                  break;
                              }
                    }

          };


          @Override
          public boolean onCreateOptionsMenu(Menu menu) {

                    getMenuInflater().inflate(R.menu.add_devices, menu);
                    return true;
          }


          //右上角增加设备
          @Override
          public boolean onOptionsItemSelected(MenuItem item) {
                    super.onOptionsItemSelected(item);
                    Intent intent;
                    switch (item.getItemId()) {
                              case android.R.id.home:

                                        finish();
                                        break;

                              case R.id.add_device:
                                        if (!checkNetwork(SmartSwitchListActivity.this)) {
                                                  Toast.makeText(SmartSwitchListActivity.this, R.string.network_error,
                                                       Toast.LENGTH_SHORT).show();
                                        } else {
                                                  intent = new Intent(SmartSwitchListActivity.this,
                                                       SmartSwitchTypeListActivity.class);
                                                  intent.putExtra("bindgiz", MacAddress);
                                                  intent.putExtra("tablename", GizMetaData.SwitchTable.TABLE_NAME);
                                                  startActivity(intent);
                                        }
                                        break;

                    }
                    return super.onOptionsItemSelected(item);
          }

          @Override
          public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                              Intent intent = new Intent(SmartSwitchListActivity.this, MainActivity.class);
                              Bundle bundle = new Bundle();
                              bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
                              intent.putExtras(bundle);
                              intent.putExtra("isoffline", false);

                              // receiver.clearAbortBroadcast();
                              setResult(1001, intent);

                              finish();
                    }
                    return false;
          }


          /**
           * description:发送数据
           * auther：joahluo
           * time：2017/6/26 20:14
           */
          private void sendJson(String key, Object value) throws JSONException {
                    ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
                    hashMap.put(key, value);
                    device.write(hashMap, 0);
                    Log.i("==", hashMap.toString());
          }

          /**
           * description:获取设备状态
           * auther：joahluo
           * time：2017/6/27 15:53
           */
          GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
                    @Override
                    public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
                              if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                                        // 已定义的设备数据点，有布尔、数值和枚举型数据
                                        if (dataMap.get("data") != null) {
                                                  ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                                                  // 获得kuozhan类型数据
                                                  byte[] bytes = (byte[]) map.get("kuozhan");
                                                  byte[] deviceId = new byte[4];
                                                  int sum = 0;
                                                  int len = bytes.length / 6;
                                                  for (int i = 0; i < bytes.length/8; i++) {
                                                            if (((bytes[5 + 6 * i] & 0xf0) == 0xf0)) {

                                                                      for (int j = 0; j < 4; j++) {
                                                                                deviceId[j] = bytes[j + 1 + i * 6];
                                                                      }
                                                                      String mac = ConvertUtil.byteStringToHexString(deviceId).toUpperCase();
                                                                      //获取数据库数据
                                                                      switchInfo = dbAdapter.findSwitchInfoStatus(mac);
                                                                      //开关类型 状态
                                                                      switch ((int) bytes[0 + 6 * i]) {
                                                                                case (int) ControlProtocol.DevType.SWITCH_THREE:
                                                                                          if ((bytes[5 + 6 * i] & 0x4) == 0x4) {
                                                                                                    switchInfo.setStatus3(1);
                                                                                          } else {
                                                                                                    switchInfo.setStatus3(0);
                                                                                          }
                                                                                case (int) ControlProtocol.DevType.SWITCH_TWO:
                                                                                          if ((bytes[5 + 6 * i] & 0x2) == 0x2) {
                                                                                                    switchInfo.setStatus2(1);
                                                                                          } else {
                                                                                                    switchInfo.setStatus2(0);
                                                                                          }
                                                                                case (int) ControlProtocol.DevType.SWITCH_ONE:
                                                                                          if (mac.equals("00000000")) {
                                                                                                    break;
                                                                                          }
                                                                                          if ((bytes[5 + 6 * i] & 0x1) == 0x1) {
                                                                                                    switchInfo.setStatus1(1);
                                                                                          } else {
                                                                                                    switchInfo.setStatus1(0);
                                                                                          }
                                                                                          sum++;
                                                                                          adapter.updateList(switchInfo, UPDATA_STATUS);
                                                                                          break;
                                                                                case (int) ControlProtocol.DevType.PLUG_FIVE:
                                                                                          if ((bytes[5 + 6 * i] & 0x1) == 0x1) {
                                                                                                    switchInfo.setStatus1(1);
                                                                                          } else {
                                                                                                    switchInfo.setStatus1(0);
                                                                                          }
                                                                                          sum++;
                                                                                          adapter.updateList(switchInfo, UPDATA_STATUS);
                                                                                          break;
                                                                      }
                                                            }

                                                  }
                                                  if (sum != 0) {
                                                            Toast.makeText(getApplicationContext(), "接收到" + sum + "条成功！",
                                                                 Toast.LENGTH_SHORT).show();
                                                            //发送广播，通知UI更新
                                                            Intent intent = new Intent(action);
                                                            sendBroadcast(intent);
                                                  }


                                        }
                              }

                    }

          };

}
