package com.way.tabui.gokit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.tabui.actity.AddOcdeviceActivity;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.GizMetaData;

import java.util.ArrayList;

/**
 * 作者：Jacky
 * 邮箱：550997728@qq.com
 * 时间：2017/6/15 15:22
 */
public class SmartSwitchTypeListActivity extends GosBaseActivity {

          private String MacAddress;
          private ListView listview_air_con_mes;
          ArrayList<String> mlist=new ArrayList<String>();
          /** 传过来正在连接中的的设备 */
          private GizWifiDevice device = null;
          SmartSwitchTypeListActivity.MyAdapter adapter;
          //各个品牌的控制码范围

          @Override
          protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_smart_switch_type_list);
                    setActionBar(true, true, getResources().getString(R.string.title_activity_switch_type));
                    Intent intent = getIntent();
                    MacAddress = intent.getStringExtra("bindgiz");
                    initDevice();
                    initView();
                    initData();
                    initList();

          }

          public void initDevice() {
                    Intent intent = getIntent();
                    device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
          }

          private void initView(){
                    listview_air_con_mes=(ListView) findViewById(R.id.listview_air_con_mes);
          }

          private void initList(){
                    listview_air_con_mes.setAdapter(adapter);
                    listview_air_con_mes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                              @Override
                              public void onItemClick(AdapterView<?> parent, View view,
                                                      int position, long id) {

                                        Intent intent = new Intent(SmartSwitchTypeListActivity.this,
                                             AddOcdeviceActivity.class);
                                        intent.putExtra("bindgiz", MacAddress);
                                        intent.putExtra("tablename", GizMetaData.SwitchTable.TABLE_NAME);
                                        intent.putExtra("name", mlist.get(position));
                                        startActivity(intent);
                              }
                    });

          }

          private void initData(){
                    String[] type = getResources().getStringArray(R.array.switchtype);
                    for (String str : type) {
                              mlist.add(str);
                    }
                    adapter = new SmartSwitchTypeListActivity.MyAdapter(this, mlist);
          }

          private class MyAdapter extends BaseAdapter {

                    Context context;
                    ArrayList<String> mlist;

                    public MyAdapter(Context context, ArrayList<String> mlist) {
                              super();
                              this.context = context;
                              this.mlist=mlist;
                    }

                    @Override
                    public int getCount() {
                              // TODO Auto-generated method stub
                              return mlist.size();
                    }

                    @Override
                    public Object getItem(int position) {
                              // TODO Auto-generated method stub
                              return mlist.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                              // TODO Auto-generated method stub
                              return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                              // TODO Auto-generated method stub
                              SmartSwitchTypeListActivity.MyAdapter.ViewHolder viewHolder = null;
                              if (null == convertView) {
                                        convertView = View.inflate(context, R.layout.listview_air_con_brand, null);
                                        viewHolder=new SmartSwitchTypeListActivity.MyAdapter.ViewHolder();
                                        viewHolder.tvAirBrand= (TextView) convertView.findViewById(R.id.tvAirBrand);
                                        convertView.setTag(viewHolder);
                              }else{
                                        viewHolder = (SmartSwitchTypeListActivity.MyAdapter.ViewHolder) convertView.getTag();
                              }

                              viewHolder.tvAirBrand.setText(mlist.get(position));
                              return convertView;
                    }

                    class ViewHolder{
                              public TextView tvAirBrand;
                    }
          }

          @Override
          protected void onActivityResult(int requestCode, int resultCode, Intent data){


                    if(data!=null){
                              boolean issave=data.getBooleanExtra("issave", false);
                              if(issave){
                                        finish();
                              }
                    }

                    super.onActivityResult(requestCode, resultCode, data);
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
