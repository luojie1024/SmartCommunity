package com.showmo.demo.adddevice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.showmo.demo.util.spUtil;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmDevice;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmBinderManager;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmBindListener;

/**
 * Created by Administrator on 2016/7/2.
 */
public class AddDeviceConfigSearchActivity extends Activity implements View.OnClickListener{

    IXmSystem xmSystem;
    IXmBinderManager xmBinderManager;

    Button btn_next;
    String wifissid,wifipsw;
    spUtil sp;
    public SharedPreferences spf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddev4);
        spf = getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
        btn_next = (Button)findViewById(R.id.btn_next);
        
        btn_next.setOnClickListener(this);

        xmSystem = XmSystem.getInstance();
        
        xmBinderManager = xmSystem.xmGetBinderManager();
        xmBinderManager.setOnBindListener(xmBindListener);

        wifissid = getIntent().getExtras().getString("wifissid");
        wifipsw = getIntent().getExtras().getString("wifipsw");
     
        xmBinderManager.beginWork(getApplicationContext(), wifissid, wifipsw);

        sp = new spUtil(this);
    }

    OnXmBindListener xmBindListener = new OnXmBindListener() {
        @Override
        public void addedByOther(String uuid, String user) {
            xmBinderManager.exitAllWork();
            finish();
            Log.v("AAAAA","addedByOther");
        }
        @Override
        public void addedSuccess(XmDevice dev) {
            xmBinderManager.exitAllWork();
            Log.v("AAAAA", "addedSuccess");
            mHandler.sendEmptyMessage(0x123);
            sp.setWifi(wifissid);
            sp.setWifiPsw(wifipsw);
            setResult(101);
            finish();
        }
        @Override
        public void addedBySelf(String uuid, String user) {
            xmBinderManager.exitAllWork();
            finish();
            Log.v("AAAAA", "addedBySelf");
        }
        @Override
        public void onDevConnectMgrErr(String uuid) {
            finish();
            Log.v("AAAAA", "onDevConnectMgrErr");
        }
        @Override
        public void addErr(String uuid,XmErrInfo errinfo) {
            finish();
            Log.v("AAAAA", "addErr");
        }
    };

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123){
                Toast.makeText(AddDeviceConfigSearchActivity.this,"添加成功！",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    private void btn_next(){
        btn_next.setVisibility(View.GONE);
        xmBinderManager.exitSendWork();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                btn_next();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        setResult(101);
        xmBinderManager.exitAllWork();
        xmBinderManager.setOnBindListener(null);
        super.onDestroy();
    }
}
