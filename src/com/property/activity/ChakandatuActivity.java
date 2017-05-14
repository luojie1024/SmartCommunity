package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.property.base.BaseActivity;
import com.property.duotushangchuan.PhotoView;
import com.property.duotushangchuan.PublicWay;
import com.property.duotushangchuan.ViewPagerFixed;
import com.way.tabui.gokit.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChakandatuActivity extends BaseActivity {
	private Intent intent;
//	// 返回按钮
//	private Button back_bt;
//	// 发送按钮
//	private Button send_bt;
//	// 删除按钮
//	private Button del_bt;
	// 顶部显示预览图片位置的textview
	private TextView positionTextView;
	// 获取前一个activity传过来的position
	private int position;
	// 当前的位置
	private int location = 0;
	private KJBitmap bitmap;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();

	private Context mContext;

	RelativeLayout photo_relativeLayout;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_chakandatu);
	}

	@Override
	public void initData() {
		super.initData();
		PublicWay.activityList.add(this);
		mContext = this;
		bitmap = new KJBitmap();
//		back_bt = (Button) findViewById(Res.getWidgetID("gallery_back"));
//		send_bt = (Button) findViewById(Res.getWidgetID("send_button"));
//		del_bt = (Button) findViewById(Res.getWidgetID("gallery_del"));
//		back_bt.setOnClickListener(new BackListener());
//		send_bt.setOnClickListener(new GallerySendListener());
//		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = Integer.parseInt(intent.getStringExtra("position"));
//		isShowOkBt();
//		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.vp_chakandatu);
		pager.setOnPageChangeListener(pageChangeListener);
		List<String> image = getIntent().getStringArrayListExtra("image");
		for (int i = 0; i < image.size(); i++) {
			initListViews(image.get(i));
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin(10);
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}		

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(String imageUrl) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		bitmap.display(img, imageUrl);
//		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}
//
//	// 返回按钮添加的监听器
//	private class BackListener implements OnClickListener {
//
//		public void onClick(View v) {
//			intent.setClass(ChakandatuActivity.this, ImageFile.class);
//			startActivity(intent);
//		}
//	}
//
//	// 删除按钮添加的监听器
//	private class DelListener implements OnClickListener {
//
//		public void onClick(View v) {
//			if (listViews.size() == 1) {
//				Bimp.getBimp().tempSelectBitmap.clear();
//				Bimp.max = 0;
//				send_bt.setText(Res.getString("finish") + "("
//						+ Bimp.getBimp().tempSelectBitmap.size() + "/"
//						+ PublicWay.num + ")");
//				Intent intent = new Intent("data.broadcast.action");
//				sendBroadcast(intent);
//				finish();
//			} else {
//				Bimp.getBimp().tempSelectBitmap.remove(location);
//				Bimp.max--;
//				pager.removeAllViews();
//				listViews.remove(location);
//				adapter.setListViews(listViews);
//				send_bt.setText(Res.getString("finish") + "("
//						+ Bimp.getBimp().tempSelectBitmap.size() + "/"
//						+ PublicWay.num + ")");
//				adapter.notifyDataSetChanged();
//			}
//		}
//	}
//
//	// 完成按钮的监听
//	private class GallerySendListener implements OnClickListener {
//		public void onClick(View v) {
//			finish();
//			// intent.setClass(mContext,Fabuzufangxinxi.class);
//			// startActivity(intent);
//		}
//
//	}
//
//	public void isShowOkBt() {
//		if (Bimp.getBimp().tempSelectBitmap.size() > 0) {
//			send_bt.setText(Res.getString("finish") + "("
//					+ Bimp.getBimp().tempSelectBitmap.size() + "/"
//					+ PublicWay.num + ")");
//			send_bt.setPressed(true);
//			send_bt.setClickable(true);
//			send_bt.setTextColor(Color.WHITE);
//		} else {
//			send_bt.setPressed(false);
//			send_bt.setClickable(false);
//			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
//		}
//	}
//
//	/**
//	 * 监听返回按钮
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (position == 1) {
//				this.finish();
//				intent.setClass(ChakandatuActivity.this, AlbumActivity.class);
//				startActivity(intent);
//			} else if (position == 2) {
//				this.finish();
//				intent.setClass(ChakandatuActivity.this, ShowAllPhoto.class);
//				startActivity(intent);
//			}
//		}
//		return true;
//	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
