/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zxing;

import java.io.IOException;
import java.lang.reflect.Field;

import zxing.camera.CameraManager;
import zxing.decoding.DecodeThread;
import zxing.utils.CaptureActivityHandler;
import zxing.utils.InactivityTimer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.listener.GizWifiSDKListener;
import com.google.zxing.Result;
import com.way.adapter.DatabaseAdapter;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private InactivityTimer inactivityTimer;

	private SurfaceView scanPreview = null;
	private RelativeLayout scanContainer;
	private RelativeLayout scanCropView;
	private ImageView scanLine;

	@SuppressWarnings("unused")
	private String product_key, passcode, did;
	private Button btnCancel;
	private ImageView ivReturn;
	private String uid, token, mac, productKey, productSecret;
	private String name, address, bindgiz;
	protected SharedPreferences spf;

	private GizWifiSDKListener gizWifiSDKListener = new GizWifiSDKListener() {

		/** 用于设备绑定 */
		public void didBindDevice(int error, String errorMessage, String did) {
			CaptureActivity.this.didBindDevice(error, errorMessage, did);
		};

	};

	/**
	 * 设备绑定回调
	 * 
	 * @param result
	 * @param did
	 */
	protected void didBindDevice(int error, String errorMessage,
			java.lang.String did) {
		if (error == 0) {
			mHandler.sendEmptyMessage(handler_key.SUCCESS.ordinal());
		} else {
			Message message = new Message();
			message.what = handler_key.FAILED.ordinal();
			message.obj = errorMessage;
			mHandler.sendMessage(message);
		}
	}

	/**
	 * ClassName: Enum handler_key. <br/>
	 * <br/>
	 * date: 2014-11-26 17:51:10 <br/>
	 * 
	 * @author Lien
	 */
	private enum handler_key {

		START_BIND,

		SUCCESS,

		FAILED, ADD_DATA,

	}

	private DatabaseAdapter dbAdapter;
	/**
	 * The handler.
	 */
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {

			case START_BIND:
				if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(token)
						&& !TextUtils.isEmpty(mac)
						&& !TextUtils.isEmpty(productKey)
						&& !TextUtils.isEmpty(productSecret)) {
					startBind(uid, token, mac, productKey, productSecret);
				} else {
					startBind(passcode, did);
				}

				break;

			case SUCCESS:
				// Toast.makeText(CaptureActivity.this, R.string.add_successful,
				// Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(CaptureActivity.this,
				// DeviceListActivity.class);
				// startActivity(intent);
				finish();
				break;
			case ADD_DATA:
				Gizinfo gizinfo = new Gizinfo(name, address, bindgiz, "NULL", 0);
				dbAdapter.add(gizinfo);
				Toast.makeText(getApplicationContext(), "添加数据完毕",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			case FAILED:
				Toast.makeText(CaptureActivity.this, R.string.add_failed,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	};

	@SuppressWarnings("deprecation")
	private void startBind(final String passcode, final String did) {

		GizWifiSDK.sharedInstance().bindDevice(spf.getString("Uid", ""),
				spf.getString("Token", ""), did, passcode, null);
	}
	private void startBind(String uid, String token, String mac,
			String productKey, String productSecret) {
		GizWifiSDK.sharedInstance().bindRemoteDevice(uid, token, mac,
				productKey, productSecret);
	}

	private Rect mCropRect = null;

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	private boolean isHasSurface = false;
	private boolean isfromoc = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/**
		 * 设置为竖屏
		 */
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}

		dbAdapter = new DatabaseAdapter(this);
		// 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
		GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);

		spf = getSharedPreferences("set", Context.MODE_PRIVATE);

		Intent intent = getIntent();
		isfromoc = intent.getBooleanExtra("fromoc", false);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_gos_capture);

		scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
		scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
		scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
		scanLine = (ImageView) findViewById(R.id.capture_scan_line);

		inactivityTimer = new InactivityTimer(this);

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		animation.setDuration(4500);
		animation.setRepeatCount(-1);
		animation.setRepeatMode(Animation.RESTART);
		scanLine.startAnimation(animation);

		btnCancel = (Button) findViewById(R.id.btn_cancel);
		ivReturn = (ImageView) findViewById(R.id.iv_return);
		OnClickListener myClick = new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				CaptureActivity.this.finish();
			}
		};
		btnCancel.setOnClickListener(myClick);
		ivReturn.setOnClickListener(myClick);
	}

	@Override
	public void onResume() {
		super.onResume();

		// 每次返回activity都要注册一次sdk监听器，保证sdk状态能正确回调
		GizWifiSDK.sharedInstance().setListener(gizWifiSDKListener);
		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		handler = null;

		if (isHasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(scanPreview.getHolder());
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			scanPreview.getHolder().addCallback(this);
		}

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!isHasSurface) {
			scanPreview.getHolder().removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!isHasSurface) {
			isHasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isHasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * 
	 * @param bundle
	 *            The extras
	 */
	public void handleDecode(Result rawResult, Bundle bundle) {
		String text = rawResult.getText();
		if ((!isfromoc) && text.contains("product_key=")
				&& text.contains("did=") && text.contains("passcode=")) {

			inactivityTimer.onActivity();
			product_key = getParamFomeUrl(text, "product_key");
			did = getParamFomeUrl(text, "did");
			passcode = getParamFomeUrl(text, "passcode");

			Toast.makeText(CaptureActivity.this, R.string.scanning_successful,
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(handler_key.START_BIND.ordinal());

		} else if ((!isfromoc) && text.contains("uid")
				&& text.contains("token") && text.contains("productKey")
				&& text.contains("productSecret")) {
			inactivityTimer.onActivity();
			uid = getParamFomeUrl(text, "uid");
			token = getParamFomeUrl(text, "token");
			mac = getParamFomeUrl(text, "mac");
			productKey = getParamFomeUrl(text, "productKey");
			productSecret = getParamFomeUrl(text, "productSecret");
			Toast.makeText(CaptureActivity.this, R.string.scanning_successful,
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(handler_key.START_BIND.ordinal());
		} else if ((isfromoc) && text.contains("name")
				&& text.contains("address")) {
			name = getParamFomeUrl(text, "name");
			address = getParamFomeUrl(text, "address");
			Intent intent = getIntent();
			bindgiz = intent.getStringExtra("bindgiz");
			Toast.makeText(CaptureActivity.this, R.string.scanning_successful,
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessage(handler_key.ADD_DATA.ordinal());
		} else {
			handler = new CaptureActivityHandler(this, cameraManager,
					DecodeThread.ALL_MODE);
		}
	}

	private String getParamFomeUrl(String url, String param) {
		String product_key = "";
		int startindex = url.indexOf(param + "=");
		startindex += (param.length() + 1);
		String subString = url.substring(startindex);
		int endindex = subString.indexOf("&");
		if (endindex == -1) {
			product_key = subString;
		} else {
			product_key = subString.substring(0, endindex);
		}
		return product_key;
	}

	// @Override
	// protected void didBindDevice(int error, String errorMessage, String did)
	// {
	// Log.d("扫描结果", "error=" + error + ";errorMessage=" + errorMessage +
	// ";did=" + did);
	// if (error == 0) {
	// mHandler.sendEmptyMessage(handler_key.SUCCESS.ordinal());
	// } else {
	// Message message = new Message();
	// message.what = handler_key.FAILED.ordinal();
	// message.obj = errorMessage;
	// mHandler.sendMessage(message);
	// }
	// }

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, cameraManager,
						DecodeThread.ALL_MODE);
			}

			initCrop();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		// camera error
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage("相机打开出错，请稍后重试");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public Rect getCropRect() {
		return mCropRect;
	}

	/**
	 * 初始化截取的矩形区域
	 */
	private void initCrop() {
		int cameraWidth = cameraManager.getCameraResolution().y;
		int cameraHeight = cameraManager.getCameraResolution().x;

		/** 获取布局中扫描框的位置信息 */
		int[] location = new int[2];
		scanCropView.getLocationInWindow(location);

		int cropLeft = location[0];
		int cropTop = location[1] - getStatusBarHeight();

		int cropWidth = scanCropView.getWidth();
		int cropHeight = scanCropView.getHeight();

		/** 获取布局容器的宽高 */
		int containerWidth = scanContainer.getWidth();
		int containerHeight = scanContainer.getHeight();

		/** 计算最终截取的矩形的左上角顶点x坐标 */
		int x = cropLeft * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的左上角顶点y坐标 */
		int y = cropTop * cameraHeight / containerHeight;

		/** 计算最终截取的矩形的宽度 */
		int width = cropWidth * cameraWidth / containerWidth;
		/** 计算最终截取的矩形的高度 */
		int height = cropHeight * cameraHeight / containerHeight;

		/** 生成最终的截取的矩形 */
		mCropRect = new Rect(x, y, width + x, height + y);
	}

	private int getStatusBarHeight() {
		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}