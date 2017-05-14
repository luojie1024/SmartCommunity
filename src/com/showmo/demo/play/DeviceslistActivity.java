package com.showmo.demo.play;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmDevice;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;

/**
 * Created by Administrator on 2016/6/29.
 */
public class DeviceslistActivity extends Activity{

    IXmSystem xmSystem;
    XmAccount account;

    ListView listView;
    MyAdapter adapter;
    List<XmDevice> mlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceslist);

        initview();

        initdata();
    }

    private void initview(){
        listView = (ListView)findViewById(R.id.list);
        mlist = new ArrayList<XmDevice>();
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemListener);
    }

    private void initdata(){
        xmSystem = XmSystem.getInstance();
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
}
