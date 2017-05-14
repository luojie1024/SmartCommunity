package com.showmo.demo.maincotent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.showmo.demo.account.AccountActivity;
import com.showmo.demo.adddevice.AddDeviceUserEnsure;
import com.showmo.demo.play.DeviceslistActivity;
import com.showmo.demo.util.ActivityManager;
import com.showmo.demo.util.spUtil;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmMgrConnectStateChangeListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * Created by Administrator on 2016/6/28.
 */
public class MainContentActivity extends Activity implements View.OnClickListener{

    IXmSystem xmSystem;

    Button bt_list,bt_binder,bt_account;
    TextView title;

    XmAccount account;

    ActivityManager manager;

    spUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincontent);

        init();

        initview();
    }

    private void initview(){
        bt_list = (Button)findViewById(R.id.bt_list);
        bt_binder = (Button)findViewById(R.id.bt_binder);
        bt_account = (Button)findViewById(R.id.bt_account);

        bt_list.setOnClickListener(this);
        bt_binder.setOnClickListener(this);
        bt_account.setOnClickListener(this);

        title = (TextView)findViewById(R.id.title);
        title.setText(account.isDemo() ? "游客用户" : "普通用户");

        sp = new spUtil(this);
        sp.setisDemo(account.isDemo());
    }

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

    private void bt_list(){
        Intent in = new Intent(this,DeviceslistActivity.class);
        in.putExtra("isDemo",account.isDemo());
        if(!account.isDemo())
            in.putExtra("username",account.getmUsername());
        startActivity(in);
    }

    private void bt_binder(){
        Intent in = new Intent(this,AddDeviceUserEnsure.class);
        in.putExtra("isDemo",account.isDemo());
        if(!account.isDemo())
            in.putExtra("username",account.getmUsername());
        startActivity(in);
    }

    private void bt_account(){
//       Intent in = new Intent(this,AccountActivity.class);
//        in.putExtra("isDemo",account.isDemo());
//        if(!account.isDemo())
//            in.putExtra("username",account.getmUsername());
//        startActivity(in);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                Toast.makeText(MainContentActivity.this,"你没有权限，请先注册登录~",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_list:
                bt_list();
                break;
            case R.id.bt_binder:
                if(account.isDemo()){
                    mHandler.sendEmptyMessage(0x123);
                    break;
                }
                bt_binder();
                break;
            case R.id.bt_account:
                bt_account();
                break;
        }
    }

    long exitTime = 0;

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
//            if((System.currentTimeMillis()-exitTime) > 2000){
//                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                exitTime = System.currentTimeMillis();
//            } else {
//                finish();
//                System.exit(0);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
