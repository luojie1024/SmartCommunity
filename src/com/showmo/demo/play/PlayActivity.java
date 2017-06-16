package com.showmo.demo.play;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.showmo.demo.util.spUtil;
import com.way.adapter.SmartSwitchListAdapter;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.model.XmStreamMode;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmRealplayCameraCtrl;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.IXmTalkManager;
import com.xmcamera.core.sysInterface.IXmTalkManager.TalkState;
import com.xmcamera.core.sysInterface.OnXmBeginTalkListener;
import com.xmcamera.core.sysInterface.OnXmEndTalkListener;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;
import com.xmcamera.core.sysInterface.OnXmStartResultListener;
import com.xmcamera.core.sysInterface.OnXmTalkVolumListener;
import com.xmcamera.core.view.decoderView.XmGlView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/6/20.
 */
public class PlayActivity extends GosBaseActivity implements View.OnClickListener {

    IXmSystem xmSystem;
    IXmRealplayCameraCtrl realplayCameraCtrl;
    int cameraid;

    FrameLayout playContent;
    XmGlView glView;

    Button play, stop, HD, SD, AT, capture, Record, StopRecord, btn_rebinder,
            SLrecord, SLrecordclose;
    private Button btn_open, btn_colse, btn_stop, btn_redic;
   // TextView show;
    String logtext = "";

    spUtil sp;
    private GizWifiDevice device;
    /**
     * The device statu.
     */
    private HashMap<String, Object> deviceStatu;

    /**
     * 传输字符
     */
    private static final String KEY_Sendcom = "Send_com";


    protected static final int OPEN = 1;
    protected static final int CLOSE = 0;
    /**
     * 控制指令关键字
     */
    private static final String KEY_Sendair = "Send_aircon";

    /**
     * 指令代码0:开 1：停止  2：关 3：换向
     */
    private int[] con_mes = {14748160, 14748416, 14748672, 14749952};

    int playId;
    IXmTalkManager talkma;
    private ListView list_oc;
    private MyReceiver receiver = null;
    private SmartSwitchListAdapter adapter;
    private LinearLayout ll_curtain;
    private LinearLayout ll_oc_list;
    private LinearLayout ll_show;
    private boolean isshow = false;
    private ScrollView scrollView;
    private TextView tv_show;
    private TextView tx_oc;
    private TextView tx_curtain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_showmo);
        init();
        initReceiver();
        sendbroadcast();
        initEvent();


    }

    private void init() {
        cameraid = getIntent().getExtras().getInt("cameraId");
        xmSystem = XmSystem.getInstance();
        realplayCameraCtrl = xmSystem.xmGetRealplayController();

        talkma = xmSystem.xmGetTalkManager(cameraid);

        glView = new XmGlView(this, null);
        playContent = (FrameLayout) findViewById(R.id.glview);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels - 10;
        params.height = dm.widthPixels - 10;
        playContent.setLayoutParams(params);
        playContent.addView((View) glView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        scrollView = (ScrollView) findViewById(R.id.scrollview);

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        HD = (Button) findViewById(R.id.HD);
        SD = (Button) findViewById(R.id.SD);
        AT = (Button) findViewById(R.id.AT);
        capture = (Button) findViewById(R.id.capture);
        Record = (Button) findViewById(R.id.Record);
        StopRecord = (Button) findViewById(R.id.StopRecord);
        btn_rebinder = (Button) findViewById(R.id.btn_rebinder);
        SLrecord = (Button) findViewById(R.id.SLrecord);
        SLrecordclose = (Button) findViewById(R.id.SLrecordclose);
        ll_show = (LinearLayout) findViewById(R.id.ll_show);
        tv_show = (TextView) findViewById(R.id.tv_show);

        tx_oc = (TextView) findViewById(R.id.tx_oc);
        tx_curtain = (TextView) findViewById(R.id.tx_curtain);
        tx_curtain.setVisibility(View.GONE);
        tx_oc.setVisibility(View.GONE);

        ll_curtain = (LinearLayout) findViewById(R.id.ll_curtain);
        ll_curtain.setVisibility(View.GONE);
        ll_oc_list = (LinearLayout) findViewById(R.id.ll_oc_list);
        ll_oc_list.setVisibility(View.GONE);
        list_oc = (ListView) findViewById(R.id.list_oc);
        adapter = new SmartSwitchListAdapter(mHander, this);

        list_oc.setAdapter(adapter);
        btn_open = (Button) findViewById(R.id.btn_open);
        btn_colse = (Button) findViewById(R.id.btn_colse);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_redic = (Button) findViewById(R.id.btn_redic);
        ll_show.setVisibility(View.GONE);
        ll_show.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        HD.setOnClickListener(this);
        SD.setOnClickListener(this);
        AT.setOnClickListener(this);
        capture.setOnClickListener(this);
        Record.setOnClickListener(this);
        StopRecord.setOnClickListener(this);
        btn_rebinder.setOnClickListener(this);
        SLrecord.setOnClickListener(this);
        SLrecordclose.setOnClickListener(this);
        //show = (TextView) findViewById(R.id.show);

//        mHander.sendEmptyMessage(0x126);
        sp = new spUtil(this);
    }

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.way.tabui.actity.GosDeviceListActivityReceviver");
        filter.addAction("com.way.tabui.actity.GizServiceTOAST");
        registerReceiver(receiver, filter);
    }

    private void initEvent() {
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    sendJson(KEY_Sendair, con_mes[0]);
                  //  btn_open.setTextColor(getResources().getColor(R.color.golden));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                try {
                    sendJson(KEY_Sendair, con_mes[1]);

           //         btn_stop.setTextColor(getResources().getColor(R.color.golden));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_colse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    sendJson(KEY_Sendair, con_mes[2]);
              //      btn_colse.setTextColor(getResources().getColor(R.color.golden));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_redic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    sendJson(KEY_Sendair, con_mes[3]);
                 //   btn_redic.setTextColor(getResources().getColor(R.color.golden));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "发送失败",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int index = 0, len = listAdapter.getCount(); index < len; index++) {
            View listViewItem = listAdapter.getView(index , null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    private void sendJson(String key, Object value) throws JSONException {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        device.write(hashMap, 0);
        Log.i("==", hashMap.toString());
    }

    private void Play() {
        if (realplayCameraCtrl.isPlaying()) {
            mHander.sendEmptyMessage(0x128);
            return;
        }
        realplayCameraCtrl.xmStart(glView, cameraid,
                new OnXmStartResultListener() {
                    @Override
                    public void onStartSuc(boolean isLocalNet, int cameraId,
                                           int var3) {
                        playId = var3;
                        showTV("播放成功！");
                    }

                    @Override
                    public void onStartErr(XmErrInfo errcode) {
//                        showTV("errId:" + errcode.errId + ",errCode:"
//                                + errcode.errCode + ",errdiscribe:"
//                                + errcode.discribe);
                        showTV("播放失败");
                    }
                });
    }

    private void slrecordclose() {
        talkma.xmEndTalk(new OnXmEndTalkListener() {

            @Override
            public void onTalkClosing() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onSuc() {
                // TODO Auto-generated method stub
                mHander.sendEmptyMessage(0x132);
                // Toast.makeText(getApplicationContext(), "关闭对讲成功",
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseTalkErr(XmErrInfo arg0) {
                // TODO Auto-generated method stub
                mHander.sendEmptyMessage(0x130);
            }

            @Override
            public void onAlreadyClosed() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void slrecord() {
        talkma.xmBeginTalk(new OnXmBeginTalkListener() {

            @Override
            public void onSuc() {
                // TODO Auto-generated method stub
                mHander.sendEmptyMessage(0x131);
            }

            @Override
            public void onOpenTalkErr(XmErrInfo arg0) {
                // TODO Auto-generated method stub
                mHander.sendEmptyMessage(0x129);
            }

            @Override
            public void onNoRecordPermission() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onIPCIsTalking() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAlreadyTalking() {
                // TODO Auto-generated method stub

            }
        }, new OnXmTalkVolumListener() {

            @Override
            public void onVolumeChange(int arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void Stop() {
        if (realplayCameraCtrl.isPlaying()) {
            realplayCameraCtrl.xmStop(playId);
            showTV("停止播放！");
        }
    }

    private void SwitchStream(final XmStreamMode mode) {
        realplayCameraCtrl.xmSwitchStream(mode, new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                showTV("切换失败！" + info.discribe);
            }

            @Override
            public void onSuc() {
                if (mode == XmStreamMode.ModeHd) {
                    showTV("切换到HD");
                } else if (mode == XmStreamMode.ModeFluency) {
                    showTV("切换到SD");
                } else if (mode == XmStreamMode.ModeAdapter) {
                    showTV("切换到AT");
                }
            }
        });
    }

    private void Capture() {
        final long time = System.currentTimeMillis();
        realplayCameraCtrl.xmCapture("/sdcard/zzj/", "p" + time + ".jpg",
                new OnXmListener<String>() {
                    @Override
                    public void onErr(XmErrInfo info) {
                        showTV("截图失败");
                    }

                    @Override
                    public void onSuc(String info) {
                        showTV("截图成功1:" + "/sdcard/zzj/" + "p" + time + ".jpg");
                        realplayCameraCtrl.xmThumbnail("/sdcard/zzj", "thumb"
                                        + time + ".jpg", "p" + time + ".jpg",
                                new OnXmListener<String>() {
                                    @Override
                                    public void onErr(XmErrInfo info) {
                                        showTV("截图失败2");
                                    }

                                    @Override
                                    public void onSuc(String info) {
                                        showTV("截图成功2:" + "/sdcard/zzj/"
                                                + "thumb" + time + ".jpg");
                                    }
                                });
                    }
                });
    }

    boolean isRecord = false;

    private void Record() {
        isRecord = true;
        long time = System.currentTimeMillis();
        boolean suc = realplayCameraCtrl.xmRecord("/sdcard/zzj", "v" + time
                + ".mp4");
        Toast.makeText(this, suc ? "开始录像" : "录像失败", Toast.LENGTH_LONG).show();
        showTV(suc ? "开始录像" : "录像失败");
    }

    private void StopRecord() {
        if (!isRecord) {
            return;
        }
        isRecord = false;
        String ss = realplayCameraCtrl.xmStopRecord();
        Toast.makeText(this, ss == null ? "录像失败" : "录像成功：" + ss,
                Toast.LENGTH_LONG).show();
        showTV(ss == null ? "录像失败" : "录像成功：" + ss);
    }

    private void btn_rebinder() {
        new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("您确定要删除此摄像机吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteDevice();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void DeleteDevice() {
        xmSystem.xmDeleteDevice(cameraid, xmSystem.xmFindDevice(cameraid)
                .getmUuid(), new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                mHander.sendEmptyMessage(0x124);
                showTV("删除失败！" + info.discribe);
            }

            @Override
            public void onSuc() {
                mHander.sendEmptyMessage(0x123);
            }
        });
    }

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int position;
            ArrayList<Gizinfo> gizinfos;
            switch (msg.what) {
                case 0x123:
                    Toast.makeText(PlayActivity.this, "删除成功！", Toast.LENGTH_LONG)
                            .show();
                    setResult(101);
                    finish();
                    break;
                case 0x124:
                    Toast.makeText(PlayActivity.this, "删除失败！", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x125:
                    Toast.makeText(PlayActivity.this, (String) msg.obj,
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x126:
                    Toast.makeText(PlayActivity.this, logtext, Toast.LENGTH_LONG)
                            .show();
//                    show.setText(logtext);
                    break;
                case 0x127:
                    Toast.makeText(PlayActivity.this, "你没有权限，请先注册登录~",
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x128:
                    Toast.makeText(PlayActivity.this, "视频已经在播放中！",
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x129:
                    Toast.makeText(PlayActivity.this, "开启错误", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x130:
                    Toast.makeText(PlayActivity.this, "关闭错误", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x131:
                    Toast.makeText(PlayActivity.this, "已开启对讲", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 0x132:
                    Toast.makeText(PlayActivity.this, "已关闭对讲", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }

    };

    private void sendbroadcast() {
        Intent intent = new Intent();
        intent.setAction("com.way.tabui.actity.GosDeviceListActivity");
        sendBroadcast(intent);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Message msg = new Message();
            if (action.equals("com.way.tabui.actity.GosDeviceListActivityReceviver")) {
                device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
                adapter.setDate(device.getMacAddress());
                fixListViewHeight(list_oc);
                ll_show.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean isPlay() {
        if (!realplayCameraCtrl.isPlaying()) {
            showTV("视频未开启！");
        }
        return realplayCameraCtrl.isPlaying();
    }

    private void showTV(String ss) {
        logtext =  ss;
        mHander.sendEmptyMessage(0x126);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                Play();
                break;
            case R.id.stop:
                Stop();
                break;
            case R.id.HD:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeHd);
                break;
            case R.id.SD:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeFluency);
                break;
            case R.id.AT:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeAdapter);
                break;
            case R.id.capture:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                Capture();
                break;
            case R.id.Record:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                Record();
                break;
            case R.id.StopRecord:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                StopRecord();
                break;
            case R.id.btn_rebinder:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                btn_rebinder();
                break;
            case R.id.SLrecord:
                if (talkma.getCurState() == TalkState.NoOpen) {
                    slrecord();
                } else {
                    mHander.sendEmptyMessage(0x129);
                }

                break;
            case R.id.SLrecordclose:
                if (talkma.getCurState() == TalkState.Opened) {
                    slrecordclose();
                } else {
                    mHander.sendEmptyMessage(0x130);
                }

                break;

            case R.id.ll_show:
                if (isshow) {
                    isshow = false;
                    tv_show.setText("-打开实时演示板-");
                    if (adapter.getmList().size()!=0)
                    tx_curtain.setVisibility(View.GONE);
                    tx_oc.setVisibility(View.GONE);
                    ll_oc_list.setVisibility(View.GONE);
                    ll_curtain.setVisibility(View.GONE);
                    //scrollView.scrollTo(50,0);
                } else {
                    isshow = true;
                    tv_show.setText("-关闭实时演示板-");
                    tx_curtain.setVisibility(View.VISIBLE);
                    if (adapter.getmList().size()!=0){
                    tx_oc.setVisibility(View.VISIBLE);
                    }
                    ll_oc_list.setVisibility(View.VISIBLE);
                    ll_curtain.setVisibility(View.VISIBLE);
                   // scrollView.scrollTo(50,0);
                }
                break;

        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Stop();
        }
        return super.onKeyDown(keyCode, event);
    }
}
