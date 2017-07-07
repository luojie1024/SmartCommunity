package com.way.tabui.gokit;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    private boolean auto_mod=false;

    private Button bt_aer_allopen,bt_aer_allclose,bt_aer_highopen,bt_aer_lowopen,bt_aer_mod;
    private byte[] ALL_OPEN={(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x01,
         (byte) 0x01, (byte) 0xbe, (byte) 0x42};

    private byte[] ALL_CLOSE= {(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x7e, (byte) 0x12};

    private byte[] HIGH_OPEN={(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x01,
         (byte) 0x00, (byte) 0x7f, (byte) 0x82};

    private byte[] LOW_OPEN= {(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x01, (byte) 0x00,
         (byte) 0x01, (byte) 0xbf, (byte) 0xd2};

    private byte[] AUTO_MODE={(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01, (byte) 0x00,
         (byte) 0x00, (byte) 0x43, (byte) 0xd2};

    private byte[] NORMAL_MODE= {(byte) 0x50, (byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01, (byte) 0x00,
         (byte) 0x01, (byte) 0x82, (byte) 0x12};

    private byte[] OPEN_DOOR = {(byte) 0x40,(byte) 0x5a, (byte) 0x1b, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x22,
            (byte) 0x14, (byte) 0x47, (byte) 0x06, (byte) 0x69, (byte) 0x01, (byte) 0x37, (byte) 0x35, (byte) 0x30,
            (byte) 0x39, (byte) 0x31, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xc3
    };
    private byte[] CLOSE_DOOR = {(byte) 0x40,(byte) 0x5a, (byte) 0x17, (byte) 0x02, (byte) 0x00, (byte) 0x00, (byte) 0x11, (byte) 0x22,
         (byte) 0x14, (byte) 0x47, (byte) 0x06, (byte) 0x69, (byte) 0x01, (byte) 0x37, (byte) 0x35, (byte) 0x30,
         (byte) 0x39, (byte) 0x31, (byte) 0x38, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xc1
    };
    private byte[] UP_STATUS = {(byte) 0x50,(byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x44, (byte) 0x3F};


    private Handler handler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_door);
        setActionBar(true, true, getResources().getString(R.string.title_activity_smart_door));
        initDevice();
        initView();
        initHandler();

    }

    /**
     * description:定时发送状态同步信息
     * auther：joahluo
     * time：2017/7/7 10:54
     */
    private void initHandler() {
        //每10秒执行一次runnable
        handler.postDelayed(runnable, 100);
    }

    private void initView() {
        ib_unclock = (ImageButton) findViewById(R.id.ib_unclock);
        et_kuozhancode=(EditText)findViewById(R.id.et_kuozhancode);
        btn_sendcode=(Button)findViewById(R.id.btn_sendcode);
        bt_aer_allopen=(Button)findViewById(R.id.bt_aer_allopen);
        bt_aer_allclose=(Button)findViewById(R.id.bt_aer_allclose);
        bt_aer_highopen=(Button)findViewById(R.id.bt_aer_highopen);
        bt_aer_lowopen=(Button)findViewById(R.id.bt_aer_lowopen);
        bt_aer_mod=(Button)findViewById(R.id.bt_aer_mod);
        bt_aer_allopen.setOnClickListener(allopenClick);
        bt_aer_allclose.setOnClickListener(allcloseClick);
        bt_aer_highopen.setOnClickListener(highopenClick);
        bt_aer_lowopen.setOnClickListener(lowopenClick);
        bt_aer_mod.setOnClickListener(modClick);
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
     * description:控制点击事件
     * auther：joahluo
     * time：2017/6/25 14:15
     */
    //全开
    private View.OnClickListener allopenClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            simpe();
            try {
                sendJson(KUOZHAN,ALL_OPEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //全关
    private View.OnClickListener allcloseClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            simpe();
            try {
                sendJson(KUOZHAN,ALL_CLOSE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //高开
    private View.OnClickListener highopenClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            simpe();
            try {
                sendJson(KUOZHAN,HIGH_OPEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //低开
    private View.OnClickListener lowopenClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            simpe();
            try {
                sendJson(KUOZHAN,LOW_OPEN);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //模式改变
    private View.OnClickListener modClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            simpe();
            try {
                //当前手动模式》---切换---》自动模式
                if (auto_mod == false) {
                    sendJson(KUOZHAN, AUTO_MODE);
                    auto_mod = true;
                    bt_aer_mod.setText("自动模式");
                    ToastUtil.ToastShow(SmartDoorActivity.this,"自动模式！");
                    bt_aer_allopen.setVisibility(View.INVISIBLE);
                    bt_aer_allclose.setVisibility(View.INVISIBLE);
                    bt_aer_highopen.setVisibility(View.INVISIBLE);
                    bt_aer_lowopen.setVisibility(View.INVISIBLE);
                } else {
                    sendJson(KUOZHAN, NORMAL_MODE);
                    auto_mod = false;
                    bt_aer_mod.setText("手动模式");
                    ToastUtil.ToastShow(SmartDoorActivity.this,"手动模式！");
                    bt_aer_allopen.setVisibility(View.VISIBLE);
                    bt_aer_allclose.setVisibility(View.VISIBLE);
                    bt_aer_highopen.setVisibility(View.VISIBLE);
                    bt_aer_lowopen.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 2017/6/21
     * 发送按钮点击事件
     * @param view
     */
    public void sendClick(View view) {
        simpe();
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

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    //发送事件
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            //要做的事情
            try {
                sendJson(KUOZHAN, UP_STATUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //每个10秒再次发送
            handler.postDelayed(this, 10000);
        }
    };

}
