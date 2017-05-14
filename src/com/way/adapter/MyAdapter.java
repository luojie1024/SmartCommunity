/**  
 * 文件名：MyAdapter.java  
 *  
 * 版本信息：  
 * 日期：2014年5月1日  
 * Copyright 足下 Corporation 2014   
 * 版权所有  
 *  
 */

package com.way.adapter;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * 
 * 项目名称：SmartCommunity 类名称：MyAdapter 类描述： 创建人：Admin 创建时间：2014年5月1日 下午2:14:41
 * 修改人：Admin 修改时间：2014年5月1日 下午2:14:41 修改备注：
 * 
 * @version
 * 
 */
public class MyAdapter extends PagerAdapter{
	/**
	 * 填充ViewPager页面的适配器
	 * @author Administrator
	 */
	private int[] imageResId ;
	private List<ImageView> imageViews; // 滑动的图片集合
	
		public MyAdapter(int[] imageResId,List<ImageView> imageViews) {
			this.imageResId = imageResId ;
			this.imageViews = imageViews ;
		}

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
}
