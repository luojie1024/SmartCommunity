package com.showmo.demo.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.showmo.demo.adddevice.AddDeviceUserEnsure;
import com.showmo.demo.util.ActivityManager;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmDevice;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmMgrConnectStateChangeListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29.
 */
public class DeviceslistActivity extends GosBaseActivity{

    IXmSystem xmSystem;
    XmAccount account;
    ListView listView;
    MyAdapter adapter;
    List<XmDevice> mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceslist);
        setActionBar(true, true, "监控摄像");
        init();
        initview();
        initdata();
    }

    ActivityManager manager;
    private void init(){
        xmSystem = XmSystem.getInstance();
        account = (XmAccount)getIntent().getExtras().getSerializable("user");
        xmSystem.registerOnMgrConnectChangeListener(onXmMgrConnectStateChangeListener);
        loginMgr();
        manager = ActivityManager.getInstance();
        manager.addActivity(this);
    }
    private void loginMgr(){
        xmSystem.xmMgrSignin(new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
//                Log.v("AAAAA", "MgrSignin fail!");
            }
            @Override
            public void onSuc() {
//                Log.v("AAAAA", "MgrSignin suc!");
            }
        });
    }
    OnXmMgrConnectStateChangeListener onXmMgrConnectStateChangeListener = new OnXmMgrConnectStateChangeListener() {
        @Override
        public void onChange(boolean connectState) {
            Log.v("AAAAA", "OnXmMgrConnectStateChangeListener onChange is " + connectState);
        }
    };

    private void initview(){
        listView = (ListView)findViewById(R.id.list);
        mlist = new ArrayList<XmDevice>();
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemListener);
    }

    private void initdata(){
//        xmSystem = XmSystem.getInstance();
//        if(!getIntent().getExtras().getBoolean("isDemo")) {
//            account = (XmAccount) getIntent().getExtras().getSerializable("username");
//        }
        getDevices();
    }

    private void getDevices(){
        Log.v("AAAAA","getDevices");
        xmSystem.xmGetDeviceList(new OnXmListener<List<XmDevice>>() {
            @Override
            public void onErr(XmErrInfo info) {
                Log.v("AAAAA", "get devices fail");
            }

            @Override
            public void onSuc(List<XmDevice> info) {
                Log.v("AAAAA","getDevices  onSuc");
                mlist = info;
                adapter.notifyDataSetChanged();
            }
        });
    }

    //设备选择事件
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cameraId = mlist.get(position).getmCameraId();
            Intent in = new Intent(DeviceslistActivity.this, PlayActivity.class);
            in.putExtra("cameraId",cameraId);
            startActivityForResult(in,100);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==101){
            getDevices();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        getDevices();
        super.onResume();
    }

    class MyAdapter extends BaseAdapter{

        Context context;

        public MyAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler holer = null;
            if(convertView==null){
                holer = new ViewHoler();
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem,null);
                holer.tv = (TextView)convertView.findViewById(R.id.name);
                convertView.setTag(holer);
            }else{
                holer = (ViewHoler)convertView.getTag();
            }
            holer.tv.setText(mlist.get(position).getmName());
            return convertView;
        }

        class ViewHoler{
            TextView tv;
        }
    }

    //增加设备
    private void addDevice(){
        Intent in = new Intent(this,AddDeviceUserEnsure.class);
        in.putExtra("isDemo",account.isDemo());
        if(!account.isDemo())
            in.putExtra("username",account.getmUsername());
        startActivity(in);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_devices, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.add_device:
                addDevice();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
