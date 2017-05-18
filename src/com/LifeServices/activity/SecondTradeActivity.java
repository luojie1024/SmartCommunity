package com.LifeServices.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.LifeServices.adapter.TradeItemListAdapter;
import com.LifeServices.bean.SecondTrade;
import com.way.tabui.gokit.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * 某一分类下的所有店铺页面
 * @author Stone
 * @date  2014-4-26 
 */
public class SecondTradeActivity extends Activity implements OnItemClickListener {
	@SuppressWarnings("unused")
	private static final String TAG = "SecondTradeActivity" ;

	private TextView tvTitle;
	private ListView lvTradeItemAllList;
	private TradeItemListAdapter tradeItemListAdapter;
	
	@SuppressWarnings("unused")
	private String title;
	private List<SecondTrade> tradeItemList = new ArrayList<SecondTrade>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second_trade_all);
		
		//获取物品数据
		getTradeItemData();
		initView();
		
	}
	
	public void initView() {
		//设置标题
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText("二手物品");
		
		lvTradeItemAllList = (ListView) findViewById(R.id.lv_second_trade_all);
		tradeItemListAdapter = new TradeItemListAdapter(this, (ArrayList<SecondTrade>) tradeItemList);
		lvTradeItemAllList.setAdapter(tradeItemListAdapter);
		lvTradeItemAllList.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		//toast("点击了： " + position);
		//将当前点击的Shop对象传递给下一个Activity
//		Intent toShopItem = new Intent(SecondTradeActivity.this, ShopItemActivity.class);
//		Bundle bundle = new Bundle();  
//        bundle.putSerializable("shop", shopList.get(position) );  
//        bundle.putString("shopID", shopList.get(position).getObjectId()); //商铺的ID需要单独传递,否则获取到的是null
//        Log.i(TAG, ">>发出>>" + "shopID: "+shopList.get(position).getObjectId()+" shopName: "+shopList.get(position).getName());
//        toShopItem.putExtras(bundle);
//		startActivity(toShopItem);
	}
	
	/**
	 * 加载二手交易中所有商品到ListView中
	 */
	private void getTradeItemData() {
		BmobQuery<SecondTrade> query = new BmobQuery<SecondTrade>();
		query.findObjects(new FindListener<SecondTrade>() {
			@Override
			public void done(List<SecondTrade> list, BmobException e) {
				if(e==null){
					toast("查询成功：共"+list.size()+"条数据。");
					tradeItemList = list;
					// 通知Adapter数据更新
					tradeItemListAdapter.refresh((ArrayList<SecondTrade>) tradeItemList);
				}else{
					Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
				}
			}
		});

	}
	
	private void toast(String toast) {
		Toast.makeText(this, toast,  Toast.LENGTH_SHORT).show();
	};
	
}
