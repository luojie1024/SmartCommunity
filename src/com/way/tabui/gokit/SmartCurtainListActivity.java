package com.way.tabui.gokit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.SmartOCAdapter;
import com.way.tabui.actity.AddOcdeviceActivity;
import com.way.tabui.actity.MainActivity;
import com.way.tabui.controlmodule.GosControlModuleBaseActivity;
import com.way.tabui.settingsmodule.GosSettiingsActivity;
import com.way.util.Gizinfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import zxing.CaptureActivity;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/6/13 23:06
 */
public class SmartCurtainListActivity extends GosControlModuleBaseActivity {

          /** 传输字符 */
          private static final String KEY_Sendcom = "Send_com";

          /** The GizWifiDevice device */
          private GizWifiDevice device;

          /** The device statu. */
          private HashMap<String, Object> deviceStatu;
          /** The isUpDateUi */
          protected static boolean isUpDateUi = true;

          protected static final int OPEN = 1;
          protected static final int CLOSE = 0;
          private TextView tv_nodevice;
          private ListView smart_oc_listview;
          private SmartOCAdapter adapter;
          ArrayList<Gizinfo> giz = new ArrayList<Gizinfo>();
          private String MacAddress, name, address;

          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_smart_list_curtain);
                    initDevice();
                    MacAddress = device.getMacAddress();
                    setActionBar(true, true, "窗帘");
                    setProgressDialog();
                    initView();
                    //  initdata();
                    initList();
          }

          @Override
          protected void onResume() {
                    if((adapter.setDate(MacAddress))==null){
                              tv_nodevice.setVisibility(View.VISIBLE);
                    }else {
                              tv_nodevice.setVisibility(View.GONE);
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
                    Log.i("device", "initDevice:----> "+device.getMacAddress());
          }

          private void initView() {
                    smart_oc_listview = (ListView) findViewById(R.id.smart_oc_listview);
                    tv_nodevice = (TextView) findViewById(R.id.tv_nodevice);
          }

          int gizid;

          private void initevent() {
                    // TODO: 2017/6/14 点击列表
                    smart_oc_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                              @Override
                              public void onItemClick(AdapterView<?> parent, View view,
                                                      int position, long id) {
                                        name = adapter.getmList().get(position).getName();
                                        //address地址代码
                                        address = adapter.getmList().get(position).getAddress();
                                        Intent intent = new Intent(SmartCurtainListActivity.this, SmartCurtainActivity.class);
                                        intent.putExtra("name",name);
                                        intent.putExtra("address",address);
                                        Bundle bundle = new Bundle();
                                        //传设备
                                        bundle.putParcelable("GizWifiDevice",device);
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent, 1000);
                              }
                    });

          }
//	private void initdata() {
//		progressDialog.setMessage("读取数据中...");
//		progressDialog.show();
//		SQLiteDatabase db = dbHelper.getWritableDatabase();
//		String whereClause = GizMetaData.GizTable.GIZ_BINDGIZ + "=?";
//		String[] whereArgs = { MacAddress };
//		String[] columns = { GizMetaData.GizTable.GIZ_ID,
//				GizMetaData.GizTable.GIZ_NAME,
//				GizMetaData.GizTable.GIZ_ADDRESS,
//				GizMetaData.GizTable.GIZ_BINDGIZ,
//				GizMetaData.GizTable.GIZ_USERID, GizMetaData.GizTable.GIZ_FLAG };
//		// 参数说明(是否去除重复记录,表明,要查询的列，查询条件，查询条件的值，分组条件，分组条件的值，排序，排序条件)
//		Cursor c = db.query(true, GizMetaData.GizTable.TABLE_NAME, columns,
//				whereClause, whereArgs, null, null, null, null);
//		if (c.getCount() == 0) {
//			tv_nodevice.setVisibility(View.VISIBLE);
//		} else {
//			tv_nodevice.setVisibility(View.GONE);
//		}
//		c.close();
//		db.close();
////		giz = dbAdapter.findbybindgiz(MacAddress);
//		// Toast.makeText(getApplicationContext(), giz.get(0).getName(),
//		// Toast.LENGTH_SHORT).show();
//	}

          Gizinfo gizinfo;

          Handler handler = new Handler(){
                    public void handleMessage(android.os.Message msg) {
                              super.handleMessage(msg);
                              int position =msg.arg1;
                              ArrayList<Gizinfo> mlist =adapter.getmList();
                              switch (msg.what){
                                        case OPEN:
                                                  try {

                                                            sendJson(KEY_Sendcom, Integer.parseInt(mlist
                                                                 .get(position).getAddress()));
                                                            mlist.get(position).setFlag(1);
                                                            adapter.updateList(mlist.get(position));

                                                  } catch (NumberFormatException e) {
                                                            // TODO Auto-generated catch block
                                                            Toast.makeText(getApplicationContext(), "指令发送失败",
                                                                 Toast.LENGTH_SHORT).show();
                                                  } catch (JSONException e) {
                                                            // TODO Auto-generated catch block
                                                            Toast.makeText(getApplicationContext(), "指令发送失败",
                                                                 Toast.LENGTH_SHORT).show();
                                                  }
                                                  break;
                                        case CLOSE:
                                                  try {
                                                            sendJson(KEY_Sendcom, Integer.parseInt(adapter.getmList()
                                                                 .get(position).getAddress()) + 1);
                                                            mlist.get(position).setFlag(0);
                                                            adapter.updateList(mlist.get(position));
                                                  } catch (NumberFormatException e) {
                                                            // TODO Auto-generated catch block
                                                            Toast.makeText(getApplicationContext(), "指令发送失败",
                                                                 Toast.LENGTH_SHORT).show();
                                                  } catch (JSONException e) {
                                                            // TODO Auto-generated catch block
                                                            Toast.makeText(getApplicationContext(), "指令发送失败",
                                                                 Toast.LENGTH_SHORT).show();
                                                  }
                                                  break;
                              }
                    }

          };

          private void initList() {
//		adapter = new SmartOCAdapter(SmartOCActivity.this, giz);
//		adapter.setHandler(handler);
                    adapter=new SmartOCAdapter(handler,this);
                    smart_oc_listview.setAdapter(adapter);
                    progressDialog.cancel();
          }



          private void sendJson(String key, Object value) throws JSONException {
                    ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
                    hashMap.put(key, value);
                    device.write(hashMap, 0);
                    Log.i("==", hashMap.toString());
                    // Log.i("Apptest", hashMap.toString());
          }

          @Override
          public boolean onCreateOptionsMenu(Menu menu) {
                    // Inflate the menu; this adds items to the action bar if it is present.
                    // if (!TextUtils.isEmpty(spf.getString("UserName", "")) &&
                    // !TextUtils.isEmpty(spf.getString("PassWord", ""))) {
                    getMenuInflater().inflate(R.menu.devicelist_logout, menu);
                    // } else {
                    // if (getIntent().getBooleanExtra("ThredLogin", false)) {
                    // getMenuInflater().inflate(R.menu.devicelist_logout, menu);
                    // } else {
                    // getMenuInflater().inflate(R.menu.devicelist_login, menu);
                    // }
                    // }

                    return true;
          }

          @Override
          public boolean onOptionsItemSelected(MenuItem item) {
                    super.onOptionsItemSelected(item);
                    Intent intent;
                    switch (item.getItemId()) {
                              case android.R.id.home:
                                        // if (checkNetwork(SmartOCActivity.this)) {
                                        // progressDialog.show();
                                        // handler.sendEmptyMessage(GETLIST);
                                        // }
                                        finish();
                                        break;
                              case R.id.action_QR_code:

                                        intent = new Intent(SmartCurtainListActivity.this, CaptureActivity.class);
                                        intent.putExtra("fromoc", true);
                                        intent.putExtra("bindgiz", MacAddress);
                                        startActivity(intent);
                                        break;
                              case R.id.action_change_user:
                                        break;
                              case R.id.action_addDevice:
                                        if (!checkNetwork(SmartCurtainListActivity.this)) {
                                                  Toast.makeText(SmartCurtainListActivity.this, R.string.network_error,
                                                       Toast.LENGTH_SHORT).show();
                                        } else {
                                                  intent = new Intent(SmartCurtainListActivity.this,
                                                       AddOcdeviceActivity.class);
                                                  intent.putExtra("bindgiz", MacAddress);
                                                  startActivity(intent);
                                        }
                                        break;
                              case R.id.action_site:
                                        intent = new Intent(SmartCurtainListActivity.this,
                                             GosSettiingsActivity.class);
                                        startActivity(intent);
                                        break;
                    }
                    return super.onOptionsItemSelected(item);
          }

          @Override
          public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                              Intent intent = new Intent(SmartCurtainListActivity.this, MainActivity.class);
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

}
