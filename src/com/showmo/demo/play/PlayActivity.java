package com.showmo.demo.play;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.showmo.demo.util.spUtil;
import com.way.tabui.gokit.R;
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

/**
 * Created by Administrator on 2016/6/20.
 */
public class PlayActivity extends Activity implements View.OnClickListener {

	IXmSystem xmSystem;
	IXmRealplayCameraCtrl realplayCameraCtrl;

	int cameraid;

	FrameLayout playContent;
	XmGlView glView;

	Button play, stop, HD, SD, AT, capture, Record, StopRecord, btn_rebinder,
			SLrecord, SLrecordclose;

	TextView show;
	String logtext = "LogCat:";

	spUtil sp;

	int playId;
	IXmTalkManager talkma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

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

		show = (TextView) findViewById(R.id.show);
		mHander.sendEmptyMessage(0x126);

		sp = new spUtil(this);
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
						showTV("播放成功！  isLocalNet:" + isLocalNet);
					}

					@Override
					public void onStartErr(XmErrInfo errcode) {
						showTV("errId:" + errcode.errId + ",errCode:"
								+ errcode.errCode + ",errdiscribe:"
								+ errcode.discribe);
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
			if (msg.what == 0x123) {
				Toast.makeText(PlayActivity.this, "删除成功！", Toast.LENGTH_LONG)
						.show();
				setResult(101);
				finish();
			} else if (msg.what == 0x124) {
				Toast.makeText(PlayActivity.this, "删除失败！", Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 0x125) {
				Toast.makeText(PlayActivity.this, (String) msg.obj,
						Toast.LENGTH_LONG).show();
			} else if (msg.what == 0x126) {
				show.setText(logtext);
			} else if (msg.what == 0x127) {
				Toast.makeText(PlayActivity.this, "你没有权限，请先注册登录~",
						Toast.LENGTH_LONG).show();
			} else if (msg.what == 0x128) {
				Toast.makeText(PlayActivity.this, "视频已经在播放中！",
						Toast.LENGTH_LONG).show();
			} else if (msg.what == 0x129) {
				Toast.makeText(PlayActivity.this, "开启错误", Toast.LENGTH_LONG)
						.show();
			} else if (msg.what == 0x130) {
				Toast.makeText(PlayActivity.this, "关闭错误", Toast.LENGTH_LONG)
						.show();

			} else if (msg.what == 0x131) {
				Toast.makeText(PlayActivity.this, "已开启对讲", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 0x132) {
				Toast.makeText(PlayActivity.this, "已关闭对讲", Toast.LENGTH_SHORT)
						.show();
			}
		}
	};

	private boolean isPlay() {
		if (!realplayCameraCtrl.isPlaying()) {
			showTV("视频未开启！");
		}
		return realplayCameraCtrl.isPlaying();
	}

	private void showTV(String ss) {
		logtext = logtext + "\n" + ss;
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

		}
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
