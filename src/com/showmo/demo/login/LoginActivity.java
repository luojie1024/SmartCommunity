package com.showmo.demo.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.showmo.demo.maincotent.MainContentActivity;
import com.showmo.demo.util.spUtil;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * Created by Administrator on 2016/6/28.
 */
public class LoginActivity extends Activity implements View.OnClickListener{

    IXmSystem xmSystem;

    EditText et_username,et_psw;
    Button bt_login,bt_logindemo,bt_register;

    spUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initview();

        init();
    }
    private void initview(){
        et_username = (EditText)findViewById(R.id.et_username);
        et_psw = (EditText)findViewById(R.id.et_psw);
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_logindemo = (Button)findViewById(R.id.bt_fast);
        bt_register = (Button)findViewById(R.id.bt_register);

        bt_login.setOnClickListener(this);
        bt_logindemo.setOnClickListener(this);
        bt_register.setOnClickListener(this);

        sp = new spUtil(this);
        et_username.setText(sp.getUsername());
    }

    private void init(){
        xmSystem = XmSystem.getInstance();
        xmSystem.xmInit(this, "CN", new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                Log.v("AAAAA", "init Fail");
            }

            @Override
            public void onSuc() {
                Log.v("AAAAA", "init Suc");
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                Toast.makeText(LoginActivity.this,"登录成功！",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x124){
                Toast.makeText(LoginActivity.this,"登录失败！",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    ProgressDialog dialog;
    private void showLoadingDialog(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍后...");
        dialog.show();
    }
    private void closeLoadingDialog(){
        dialog.dismiss();
    }

    private void login(){
        String username = et_username.getText().toString();
        String psw = et_psw.getText().toString();
        if(username.equals("")||psw.equals("")){
            Toast.makeText(this,"用户名或密码不能为空！",Toast.LENGTH_LONG).show();
            return;
        }
        showLoadingDialog();
        try {
            xmSystem.xmLogin(username, psw, new OnXmListener<XmAccount>() {
                @Override
                public void onSuc(XmAccount outinfo) {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x123);
                    sp.setUsername(et_username.getText().toString());
                    loginSuc(outinfo);
                }

                @Override
                public void onErr(XmErrInfo info) {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x124);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            closeLoadingDialog();
            mHandler.sendEmptyMessage(0x124);
        }finally {

        }
    }

    private void login_demo(){
        showLoadingDialog();
        try {
            xmSystem.xmLoginDemo(new OnXmListener<XmAccount>() {
                @Override
                public void onErr(XmErrInfo info) {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x124);
                }

                @Override
                public void onSuc(XmAccount info) {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x123);
                    loginSuc(info);
                }
            });
        } catch (Exception e) {
            closeLoadingDialog();
            e.printStackTrace();
            mHandler.sendEmptyMessage(0x124);
        } finally {

        }
    }

    private void loginSuc(XmAccount info){
        Intent in = new Intent(this,MainContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", info);
        in.putExtras(bundle);
        startActivity(in);
        finish();
    }

    private void bt_register(){
        Intent in = new Intent(this,RegisterActivity.class);
        startActivity(in);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_fast:
                login_demo();
                break;
            case R.id.bt_register:
                bt_register();
                break;
        }
    }

    long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
