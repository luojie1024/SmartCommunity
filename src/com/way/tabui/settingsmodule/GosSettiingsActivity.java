package com.way.tabui.settingsmodule;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizDeviceSharing;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingUserRole;
import com.gizwits.gizwifisdk.enumration.GizDeviceSharingWay;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizDeviceSharingListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

import static android.graphics.Color.BLACK;

public class GosSettiingsActivity extends GosBaseActivity implements
		OnClickListener {

    private static final int QR_WIDTH = 800;
    private static final int QR_HEIGHT = 800;
    /** The ll About */
	private LinearLayout llAbout, llexit,llsetbund,llSetLed,llSetSafe,llQrcode;

	private Switch sw_red,sw_sf;
	/** led红灯开关 0=关 1=开. */
	private static final String KEY_RED_SWITCH = "LED_OnOff";
	
	/** The Intent */
	Intent intent;

	/** The ActionBar */
	ActionBar actionBar;
	
	public GizWifiDevice device=null;
	private HashMap<String, Object> deviceStatu;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_settings);
		initDevice();
		// 设置ActionBar
		setActionBar(true, true, R.string.site);
		initView();
		initEvent();
		if (device==null) {
			llSetLed.setVisibility(View.GONE);
			llSetSafe.setVisibility(View.GONE);
            llQrcode.setVisibility(View.GONE);

		}else{
			llSetLed.setVisibility(View.VISIBLE);
			llSetSafe.setVisibility(View.VISIBLE);
            llQrcode.setVisibility(View.VISIBLE);
		}
	}

   public void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		deviceStatu = new HashMap<String, Object>();
	}
	
	private void initView() {
		llSetLed= (LinearLayout) findViewById(R.id.llSetLed);
		llAbout = (LinearLayout) findViewById(R.id.llAbout);
		llexit = (LinearLayout) findViewById(R.id.llexit);
		llsetbund= (LinearLayout) findViewById(R.id.llsetbund);
		llSetSafe=(LinearLayout) findViewById(R.id.llSetSafe);
        llQrcode=(LinearLayout) findViewById(R.id.llQRcode);
		sw_red=(Switch) findViewById(R.id.sw_red);
		sw_sf=(Switch) findViewById(R.id.sw_sf);
		sw_sf.setChecked(spf.getBoolean("issafe", true));
	}

	// GosPushManager gosPushManager;
	private void initEvent() {
		llAbout.setOnClickListener(this);
		llexit.setOnClickListener(this);
		llsetbund.setOnClickListener(this);
//		sw_red.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				try {
//					sendJson(KEY_RED_SWITCH, sw_red.isChecked());
//					if(sw_red.isChecked()){
//						Toast.makeText(getApplicationContext(), "请看网关测试灯是否亮起",Toast.LENGTH_SHORT ).show();
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
		
		sw_red.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				try {
					sendJson(KEY_RED_SWITCH, sw_red.isChecked());
					if(sw_red.isChecked()){
						Toast.makeText(getApplicationContext(), "请看网关测试灯是否亮起",Toast.LENGTH_SHORT ).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		sw_sf.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(sw_sf.isChecked()){
					spf.edit().putBoolean("issafe", true).commit();
					Toast.makeText(getApplicationContext(), "监听状态，可接收警报信息",Toast.LENGTH_SHORT ).show();
				}else{
					spf.edit().putBoolean("issafe", false).commit();
					Toast.makeText(getApplicationContext(), "撤防状态，不再接收警报信息",Toast.LENGTH_SHORT ).show();
				}
			}
		});
//		sw_sf.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(sw_sf.isChecked()){
//					spf.edit().putBoolean("issafe", true).commit();
//					Toast.makeText(getApplicationContext(), "监听状态，可接收警报信息",Toast.LENGTH_SHORT ).show();
//				}else{
//					spf.edit().putBoolean("issafe", false).commit();
//					Toast.makeText(getApplicationContext(), "撤防状态，不再接收警报信息",Toast.LENGTH_SHORT ).show();
//				}
//			}
//		});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llAbout:
			intent = new Intent(GosSettiingsActivity.this,
					GosAboutActivity.class);
			intent.putExtra("title", "关于");
			startActivity(intent);
			break;
		case R.id.llsetbund:
			intent = new Intent(GosSettiingsActivity.this,
					SetBundMesActivity.class);
			startActivity(intent);
			break;
			
		case R.id.llexit:
			Intent eintent = new Intent();
			eintent.setAction("com.way.util.exit_app");
			sendBroadcast(eintent);
			break;
            case R.id.llQRcode:
               makeQRCode();
//                sharing();
                break;

		default:
			break;
		}

	}

	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.v("==",""+hashMap.get(key));
	//	Log.i("Apptest", hashMap.toString());
	}
    ImageView ImageView;
    GizDeviceSharingListener mListener = new GizDeviceSharingListener() {
        // 实现设备分享的回调
        @Override
        public void didSharingDevice(GizWifiErrorCode result, String deviceID, int sharingID, Bitmap QRCodeImage) {
            if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
                QRCodeImage.setHeight(QR_HEIGHT);
                QRCodeImage.setWidth(QR_WIDTH);
                ImageView = new ImageView(GosSettiingsActivity.this);
                ImageView.setImageBitmap(QRCodeImage);
                AlertDialog.Builder builder = new AlertDialog.Builder(GosSettiingsActivity.this);
                builder.setTitle("二维码");
                builder.setView(ImageView);
                builder.show();
            } else {
                Toast.makeText(GosSettiingsActivity.this, "分享失败"+result.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    };

    String token;
    String uid;
    public void sharing(){
        uid = spf.getString("Uid","");
        token =spf.getString("Token","");
    GizDeviceSharing.setListener(mListener);
// 在设备列表中找到可以分享的设备
// 二维码分享设备

       if( (device.getSharingRole()).equals(GizDeviceSharingUserRole.GizDeviceSharingNormal)){
        Toast.makeText(this, "您的权限为：普通用户.没有分享权限", Toast.LENGTH_SHORT).show();
           return;
       }
        else if((device.getSharingRole()).equals(GizDeviceSharingUserRole.GizDeviceSharingGuest)){
           Toast.makeText(this, "您的权限为：分享者.没有分享权限", Toast.LENGTH_SHORT).show();
           return;
        }
    GizDeviceSharing.sharingDevice(token,device.getDid(), GizDeviceSharingWay.GizDeviceSharingByQRCode, null, null);
    }




    private void makeQRCode(){
         uid = spf.getString("Uid",null);
        token =spf.getString("Token",null);

        String mac=device.getMacAddress();

//        String text = "http://blog.csdn.net/gao36951";
        String url = "type=bang"+"&mac="+mac+"&productKey="+ GosConstant.Product_Key
                +"&productSecret="+GosConstant.Product_Secret;

        Log.i("xxs", "url::::"+url);
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return ;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            ImageView ImageView = new ImageView(this);
            ImageView.setImageBitmap(bitmap);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("二维码"+device.getPasscode());
            builder.setView(ImageView);
         //   builder.setMessage(url);
            builder.show();
            // 显示到一个ImageView上面
            // sweepIV.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Log.i("log", "生成二维码错误" + e.getMessage());
        }
    }

    public static Bitmap createQRCode(String str, int widthAndHeight)
            throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = BLACK;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
    /**
	 * 设置ActionBar（工具方法*开发用*）
	 * 
	 * @param HBE
	 * @param DSHE
	 * @param Title
	 */
	public void setActionBar(Boolean HBE, Boolean DSHE, int Title) {

		actionBar = getActionBar();// 初始化ActionBar
		actionBar.setHomeButtonEnabled(HBE);
		actionBar.setIcon(R.drawable.back_bt);
		actionBar.setTitle(Title);
		actionBar.setDisplayShowHomeEnabled(DSHE);
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
