package com.showmo.demo.account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.showmo.demo.login.LoginActivity;
import com.showmo.demo.util.ActivityManager;
import com.way.tabui.gokit.R;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmAccountManager;
import com.xmcamera.core.sysInterface.IXmSystem;

/**
 * Created by Administrator on 2016/6/28.
 */
public class AccountActivity extends Activity implements View.OnClickListener {

    IXmSystem xmSystem;
    IXmAccountManager xmAccountManager;

    Button bt_modifypsw,bt_logout;

    ActivityManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        init();

        initview();
    }

    private void init(){
        xmSystem = XmSystem.getInstance();
        xmAccountManager = xmSystem.xmGetAccountManager();
        manager = ActivityManager.getInstance();
    }

    private void initview(){
        bt_modifypsw = (Button)findViewById(R.id.bt_modifypsw);
        bt_logout = (Button)findViewById(R.id.bt_logout);

        bt_modifypsw.setOnClickListener(this);
        bt_logout.setOnClickListener(this);

        if(getIntent().getExtras().getBoolean("isDemo"))
            bt_modifypsw.setVisibility(View.INVISIBLE);
    }

    private void bt_modifypsw(){
        Intent in = new Intent(this,ModifyPswActivity.class);
        in.putExtra("usernmae",getIntent().getExtras().getString("username"));
        startActivity(in);
    }

    private void bt_logout(){
        boolean isSuc = xmSystem.xmLogout();
        if(!isSuc){
            return;
        }
        Intent in = new Intent(this,LoginActivity.class);
        startActivity(in);
        finish();
        manager.quit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_modifypsw:
                bt_modifypsw();
                break;
            case R.id.bt_logout:
                bt_logout();
                break;
        }
    }
}
