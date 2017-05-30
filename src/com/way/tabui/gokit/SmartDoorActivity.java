package com.way.tabui.gokit;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.way.tabui.commonmodule.GosBaseActivity;

import org.json.JSONException;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class SmartDoorActivity extends GosBaseActivity {

    boolean isunlock = false;
    private ImageButton ib_unclock;
    public GizWifiDevice device;
    private HashMap<String, Object> deviceStatu;
    private static final String KUOZHAN = "kuozhan";

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
