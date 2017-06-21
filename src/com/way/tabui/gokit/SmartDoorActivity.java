package com.way.tabui.gokit;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.ConvertUtil;
import com.way.util.ToastUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SmartDoorActivity extends GosBaseActivity {

    boolean isunlock = false;
    private ImageButton ib_unclock;
    public GizWifiDevice device;
    private HashMap<String, Object> deviceStatu;
    private static final String KUOZHAN = "kuozhan";
    private EditText et_kuozhancode;
    private Button btn_sendcode;

    private byte[] OPEN_DOOR = {(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef
    };
    private byte[] CLOSE_DOOR = {(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67,(byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_door);
        setActionBar(true, true, getResources().getString(R.string.title_activity_smart_door));
        initDevice();
        initView();

    }

    private void initView() {
        ib_unclock = (ImageButton) findViewById(R.id.ib_unclock);
        et_kuozhancode=(EditText)findViewById(R.id.et_kuozhancode);
        btn_sendcode=(Button)findViewById(R.id.btn_sendcode);
    }

    public void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        deviceStatu = new HashMap<String, Object>();

    }

    private void sendJson(String key, Object value) throws JSONException {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        device.write(hashMap, 0);
        Log.i("==", hashMap.toString());
    }

    public void simpe() {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(20);
    }

    /**
     * 2017/6/21
     * 发送按钮点击事件
     * @param view
     */
    public void sendClick(View view) {
        String code = et_kuozhancode.getText().toString();
        //如果获得的代码为出示代码，则弹出提示信息
        if (code.length()==0) {
            ToastUtil.ToastShow(this,"请输入代码！");
        } else {
            byte[] bytes_code = ConvertUtil.hexStringToByte(code);
            try {
                sendJson(KUOZHAN,bytes_code);
                ToastUtil.ToastShow(this,"发送成功！");
            } catch (JSONException e) {
                e.printStackTrace();
                ToastUtil.ToastShow(this,"发送失败！");
            }
        }
    }


    public void doorClick(View view) {
        simpe();
        // ToastUtil.ToastShow(this,"功能正在开发中...");
        if (isunlock) {
            isunlock = false;
            try {
                sendJson(KUOZHAN, CLOSE_DOOR);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ib_unclock.setImageResource(R.drawable.unlock_normal);
        } else {
            isunlock = true;
            try {
                sendJson(KUOZHAN, OPEN_DOOR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ib_unclock.setImageResource(R.drawable.unlocked_normal);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
