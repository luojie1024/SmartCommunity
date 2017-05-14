package com.property.activity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.property.base.BaseActivity;
import com.property.base.BaseFragment;
import com.property.activity.MyFragmentPagerAdapter;
import com.property.activity.WorkerBaoxiuFragment;
import com.way.tabui.gokit.R;

public class YuangongbaoxiuFragment extends BaseFragment {

	private ViewPager mPager;
	private ArrayList<Fragment> fragmentList;
	private ImageView image;
	private TextView view2, view3, view4, view5;
	private int currIndex; // 当前页卡编号
	private int bmpW; // 横线图片宽度
	private int offset; // 图片移动的偏移量
	private ImageView ivBack;
	private TextView tvBack;		

	@Override
	public void registerBroadcast() {
		
	}

	@Override
	public void unRegisterBroadcast() {
		
	}

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		View view = inflater.inflate(R.layout.activity_workerbaoxiu, null);
		view2 = (TextView) view.findViewById(R.id.tv_guid2);
		view3 = (TextView) view.findViewById(R.id.tv_guid3);
		view4 = (TextView) view.findViewById(R.id.tv_guid4);
		view5 = (TextView) view.findViewById(R.id.tv_guid5);
		ivBack = (ImageView) view.findViewById(R.id.iv_back);
		tvBack = (TextView) view.findViewById(R.id.tv_back);
		image = (ImageView) view.findViewById(R.id.cursor);
		mPager = (ViewPager) view.findViewById(R.id.viewpager);
		ivBack.setVisibility(View.GONE);
		tvBack.setVisibility(View.GONE);
		view2.setOnClickListener(new txListener(0));
		view3.setOnClickListener(new txListener(1));
		view4.setOnClickListener(new txListener(2));
		view5.setOnClickListener(new txListener(3));
		return view;
	}

	
	@Override
	public void initData() {
		super.initData();
		InitTextView();
		InitImage();
		InitViewPager();
	}
	
	// 初始化标签名
	public void InitTextView() {

		
	}

	public class txListener implements View.OnClickListener {

		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}

	}

	// 初始化图片的位移像素
	public void InitImage() {
		WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
//		int height = width/10*3;
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) image.getLayoutParams();  
//		linearParams.height = height; 
		linearParams.width = width/4;
		image.setLayoutParams(linearParams);
		bmpW = image.getLayoutParams().width;
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 4 - bmpW) / 2;

		// imageview设置平移，使下划线平移到初始位置（平移一个offset)
		Matrix matris = new Matrix();
		matris.postTranslate(offset, 0);
		image.setImageMatrix(matris);

	}

	/**
	 * 初始化ViewPager
	 */
	public void InitViewPager() {

		fragmentList = new ArrayList<Fragment>();
		
		WorkerBaoxiuFragment daiweixiuFragment = new WorkerBaoxiuFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putInt("status", 1);
		daiweixiuFragment.setArguments(bundle1);

		WorkerBaoxiuFragment yijiedanFrament = new WorkerBaoxiuFragment();
		Bundle bundle2 = new Bundle();
		bundle2.putInt("status", 2);
		yijiedanFrament.setArguments(bundle2);
		
		WorkerBaoxiuFragment weixiuzhongFragment = new WorkerBaoxiuFragment();
		Bundle bundle3 = new Bundle();
		bundle3.putInt("status", 3);
		weixiuzhongFragment.setArguments(bundle3);

		WorkerBaoxiuFragment yiwanchengFragment = new WorkerBaoxiuFragment();
		Bundle bundle4 = new Bundle();
		bundle4.putInt("status", 4);
		yiwanchengFragment.setArguments(bundle4);		

		fragmentList.add(daiweixiuFragment);
		fragmentList.add(yijiedanFrament);
		fragmentList.add(weixiuzhongFragment);
		fragmentList.add(yiwanchengFragment);
		// 给ViewPager设置适配器
		mPager.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器

	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		private int one = offset * 2 + bmpW;// 两个相邻页面的偏移量

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(currIndex * one, arg0 * one, 0, 0);// 平移动画
			currIndex = arg0;
			animation.setFillAfter(true);// 动画终止时停留在最后一帧，不然会回到没有执行前的状态
			animation.setDuration(200);// 动画持续时间0.2秒
			image.startAnimation(animation);// 是用ImageView来显示动画的
//			int i = currIndex + 1;
			switch (arg0) {			
			case 0:
				 view2.setTextColor(getResources().getColor(R.color.theme_color));
				 view3.setTextColor(getResources().getColor(R.color.black));
				 view4.setTextColor(getResources().getColor(R.color.black));
				 view5.setTextColor(getResources().getColor(R.color.black));
				break;
			case 1:
				 view2.setTextColor(getResources().getColor(R.color.black));
				 view3.setTextColor(getResources().getColor(R.color.theme_color));
				 view4.setTextColor(getResources().getColor(R.color.black));
				 view5.setTextColor(getResources().getColor(R.color.black));
				break;
			case 2:
				 view2.setTextColor(getResources().getColor(R.color.black));
				 view3.setTextColor(getResources().getColor(R.color.black));
				 view4.setTextColor(getResources().getColor(R.color.theme_color));
				 view5.setTextColor(getResources().getColor(R.color.black));
				break;
			case 3:
				 view2.setTextColor(getResources().getColor(R.color.black));
				 view3.setTextColor(getResources().getColor(R.color.black));
				 view4.setTextColor(getResources().getColor(R.color.black));
				 view5.setTextColor(getResources().getColor(R.color.theme_color));
				break;
			default:
				break;
			}
		}
	}
}
