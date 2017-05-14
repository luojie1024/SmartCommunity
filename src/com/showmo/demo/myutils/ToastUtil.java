package com.showmo.demo.myutils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


public class ToastUtil {
	private static final int TOAST_SHORT_STR = 1;
	private static final int TOAST_LONG_STR = 2;
	private static final int TOAST_SHORT_ID = 3;
	private static final int TOAST_LONG_ID = 4;
	public static final long mainThreadid=Looper.getMainLooper().getThread().getId();
	public static Toast myToast=null;
	
	public static Handler m_Handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			if(!(msg.obj instanceof ToastStruc)){
				return;
			}
			ToastStruc info=(ToastStruc)msg.obj;
			switch (msg.what) {
			case TOAST_SHORT_STR:
				toastUseSingleToast(info.context,info.msg, Toast.LENGTH_SHORT);
				break;
			case TOAST_LONG_STR:
				toastUseSingleToast(info.context,info.msg, Toast.LENGTH_LONG);
				break;
			case TOAST_SHORT_ID:
				toastUseSingleToast(info.context,info.msgId, Toast.LENGTH_SHORT);
				break;
			case TOAST_LONG_ID:
				toastUseSingleToast(info.context,info.msgId, Toast.LENGTH_LONG);
				break;
			default:
				break;
			}
		};
	};


	public static class MyToast extends Toast{
		MyToast(Context context){
			super(context);
		}

		@Override
		public void setText(int resId) {
			View v=getView();
			TextView tv=(v==null?null:(TextView) v.findViewById(android.R.id.text1));
			if(tv!=null){
				tv.setText(resId);
				return;
			}
			super.setText(resId);
		}

		@Override
		public void setText(CharSequence s) {
			View v=getView();
			TextView tv=(v==null?null:(TextView) v.findViewById(android.R.id.text1));
			if(tv!=null){
				tv.setText(s);
				return;
			}
			super.setText(s);
		}
	}
	private static int mTaostViewRes=0;
	public static void initToast(int toastViewRef){
		mTaostViewRes=toastViewRef;
	}
	private static void createToast(Context context){
		if(myToast==null){
			Context appContext=context.getApplicationContext();
			View toastV=null;
			if(mTaostViewRes!=0){
				LayoutInflater layoutInflater=LayoutInflater.from(appContext);
				View v=layoutInflater.inflate(mTaostViewRes,null);
				//LogUtils.e("Toast","----mTaostViewRes!=01--------");
				if(v!=null){
					//LogUtils.e("Toast","----mTaostViewRes!=02--------");
					TextView tv=(TextView)v.findViewById(android.R.id.text1);
					if(tv!=null){
						//LogUtils.e("Toast","----mTaostViewRes!=03--------");
						tv.setText("TestToast");
						toastV=v;
					}
				}
			}
			if(toastV!=null){
				myToast = new MyToast(appContext);
				myToast.setView(toastV);
			}else{
				myToast=Toast.makeText(appContext, "", Toast.LENGTH_SHORT);
			}
			WindowManager wm = (WindowManager) appContext
					.getSystemService(Context.WINDOW_SERVICE);
			int height = wm.getDefaultDisplay().getHeight();
			myToast.setGravity(Gravity.CENTER_HORIZONTAL,0,20);
		}
	}
	private static void toastUseSingleToast(Context context,int textid,int duration){
		createToast(context);
		myToast.setDuration(duration);
		myToast.setText(textid);
		//myToast.setGravity(Gravity.CENTER_HORIZONTAL, 0,myToast.getYOffset()+10);
		myToast.show();
	}
	private static void toastUseSingleToast(Context context,String text,int duration){
		createToast(context);
		myToast.setDuration(duration);
		myToast.setText(text);
		//myToast.setGravity(Gravity.CENTER_HORIZONTAL, 0, myToast.getYOffset() + 10);
		myToast.show();
	}
	public static void toastShort(Context context, String str) {
		context=context.getApplicationContext();
		if(Thread.currentThread().getId() ==mainThreadid){
			toastUseSingleToast(context,str, Toast.LENGTH_SHORT);
		}else{
			toastShortFromThread(context, str);
		}
	}

	public static void toastShort(Context context, int strId) {
		context=context.getApplicationContext();
		if(Thread.currentThread().getId() ==mainThreadid){
			toastUseSingleToast(context,strId, Toast.LENGTH_SHORT);
		}else{
			toastShortFromThread(context, strId);
		}
	}

	public static void toastLong(Context context, String str) {
		context=context.getApplicationContext();
		if(Thread.currentThread().getId() ==mainThreadid){
			createToast(context);
			toastUseSingleToast(context,str, Toast.LENGTH_LONG);
		}else{
			toastLongFromThread(context,str);
		}
	}

	public static void toastLong(Context context, int strId) {
		context=context.getApplicationContext();
		if(Thread.currentThread().getId() ==mainThreadid){
			createToast(context);
			toastUseSingleToast(context,strId, Toast.LENGTH_LONG);
		}else{
			toastLongFromThread(context,strId);
		}
	}

	private static void toastShortFromThread(Context context, String str) {
		Message msgMessage = m_Handler.obtainMessage();
		msgMessage.obj = new ToastStruc(context, str, 0);
		msgMessage.what = TOAST_SHORT_STR;
		m_Handler.sendMessage(msgMessage);
	}
	private static void toastShortFromThread(Context context, int strId) {
		Message msgMessage = m_Handler.obtainMessage();
		msgMessage.obj = new ToastStruc(context, "", strId);
		msgMessage.what = TOAST_SHORT_ID;
		m_Handler.sendMessage(msgMessage);
	}
	private static void toastLongFromThread(Context context, String strId) {
		Message msgMessage = m_Handler.obtainMessage();
		msgMessage.obj = new ToastStruc(context, strId,0);
		msgMessage.what = TOAST_LONG_STR;
		m_Handler.sendMessage(msgMessage);
	}
	private static void toastLongFromThread(Context context, int strId) {
		Message msgMessage = m_Handler.obtainMessage();
		msgMessage.obj = new ToastStruc(context, "", strId);
		msgMessage.what = TOAST_LONG_ID;
		m_Handler.sendMessage(msgMessage);
	}
	private static class ToastStruc {
		Context context;
		String msg;
		int msgId;

		public ToastStruc(Context context, String msg, int msgId) {
			super();
			this.context = context;
			this.msg = msg;
			this.msgId = msgId;
		}
	}
}
