package com.showmo.demo.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.showmo.demo.util.StringUtil;
import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmAccountManager;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * Created by Administrator on 2016/7/4.
 */
public class RegisterActivity2 extends Activity implements View.OnClickListener{

    IXmSystem xmSystem;
    IXmAccountManager xmAccountManager;

    EditText et_psw,et_chekpsw;
    Button btn_ok;

    String account,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        xmSystem = XmSystem.getInstance();
        xmAccountManager = xmSystem.xmGetAccountManager();

        et_psw = (EditText)findViewById(R.id.et_psw);
        et_chekpsw = (EditText)findViewById(R.id.et_chekpsw);

        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        account = getIntent().getExtras().getString("account");
        code = getIntent().getExtras().getString("code");
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0x123){
                Toast.makeText(RegisterActivity2.this,"注册失败！",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x124){
                Toast.makeText(RegisterActivity2.this,"密码格式错误或不一致！",Toast.LENGTH_LONG).show();
            }else if(msg.what==0x125){
                Toast.makeText(RegisterActivity2.this,"注册成功！",Toast.LENGTH_LONG).show();
            }
            super.handleMessage(msg);
        }
    };

    private void btn_ok(){
        if (checkInputContent()) {
            showLoadingDialog();
            netTaskRegister();
        }
    }

    private void netTaskRegister(){
        xmAccountManager.xmRegisterAccount(account,et_psw.getText().toString().trim(),code,
                new OnXmSimpleListener(){
                    public void onErr(XmErrInfo info){
                        closeLoadingDialog();
                        mHandler.sendEmptyMessage(0x123);
                    }
                    public void onSuc(){
                        closeLoadingDialog();
                        mHandler.sendEmptyMessage(0x125);
                        setResult(101);
                        finish();
                    }
                });
    }

    ProgressDialog dialog;
    private void showLoadingDialog(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍后...");
        dialog.show();
    }
    private void closeLoadingDialog(){
        dialog.dismiss();
    }

    private boolean checkInputContent() {
        String psw = et_psw.getText().toString().trim() ;
        String pswRe = et_chekpsw.getText().toString().trim();
        if(!StringUtil.checkPsw(psw)  ){
            mHandler.sendEmptyMessage(0x124);
            return false ;
        }else if(!StringUtil.checkPswRe(psw, pswRe)){
            mHandler.sendEmptyMessage(0x124);
            return false ;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:
                btn_ok();
                break;
        }
    }
}
