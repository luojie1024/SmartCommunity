package com.way.tabui.gokit;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.util.AirMesinfo;

import org.json.JSONException;
import org.kymjs.kjframe.ui.BindView;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SmartAirConditionActivity extends GosBaseActivity {
    /**
     * 开 关 0:开 1：关
     */
    private int[] OPCL = {327432, 262152};

    @BindView(id = R.id.bt_open_close, click = true)
    private Button btOpCl;

    /**
     * 模式 0：制冷 1：抽湿 2：送风 3：制热  4:自动
     */
    private int[] MOD = {327944, 328200, 328456, 328712, 327688};
    /**
     * 模式图片资源索引 0：制冷 1：抽湿 2：送风 3：制热  4:自动
     */
    private int[] imaMOD = {R.drawable.btn_mode_cold_black,
            R.drawable.btn_mode_humidity_black,
            R.drawable.btn_winddirect_black, R.drawable.btn_mode_hot_black, R.drawable.btn_mode_auto_black};
    /**
     * 模式文字资源索引 0：制冷 1：抽湿 2：送风 3：制热  4：自动
     */
    private String[] txMOD = {"制冷", "抽湿", "送风", "制热", "自动"};

    private Button btMod;

    /**
     * 减少温度
     */
    private Button btSub;
    /**
     * 增加温度
     */
    private Button btAdd;

    /**
     * 发送温度
     */
    private int sendtem = 393216; // 06 xx xx
    /**
     * 发送遥控类型
     */
    private int sendtype = 131072;// 02 xx xx

    /**
     * 初始化自动
     */
    private int INITAUTO = 11184648;// AA AA 08

    /**
     * 初始化结束
     */
    private int INITOVER = 13421576;// CC CC 08
    /**
     * 风速 0:自动 1：低速 2：中速 3：高速
     */
    private int[] WS = {458760, 459016, 459272, 459528};
    /**
     * 风速图片资源索引 0:自动 1：低速 2：中速 3：高速
     */
    private int[] imaWS = {R.drawable.ic_conditionor_windcapacity_aoto,
            R.drawable.ic_conditionor_windcapacity_low,
            R.drawable.ic_conditionor_windcapacity_middle,
            R.drawable.ic_conditionor_windcapacity};

    private String[] txWS = {"自动", "低速", "中速", "高速"};


    private Button btWS;

    /**
     * 风向 0:自动 1：手动
     */
    private int[] WD = {524296, 524552};
    /**
     * 风向图片资源索引 0:自动 1：手动
     */
    private int[] imaWD = {R.drawable.btn_windspread,
            R.drawable.btn_windspread_default};
    /**
     * 风向文字 0:自动 1：手动
     */
    private String[] txWD = {"自动风向", "手动风向"};

    private Button btWD;

    private TextView tvTem;

    private TextView tvMod;

    private ImageView imMod;

    private ImageView imWS;

    private TextView tvWS;

    private TextView tvWD;

    private ImageView imWD;

    private LinearLayout llTem;

    private LinearLayout llMod;

    private LinearLayout llWsd;

    private TextView tv_temsign;

    boolean isOpen = false;
    int modIndex = 0;
    int wsIndex = 0;
    int wdIndex = 0;

    /**
     * The GizWifiDevice device
     */
    private GizWifiDevice device;
    /**
     * The device statu.
     */
    private HashMap<String, Object> deviceStatu;
    /**
     * 空调命令
     */
    private static final String KEY_Sendair = "Send_aircon";

    private int opcl;
    private String name;
    private int brand;
    private int temperature;
    private int _id;

    private DatabaseAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_air_condition);
        initDevice();
        setActionBar(true, true, name);
        dbAdapter = new DatabaseAdapter(SmartAirConditionActivity.this);
        initView();
        initData();
        initEvent();
    }

    private void initView() {

        btAdd = (Button) findViewById(R.id.bt_add);
        btMod = (Button) findViewById(R.id.bt_mod);
        btOpCl = (Button) findViewById(R.id.bt_open_close);
        btSub = (Button) findViewById(R.id.bt_sub);
        btWD = (Button) findViewById(R.id.bt_wind_direction);
        btWS = (Button) findViewById(R.id.bt_wind_speed);

        tvMod = (TextView) findViewById(R.id.tv_mod);
        tvTem = (TextView) findViewById(R.id.tv_tem);
        tvWD = (TextView) findViewById(R.id.tv_wd);
        tvWS = (TextView) findViewById(R.id.tv_ws);
        tv_temsign = (TextView) findViewById(R.id.tv_temsign);

        imMod = (ImageView) findViewById(R.id.im_mod);
        imWD = (ImageView) findViewById(R.id.im_wd);
        imWS = (ImageView) findViewById(R.id.im_ws);

        llMod = (LinearLayout) findViewById(R.id.ll_mod);
        llTem = (LinearLayout) findViewById(R.id.ll_tem);
        llWsd = (LinearLayout) findViewById(R.id.ll_wsd);
    }

    String mac;

    private void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        mac = device.getMacAddress();
        deviceStatu = new HashMap<String, Object>();
        name = intent.getStringExtra("name");
        brand = intent.getIntExtra("brand", 0);
        temperature = intent.getIntExtra("temperature", 0);
        modIndex = intent.getIntExtra("mod", 0);
        wsIndex = intent.getIntExtra("speed", 0);
        wdIndex = intent.getIntExtra("direction", 0);
        opcl = intent.getIntExtra("opcl", 0);
        _id = intent.getIntExtra("id", 0);
    }

    private void initData() {
        try {
            sendJson(KEY_Sendair, sendtype + brand);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (opcl == 0) {
            llMod.setVisibility(View.VISIBLE);
            llTem.setVisibility(View.VISIBLE);
            llWsd.setVisibility(View.VISIBLE);
        } else {
            llMod.setVisibility(View.GONE);
            llTem.setVisibility(View.GONE);
            llWsd.setVisibility(View.GONE);
        }

        tvTem.setText("" + temperature);

        imMod.setBackgroundResource(imaMOD[modIndex]);
        tvMod.setText(txMOD[modIndex]);

        imWS.setBackgroundResource(imaWS[wsIndex]);
        tvWS.setText(txWS[wsIndex]);

        imWD.setBackgroundResource(imaWD[wdIndex]);
        tvWD.setText(txWD[wdIndex]);
    }

    private void initEvent() {
        btOpCl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                vSimple();
                if (opcl == 0) {
                    try {
                        sendJson(KEY_Sendair, OPCL[1]);
                        llMod.setVisibility(View.GONE);
                        llTem.setVisibility(View.GONE);
                        llWsd.setVisibility(View.GONE);
                        opcl = 1;
                        updbData();
                    } catch (JSONException e) {
                    }
                } else {
                    try {
                        sendJson(KEY_Sendair, OPCL[0]);
                        llMod.setVisibility(View.VISIBLE);
                        llTem.setVisibility(View.VISIBLE);
                        llWsd.setVisibility(View.VISIBLE);
                        opcl = 0;
                        updbData();
                    } catch (JSONException e) {
                    }
                }
            }
        });

        btSub.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vSimple();
                temperature = Integer.parseInt(tvTem.getText().toString());
                llMod.setVisibility(View.VISIBLE);
                llTem.setVisibility(View.VISIBLE);
                llWsd.setVisibility(View.VISIBLE);
                opcl = 0;
                if (temperature > 16) {
                    tvTem.setText("" + (temperature - 1));
                    temperature = Integer.parseInt(tvTem.getText().toString());
                    String temx = Integer.toHexString(temperature) + "08";
                    try {
                        sendtem = sendtem + Integer.valueOf(temx, 16);
                        sendJson(KEY_Sendair, sendtem);
                        sendtem = 393216;
                        updbData();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "发送失败",
                                Toast.LENGTH_SHORT).show();
                        sendtem = 393216;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请在16~30℃中选择温度",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vSimple();
                llMod.setVisibility(View.VISIBLE);
                llTem.setVisibility(View.VISIBLE);
                llWsd.setVisibility(View.VISIBLE);
                opcl = 0;
                temperature = Integer.parseInt(tvTem.getText().toString());

                if (temperature < 30) {
                    tvTem.setText("" + (temperature + 1));
                    temperature = Integer.parseInt(tvTem.getText().toString());
                    String temx = Integer.toHexString(temperature) + "08";
                    try {
                        sendtem = sendtem + Integer.valueOf(temx, 16);
                        sendJson(KEY_Sendair, sendtem);
                        sendtem = 393216;

                        updbData();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Toast.makeText(getApplicationContext(), "发送失败",
                                Toast.LENGTH_SHORT).show();
                        sendtem = 393216;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请在16~30℃中选择温度",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btMod.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vSimple();
                llMod.setVisibility(View.VISIBLE);
                llTem.setVisibility(View.VISIBLE);
                llWsd.setVisibility(View.VISIBLE);
                opcl = 0;
                modIndex++;
                if (modIndex >= 5)
                    modIndex = 0;

                try {
                    sendJson(KEY_Sendair, MOD[modIndex]);
                    imMod.setBackgroundResource(imaMOD[modIndex]);
                    tvMod.setText(txMOD[modIndex]);

                    if (modIndex == 2) {
                        llTem.setVisibility(View.INVISIBLE);
                        btAdd.setEnabled(false);
                        btSub.setEnabled(false);
                    } else {
                        llTem.setVisibility(View.VISIBLE);
                        btAdd.setEnabled(true);
                        btSub.setEnabled(true);
                    }

                    if (modIndex == 4 || modIndex == 1) {
                        btWS.setEnabled(false);
                    } else {
                        btWS.setEnabled(true);
                    }
                    updbData();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        btWS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vSimple();
                llMod.setVisibility(View.VISIBLE);
                llTem.setVisibility(View.VISIBLE);
                llWsd.setVisibility(View.VISIBLE);
                opcl = 0;
                wsIndex++;
                if (wsIndex >= 4)
                    wsIndex = 0;

                try {
                    sendJson(KEY_Sendair, WS[wsIndex]);
                    imWS.setBackgroundResource(imaWS[wsIndex]);
                    tvWS.setText(txWS[wsIndex]);
                    updbData();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btWD.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                vSimple();
                llMod.setVisibility(View.VISIBLE);
                llTem.setVisibility(View.VISIBLE);
                llWsd.setVisibility(View.VISIBLE);
                opcl = 0;
                wdIndex++;
                if (wdIndex >= 2)
                    wdIndex = 0;
                try {
                    sendJson(KEY_Sendair, WD[wdIndex]);
                    imWD.setBackgroundResource(imaWD[wdIndex]);
                    tvWD.setText(txWD[wdIndex]);
                    updbData();

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void vSimple() {
        Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    private void updbData() {
        AirMesinfo airMesinfo = new AirMesinfo(_id, name, brand, temperature, modIndex, wsIndex, wdIndex, mac, "Null", opcl);
        dbAdapter.updateAirmes(airMesinfo);
    }

    private void sendJson(String key, Object value) throws JSONException {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        device.write(hashMap, 0);
        Log.i("==", hashMap.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
