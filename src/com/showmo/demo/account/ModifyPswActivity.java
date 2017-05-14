package com.showmo.demo.account;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.way.tabui.gokit.R;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmAccountManager;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

/**
 * Created by Administrator on 2016/6/28.
 */
public class ModifyPswActivity extends Activity implements View.OnClickListener {

    IXmSystem xmSystem;
    IXmAccountManager xmAccountManager;

    EditText et_oldpsw,et_newpsw,et_newpsw2;
    Button bt_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypsw);

        init();

        initview();
    }

    private void init(){
        xmSystem = XmSystem.getInstance();
        xmAccountManager = xmSystem.xmGetAccountManager();
    }

    private void initview(){
        et_oldpsw = (EditText)findViewById(R.id.et_oldpsw);
        et_newpsw = (EditText)findViewById(R.id.et_newpsw);
        et_newpsw2 = (EditText)findViewById(R.id.et_newpsw2);

        bt_ok = (Button)findViewById(R.id.bt_ok);
        bt_ok.setOnClickListener(this);
    }

    private void bt_ok(){
        String oldpsw = et_oldpsw.getText().toString();
        String newpsw = et_newpsw.getText().toString();
        String newpsw2 = et_newpsw2.getText().toString();
        if(oldpsw.equals("")||newpsw.equals("")||!newpsw.equals(newpsw2)){
            Toast.makeText(ModifyPswActivity.this,"密码不能为空或新密码不一致！",Toast.LENGTH_LONG).show();
            return;
        }
        xmAccountManager.xmResetPswByOldPsw(getIntent().getExtras().getString("username"), et_newpsw.getText().toString(), et_oldpsw.getText().toString(), new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                Toast.makeText(ModifyPswActivity.this,"修改失败！",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuc() {
                Toast.makeText(ModifyPswActivity.this,"修改成功！",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_ok:
                bt_ok();
                break;
        }
    }
}
