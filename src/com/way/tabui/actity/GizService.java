package com.way.tabui.actity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizPushType;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;
import com.way.tabui.pushmodule.GosPushManager;
import com.way.util.Alertinfo;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class GizService extends Service {

    /**
     * The wifiHardVerKey
     */
    private static final String wifiHardVerKey = "wifiHardVersion";

    /**
     * The wifiSoftVerKey
     */
    private static final String wifiSoftVerKey = "wifiSoftVersion";

    /**
     * The mcuHardVerKey
     */
    private static final String mcuHardVerKey = "mcuHardVersion";

    /**
     * The mcuSoftVerKey
     */
    private static final String mcuSoftVerKey = "mcuSoftVersion";

    /**
     * The wifiFirmwareIdKey
     */
    private static final String FirmwareIdKey = "wifiFirmwareId";

    /**
     * The wifiFirmwareVerKey
     */
    private static final String FirmwareVerKey = "wifiFirmwareVer";

    /**
     * The productKey
     */
    private static final String productKey = "productKey";
    // HashMap<String, Object> deviceStatu;
    /**
     * The isUpDateUi
     */
    protected static boolean isUpDateUi = true;
    // private GizWifiDevice device;

    private static final String KEY_Gate = "gate1";
    private static final String KEY_Smoke = "smoke1";
    private static final String KEY_Gas = "gas1";
    private static final String KEY_Body = "body1";
    /**
     * led红灯开关 0=关 1=开.
     */
    private static final String KEY_RED_SWITCH = "LED_OnOff";// LED_OnOff
    /**
     * 指定led颜色值 0=自定义 1=黄色 2=紫色 3=粉色.
     */
    private static final String KEY_LIGHT_COLOR = "LED_Color";// LED_Color
    /**
     * led灯红色值 0-254.
     */
    private static final String KEY_LIGHT_RED = "LED_R";// LED_R
    /**
     * led灯绿色值 0-254.
     */
    private static final String KEY_LIGHT_GREEN = "LED_G";// LED_G
    /**
     * led灯蓝色值 0-254.
     */
    private static final String KEY_LIGHT_BLUE = "LED_B";
    /**
     * 电机转速 －5～－1 电机负转 0 停止 1～5 电机正转.
     */
    private static final String KEY_SPEED = "Motor_Speed";
    /**
     * 红外探测 0无障碍 1有障碍.
     */
    private static final String KEY_INFRARED = "Infrared";
    /**
     * 环境温度.
     */
    private static final String KEY_TEMPLATE = "Temperature";
    /**
     * 环境湿度.
     */
    private static final String KEY_HUMIDITY = "Humidity";
    /**
     * The Constant TOAST.
     */
    protected static final int TOAST = 0;

    /**
     * The Constant SETNULL.
     */
    protected static final int SETNULL = 1;

    /**
     * The Constant UPDATE_UI.
     */
    protected static final int UPDATE_UI = 2;

    /**
     * The Constant LOG.
     */
    protected static final int LOG = 3;

    /**
     * The Constant RESP.
     */
    protected static final int RESP = 4;

    /**
     * The Constant HARDWARE.
     */
    protected static final int HARDWARE = 5;

    /**
     * The Disconnect
     */
    protected static final int DISCONNECT = 6;
    /**
     * 各项数据点
     */
    protected boolean gasstua = false;
    protected boolean smokestua = false;
    protected boolean gatestua = false;
    protected boolean bodystua = false;
    protected boolean lastgasstua = false;
    protected boolean lastsmokestua = false;
    protected boolean lastgatestua = false;
    protected boolean lastbodystua = false;
    protected String temperature;
    protected String humidity;
    protected Intent data;
    protected InitThread thread;
    //HashMap<String, Object>
    private ConcurrentHashMap<String, Object> deviceStatu;
    DatabaseAdapter dbAdapter;
    private String bindgiz;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        super.onCreate();

    }

    private GizWifiDevice device;
    private MyReceiver receiver = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        data = intent;
        dbAdapter = new DatabaseAdapter(this);
        // 以线程方式启动设备的链接
        thread = new InitThread();
        thread.start();
        thread.interrupt();
        // 定义一个receiver用来接收相关广播
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.way.tabui.actity.GosDeviceListActivity");
        filter.addAction("com.way.tabui.actity.MainActivity");
        filter.addAction("com.way.tabui.actity.GosDeviceControlActivity");
        registerReceiver(receiver, filter);
        return START_REDELIVER_INTENT;
        // flags=START_REDELIVER_INTENT;
        // return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // 释放监听者和广播接收器
        device.setListener(null);
        device.setSubscribe(false);
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private class InitThread extends Thread {
        @Override
        public void run() {
            initsdk();
            initDevice(data);
        }
    }

    // 初始化储存器和SDK，保证运行
    public SharedPreferences spf;
    GosPushManager gosPushManager;

    private void initsdk() {
        spf = getSharedPreferences(GosConstant.SPF_Name, Context.MODE_PRIVATE);
        try {
//			GosConstant.App_ID = spf.getString("appid",
//					"a61ed92da3764cca848f3dbab8481149");
//			GosConstant.App_Screct = spf.getString("appscrect",
//					"57c13265403549ac83d828e50639c37a");
//			GosConstant.device_ProductKey = spf.getString("prroductkey",
//					"330b43e5cd9b4aa9a03fc97c5f6f52a4");
//            GosConstant.device_ProductKey="69353614e549438ead162509abefd243";
//            GosConstant.App_ID = "84ec3257a927470e97c7d66ff0558dc8";
//            GosConstant.App_Screct="beaca00e79a546f3b393ffdc81fdef72";
            // 启动SDK
            GizWifiSDK.sharedInstance().startWithAppID(getApplicationContext(), GosConstant.App_ID,
                    GosConstant.App_Screct, null, null, false);
            // 只能选择支持其中一种
            gosPushManager = new GosPushManager(GizPushType.GizPushJiGuang,
                    this);// 极光推送
        } catch (Exception e) {
            // TODO: handle exception

        }

    }

    // 设备监听者及相关回调
    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {
        /** 用于设备订阅 */
        public void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1,
                                    boolean arg2) {
            GizService.this.didSetSubscribe(arg0, arg1, arg2);
        }

        /** 用于设备状态 */
        public void didReceiveData(GizWifiErrorCode arg0, GizWifiDevice arg1,
                                   ConcurrentHashMap<String, Object> arg2, int arg3) {
            GizService.this.didReceiveData(arg0, arg1, arg2, arg3);
        }

        /** 用于设备硬件信息 */
        public void didGetHardwareInfo(GizWifiErrorCode arg0,
                                       GizWifiDevice arg1, ConcurrentHashMap<String, String> arg2) {
            GizService.this.didGetHardwareInfo(arg0, arg1, arg2);
        }

        /** 用于修改设备信息 */
        public void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
            GizService.this.didSetCustomInfo(arg0, arg1);
        }

        /** 用于设备状态变化 */
        public void didUpdateNetStatus(GizWifiDevice arg0,
                                       GizWifiDeviceNetStatus arg1) {
            GizService.this.didUpdateNetStatus(arg0, arg1);
        }
    };

    private void initDevice(Intent intent) {

        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        Log.i("==", "device:" + device.getMacAddress());
        deviceStatu = new ConcurrentHashMap<String, Object>();
        device.setListener(gizWifiDeviceListener);
        Log.i("==", "Listener---OK");
        device.setSubscribe(true);
        device.getDeviceStatus();
        senddevice();
        bindgiz = device.getMacAddress();

    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("com.way.tabui.actity.GosDeviceListActivity")) {
                // 回复已经在后台所连接的设备信息
                senddevice();
            }
            if (action.equals("com.way.tabui.actity.MainActivity")) {
                // 发送后台目前的数据
                sendbcmes();
            }
            if (action.equals("com.way.tabui.actity.GosDeviceControlActivity")) {
                // 回复已经在后台所连接的设备信息
                senddevice();
                // 发送后台目前的数据
                sendbcmes();

            }

        }
    }

    int hum;

    String color, blue, red, green, speed, air_con;
    boolean isred, isInfrared;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case UPDATE_UI:
                    isUpDateUi = true;
                    deviceStatu = (ConcurrentHashMap<String, Object>) msg.obj;
                    if (deviceStatu != null) {
                        for (String dataKey : deviceStatu.keySet()) {
                            if (dataKey.equals(KEY_RED_SWITCH)) {
                                isred = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_LIGHT_COLOR)) {
                                color = deviceStatu.get(dataKey).toString();
                                spf.edit()
                                        .putInt("COLOR",
                                                Integer.parseInt(color)).commit();
                            }

                            if (dataKey.equals(KEY_LIGHT_RED)) {


                                red = deviceStatu.get(dataKey).toString();
                            }
                            if (dataKey.equals(KEY_LIGHT_GREEN)) {
                                green = deviceStatu.get(dataKey).toString();
                            }
                            if (dataKey.equals(KEY_LIGHT_BLUE)) {

                                blue = deviceStatu.get(dataKey).toString();
                            }
                            if (dataKey.equals(KEY_SPEED)) {
                                speed = deviceStatu.get(dataKey).toString();
                            }
                            if (dataKey.equals(KEY_INFRARED)) {
                                isInfrared = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_Gate)) {
                                gatestua = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_Smoke)) {
                                smokestua = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_Gas)) {
                                gasstua = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_Body)) {
                                bodystua = (Boolean) deviceStatu.get(dataKey);
                            }
                            if (dataKey.equals(KEY_TEMPLATE)) {

                                temperature = deviceStatu.get(dataKey).toString();
                            }
                            if (dataKey.equals(KEY_HUMIDITY)) {
                                humidity = String.valueOf(100 - Integer.parseInt(deviceStatu
                                        .get(dataKey).toString()));
                            }
                        }
                    }
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "yyyy年MM月dd日 HH:mm:ss ");
                        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                        String str = formatter.format(curDate);

                        if (spf.getBoolean("issafe", true)) {
                            if (lastgasstua != gasstua) {
                                lastgasstua = gasstua;
                                if (lastgasstua) {
                                    Alertinfo alertinfo = new Alertinfo("燃气报警器", str,
                                            bindgiz, "null", 0);
                                    dbAdapter.addalert(alertinfo);
                                    notifbulid("燃气报警器发出警报!!", R.drawable.ic_gas_a, 0x010,
                                            "燃气");
                                }
                            }
                            if (lastgatestua != gatestua) {
                                lastgatestua = gatestua;
                                if (lastgatestua) {
                                    Alertinfo alertinfo = new Alertinfo("门磁", str, bindgiz,
                                            "null", 0);
                                    dbAdapter.addalert(alertinfo);
                                    notifbulid("门磁触发了!!", R.drawable.ic_gate_a, 0x011, "门磁");
                                }
                            }

                            if (lastbodystua != bodystua) {
                                lastbodystua = bodystua;
                                if (lastbodystua) {
                                    Alertinfo alertinfo = new Alertinfo("人体红外", str,
                                            bindgiz, "null", 0);
                                    dbAdapter.addalert(alertinfo);
                                    notifbulid("人体红外移动监测到有人经过!!", R.drawable.ic_body_a,
                                            0x012, "人体移动");
                                }
                            }


                            if (lastsmokestua != smokestua) {
                                lastsmokestua = smokestua;
                                if (lastsmokestua) {
                                    Alertinfo alertinfo = new Alertinfo("烟雾报警器", str,
                                            bindgiz, "null", 0);
                                    dbAdapter.addalert(alertinfo);
                                    notifbulid("烟雾报警器发出警报!!", R.drawable.ic_smoke_a, 0x013,
                                            "烟雾");
                                }
                            }
                        } else {
                            gasstua = false;
                            smokestua = false;
                            gatestua = false;
                            bodystua = false;
                            lastgasstua = false;
                            lastsmokestua = false;
                            lastgatestua = false;
                            lastbodystua = false;
                        }
                    sendbcmes();
                    break;
                case RESP:
                    //String data = msg.obj.toString();
                    try {
                        showDataInUI(msg.obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case LOG:
                    String datal = msg.obj.toString();
                    Intent intentlog = new Intent();
                    intentlog.putExtra("Logdata", datal);
                    intentlog.setAction("com.way.tabui.actity.GizServiceLOG");
                    sendBroadcast(intentlog);
//				StringBuilder sb = new StringBuilder();
//				JSONObject jsonObject;
//				int logText = 1;
//				try {
//					jsonObject = new JSONObject(msg.obj.toString());
//					for (int i = 0; i < jsonObject.length(); i++) {
//						if (jsonObject.getBoolean(jsonObject.names().getString(
//								i)) != false) {
//							sb.append(jsonObject.names().getString(i) + " "
//									+ logText + "\r\n");
//						}
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}

                    break;
                case TOAST:

                    String datat = msg.obj.toString();
                    Intent intenttoast = new Intent();
                    intenttoast.putExtra("Toastdata", datat);
                    intenttoast.setAction("com.way.tabui.actity.GizServiceTOAST");
                    sendBroadcast(intenttoast);

                    break;
                case HARDWARE:
                    String datah = msg.obj.toString();
                    Intent intenthardware = new Intent();
                    intenthardware.putExtra("MCUversion", MCUversion);
                    intenthardware.putExtra("Hardwaredata", datah);
                    intenthardware.setAction("com.way.tabui.actity.GizServiceHARDWARE");
                    sendBroadcast(intenthardware);
                    break;

                case DISCONNECT:
                    notifbulid("设备连接断开，尝试重启中...", R.drawable.ic_launcher, 0x020,
                            "网关设备");
                    reStart();
                    break;
            }
        }
    };

    private void reStart() {
        Intent reintent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(
                        getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, reintent,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(),
                restartIntent);

        Intent intentexit = new Intent();
        intentexit.setAction("com.way.util.exit_app");
        sendBroadcast(intentexit);
        GizService.this.onDestroy();
    }

    private void sendbcmes() {
        Intent intent = new Intent();
//		intent.putExtra("aircon", air_con);
        intent.putExtra("gasstua", gasstua);
        intent.putExtra("gatestua", gatestua);
        intent.putExtra("bodystua", bodystua);
        intent.putExtra("smokestua", smokestua);
        intent.putExtra("temperature", temperature);
        intent.putExtra("humidity", humidity);
        intent.putExtra("isred", isred);
        intent.putExtra("isInfrared", isInfrared);
        intent.putExtra("color", color);
        intent.putExtra("red", red);
        intent.putExtra("green", green);
        intent.putExtra("blue", blue);
        intent.putExtra("speed", speed);
        intent.setAction("com.way.tabui.actity.GizService");
        sendBroadcast(intent);
    }

    private void senddevice() {
        Intent rintent = new Intent();
        rintent.setAction("com.way.tabui.actity.GosDeviceListActivityReceviver");
        Bundle bundle = new Bundle();
        bundle.putParcelable("GizWifiDevice", (GizWifiDevice) device);
        rintent.putExtras(bundle);
        sendBroadcast(rintent);
    }

    private void notifbulid(String mes, int icon, int id, String mestit) {
        if (spf.getBoolean("issafe", true)) {
            NotificationCompat.Builder bulider = new NotificationCompat.Builder(
                    this);
            bulider.setSmallIcon(icon);
            bulider.setContentTitle("接收到" + mestit + "报警消息");
            bulider.setTicker("接到环泰智能家居提示消息");
            bulider.setDefaults(Notification.DEFAULT_ALL);
            bulider.setAutoCancel(true);
            bulider.setContentText(mes);
            if (id != 0x020) {
//            Intent intentexit = new Intent();
//            intentexit.setAction("com.way.util.exit_app");
//            sendBroadcast(intentexit);
                Intent intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("GizWifiDevice", device);
                intent.putExtras(bundle);
                intent.putExtra("isoffline", false);
                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                bulider.setContentIntent(pi);
            }
            Notification notification = bulider.build();
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(id, notification);
        }
    }

    @SuppressWarnings("rawtypes")
    private void showDataInUI(Object data) throws JSONException {

        Message msg = new Message();
        msg.obj = data;
        msg.what = UPDATE_UI;
        handler.sendMessage(msg);
    }

    protected void didReceiveData(GizWifiErrorCode result, GizWifiDevice arg1,
                                  ConcurrentHashMap<String, Object> dataMap, int arg3) {
        try {

            if (result != GizWifiErrorCode.GIZ_SDK_SUCCESS || dataMap.isEmpty()) {
                return;
            }
            if (dataMap.get("data") != null) {
                Log.i("Apptest", dataMap.get("data").toString());
//          deviceStatu = (ConcurrentHashMap<String, Object>) dataMap.get("data");
                Message msg = new Message();
                msg.obj = dataMap.get("data");
                msg.what = RESP;
                handler.sendMessage(msg);
            }
            System.out.println("------------>data:" + dataMap.toString());

            if (dataMap.get("alerts") != null) {
                Message msg = new Message();
                msg.obj = dataMap.get("alerts");
                msg.what = LOG;
                handler.sendMessage(msg);
            }

            if (dataMap.get("faults") != null) {
                Message msg = new Message();
                msg.obj = dataMap.get("faults");
                msg.what = LOG;
                handler.sendMessage(msg);
            }

//            if (dataMap.get("binary") != null) {
//                byte[] binary = (byte[]) dataMap.get("binary");
////                notifbulid(bytesToHex(binary), R.drawable.ic_launcher, 0x021,
////                        "网关设备");
//                Log.i("xxxxs", "Binary data:" + bytesToHex(binary));
//                System.out.println("------------>Binary data:"+bytesToHex(binary));
//            }


        } catch (Exception e) {
            // TODO: handle exception
            notifbulid("接收数据出错...重启APP中...", R.drawable.ic_launcher, 0x020,
                    "网关设备");
            reStart();
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    String MCUversion = "null";

    protected void didGetHardwareInfo(GizWifiErrorCode result, GizWifiDevice arg1, ConcurrentHashMap<String, String> hardwareInfo) {
        // Log.i("Apptest", hardwareInfo.toString());
        StringBuffer sb = new StringBuffer();
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
            sb.append("Wifi Hardware Version:" +
                    hardwareInfo.get(wifiHardVerKey) + "\r\n");
            sb.append("Wifi Software Version:" +
                    hardwareInfo.get(wifiSoftVerKey) + "\r\n");
            sb.append("MCU Hardware Version:" +
                    hardwareInfo.get(mcuHardVerKey) + "\r\n");
            sb.append("MCU Software Version:" +
                    hardwareInfo.get(mcuSoftVerKey) + "\r\n");
            sb.append("Wifi Firmware Id:" + hardwareInfo.get(FirmwareIdKey) +
                    "\r\n");
            sb.append("Wifi Firmware Version:" +
                    hardwareInfo.get(FirmwareVerKey) + "\r\n");
            sb.append("Product Key:" + "\r\n" + hardwareInfo.get(productKey)
                    + "\r\n");
            // 设备属性
            sb.append("Device ID:" + "\r\n" + device.getDid() + "\r\n");
            sb.append("Device IP:" + device.getIPAddress() + "\r\n");
            sb.append("Device MAC:" + device.getMacAddress() + "\r\n");
            MCUversion = "V" + hardwareInfo.get(mcuHardVerKey);
        } else {
            MCUversion = "查询失败,只有在局域网内方可获取硬件版本";
            sb.append("查询失败,只有在局域网内方可获取信息");
        }
        Message msg = new Message();
        msg.what = HARDWARE;
        msg.obj = sb.toString();
        handler.sendMessage(msg);
    }

    protected void didSetCustomInfo(GizWifiErrorCode arg0, GizWifiDevice arg1) {
//		 progressDialog.cancel();
        Message msg = new Message();
        msg.what = TOAST;
        String toastText;
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == arg0) {
            toastText = (String) getText(R.string.set_info_successful);
        } else {
            toastText = (String) getText(R.string.set_info_failed) + "\n"
                    + arg0;
        }
        msg.obj = toastText;
        handler.sendMessage(msg);
    }

    protected void didUpdateNetStatus(GizWifiDevice arg0,
                                      GizWifiDeviceNetStatus arg1) {
        if (device == arg0) {
            if (GizWifiDeviceNetStatus.GizDeviceUnavailable == arg1
                    || GizWifiDeviceNetStatus.GizDeviceOffline == arg1) {
                handler.sendEmptyMessage(DISCONNECT);
            }
        }
    }

    protected void didSetSubscribe(GizWifiErrorCode arg0, GizWifiDevice arg1,
                                   boolean arg2) {
        Message msg = new Message();
        msg.what = TOAST;
        String toastText;
        String mac;
        if (GizWifiErrorCode.GIZ_SDK_SUCCESS == arg0) {
//			toastText="连接设备成功";
            mac = arg1.getMacAddress();
            spf.edit().putString("msgobj", mac).commit();
        } else {
            toastText = "连接设备失败";
            msg.obj = toastText;
            handler.sendMessage(msg);
        }

    }

}
