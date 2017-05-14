package com.showmo.demo.adddevice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.showmo.demo.util.spUtil;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;

/**
 * Created by Administrator on 2016/7/1.
 */
public class AddDeviceSetNetworkActivity extends Activity implements View.OnClickListener{

    EditText et_wifissid,et_wifipsw;
    Button btn_next;

    spUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddev2);
        spf = getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
        btn_next = (Button)findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        et_wifissid = (EditText)findViewById(R.id.et_wifissid);
        et_wifipsw = (EditText)findViewById(R.id.et_wifipsw);

        sp = new spUtil(this);
        et_wifissid.setText(spf.getString("workSSID", ""));
        et_wifipsw.setText(spf.getString("workSSIDPsw", ""));
    }

    public SharedPreferences spf;
    private void btn_next(){
//        String wifi = "xm_test2.4g";
//        String psw = "88888888";
        String wifi = et_wifissid.getText().toString();
        String psw = et_wifipsw.getText().toString();
        if(wifi.equals("")||psw.equals("")){
            Toast.makeText(this,"wifi或密码不能为空！",Toast.LENGTH_LONG).show();
            return;
        }
        Intent in = new Intent(this,AddDeviceUserTipClose.class);
        spf.edit().putString("workSSID", wifi).commit();
		spf.edit().putString("workSSIDPsw", psw).commit();
        in.putExtra("wifissid",wifi);
        in.putExtra("wifipsw",psw);
        startActivityForResult(in,100);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==101){
            setResult(101);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
