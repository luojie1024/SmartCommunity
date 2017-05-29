package com.LifeServices.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 商铺详情页--ViewPagerCompat适配器
 * @author Stone
 */
public class ViewPagerAdapter extends PagerAdapter {
	
	private List<View> mListViews;
	private List<String> mTitleList;
	
	public ViewPagerAdapter(List<View> mListViews, List<String> mTitleList) {
		this.mListViews = mListViews;
		this.mTitleList = mTitleList;
	}
	
	@Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(mListViews.get(position));//删除页卡  
    }  
	
	@Override
    public CharSequence getPageTitle(int position) {

        return mTitleList.get(position);//直接用适配器来完成标题的显示，所以从上面可以看到，我们没有使用PagerTitleStrip。当然你可以使用。  
    }  


    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡
         container.addView(mListViews.get(position), 0);//添加页卡  
         return mListViews.get(position);  
    }  

    @Override
    public int getCount() {           
        return  mListViews.size();//返回页卡的数量  
    }  
      
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写  
    }  

}
