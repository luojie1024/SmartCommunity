package com.showmo.demo.login;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.showmo.demo.myutils.ToastUtil;
import com.showmo.demo.util.StringUtil;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmAccountManager;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnCheckVerifyCodeListener;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * Created by Administrator on 2016/7/4.
 */
public class RegisterActivity extends Activity implements View.OnClickListener{

    IXmSystem xmSystem;
    IXmAccountManager xmAccountManager;

    EditText et_username,et_code;
    Button btn_code,btn_next;

    Timer timer;
    int timeCount = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        xmSystem = XmSystem.getInstance();
        xmAccountManager = xmSystem.xmGetAccountManager();

        et_username = (EditText)findViewById(R.id.et_username);
        et_code = (EditText)findViewById(R.id.et_code);
        btn_code = (Button)findViewById(R.id.btn_code);
        btn_next = (Button)findViewById(R.id.btn_next);

        btn_code.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x123){
                if(timeCount>0){
                    timeCount --;
                    btn_code.setText(timeCount+"秒后重新获取");
                }else {
                    timer.cancel();
                    btn_code.setText("重新获取验证码");
                    btn_code.setClickable(true);
                }
            }else if(msg.what==0x124){
                Toast.makeText(RegisterActivity.this,"获取验证码失败！",Toast.LENGTH_LONG).show();
                timer.cancel();
                btn_code.setText("重新获取验证码");
                btn_code.setClickable(true);
            }else if(msg.what==0x125){
                Toast.makeText(RegisterActivity.this,"账号已被注册！",Toast.LENGTH_LONG).show();
                timer.cancel();
                btn_code.setText("获取验证码");
                btn_code.setClickable(true);
            }else if(msg.what==0x126){
                Toast.makeText(RegisterActivity.this,"验证码已经发送成功！",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x127){
                Toast.makeText(RegisterActivity.this,"验证码格式错误！",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x128){
                Toast.makeText(RegisterActivity.this,"账号发生改变，请重新获取验证码",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x129){
                Toast.makeText(RegisterActivity.this,"验证码错误！",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    private void btn_code(String account){
        btn_code.setClickable(false);
        timeCount = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x123);
            }
        },0,1000);

        isExistAccount(account);//判断账号是否已被注册
    }

    private void isExistAccount(final String account){
        //注册前，首先判断账号是否已经被注册
        xmAccountManager.xmCheckAccountExist(account, new OnXmListener<Boolean>() {
            @Override
            public void onErr(XmErrInfo info) {
                mHandler.sendEmptyMessage(0x124);
            }

            @Override
            public void onSuc(Boolean info) {
                if (info) {
                    mHandler.sendEmptyMessage(0x125);//账号已被注册
                } else {
                    xmGetCode(account);//获取验证码
                }
            }
        });
    }

    private String mAccountWhenGetVerifyCode="";
    private void xmGetCode(final String account){
        xmAccountManager.xmGetVerifycode(account, new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                mHandler.sendEmptyMessage(0x124);
            }
            @Override
            public void onSuc() {
                mAccountWhenGetVerifyCode = account;
                mHandler.sendEmptyMessage(0x126);
            }
        });
    }

    private void btn_next(String account,String code){
        if(!com.showmo.demo.util.StringUtil.checkVerificationCode(code)){
            mHandler.sendEmptyMessage(0x127);
            return;
        }
        showLoadingDialog();
        if(account.equals(mAccountWhenGetVerifyCode)){
            xmAccountManager.xmCheckVerifyCode(account, code, new OnCheckVerifyCodeListener() {
                @Override
                public void onErr(XmErrInfo info) {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x129);
                }

                @Override
                public void onSuc() {
                    closeLoadingDialog();
                    gotoRegister2();
                }

                @Override
                public void onCodeNotExist() {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x129);
                }

                @Override
                public void onCodeNotCorrect() {
                    closeLoadingDialog();
                    mHandler.sendEmptyMessage(0x129);
                }
            });
        }else {
            mHandler.sendEmptyMessage(0x128);
            closeLoadingDialog();
        }
    }

    private void gotoRegister2(){
        Intent in = new Intent(this,RegisterActivity2.class);
        in.putExtra("account",et_username.getText().toString().toLowerCase().trim());
        in.putExtra("code",et_code.getText().toString().trim());
        startActivityForResult(in,100);
    }

    ProgressDialog dialog;
    private void showLoadingDialog(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("检验验证码中...");
        dialog.show();
    }
    private void closeLoadingDialog(){
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        String account = et_username.getText().toString().trim();
        String code = et_code.getText().toString().trim();
        if(!StringUtil.checkPhoneNumber(account)&&!StringUtil.checkEmail(account)){
            ToastUtil.toastShort(this, "用户格式不正确！");
            return;
        }
        switch (v.getId()){
            case R.id.btn_code:
                btn_code(account);
                break;
            case R.id.btn_next:
                btn_next(account,code);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==101){
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        timeCount = 60;
        btn_code.setClickable(true);
        btn_code.setText("获取验证码");
        super.onResume();
    }

}
