package com.LifeServices.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.LifeServices.adapter.ShopListAdapter;
import com.LifeServices.bean.Shop;
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
public class ShopAllActivity extends Activity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
	
	private static final String TAG = "ShopAllActivity" ;

	private TextView tvTitle;
	private TextView tvEmptyBg;  //当数据为空时现实的视图
	
	private ListView lvShopAllList;
	private ShopListAdapter shopListAdapter;
	private SwipeRefreshLayout swipeLayout;
	
	//记录从ShopActivity中传过来的当前点击项的类型
	private String title;
	private List<Shop> shopList = new ArrayList<Shop>();
	
	//下拉刷新
	private static final int STATE_REFRESH = 0;// 下拉刷新
	@SuppressWarnings("unused")
	private static final int STATE_MORE = 1;// 加载更多
	
	private int limit = 10;		// 每页的数据是10条
	private int curPage = 0;		// 当前页的编号，从0开始
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_all);
		//得到从上级Activity中传入的Type数据
		title = getIntent().getStringExtra("title");
		//获取商店数据
		queryShopData();
		
		initView();
		
	}
	
	public void initView() {
		//设置标题
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTitle.setText(title);
		
		tvEmptyBg = (TextView) findViewById(R.id.ll_msg_empty);
		tvEmptyBg.setVisibility(View.GONE);
		
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.lv_shop_all_swipe_container);
		
		swipeLayout.setOnRefreshListener(this);
//		swipeLayout.setColorScheme(getResources().getColor(android.R.color.holo_blue_bright),getResources().getColor( android.R.color.holo_green_light),
//		getResources().getColor(android.R.color.holo_orange_light), getResources().getColor(android.R.color.holo_red_light));
		
		lvShopAllList = (ListView) findViewById(R.id.lv_shop_all);
		//type商店类型，默认为1
		shopListAdapter = new ShopListAdapter(this, (ArrayList<Shop>) shopList);
		lvShopAllList.setAdapter(shopListAdapter);
		lvShopAllList.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		//toast("点击了： " + position);
		//将当前点击的Shop对象传递给下一个Activity
		Intent toShopItem = new Intent(ShopAllActivity.this, ShopItemActivity.class);
		Bundle bundle = new Bundle();
        bundle.putSerializable("shop", shopList.get(position) );  
        bundle.putString("shopID", shopList.get(position).getObjectId()); //商铺的ID需要单独传递,否则获取到的是null
        Log.i(TAG, ">>发出>>" + "shopID: "+shopList.get(position).getObjectId()+" shopName: "+shopList.get(position).getName());
        toShopItem.putExtras(bundle);
		startActivity(toShopItem);
	}


	/**
	 * 获取商商店列表
	 */
	private void queryShopData() {
		shopList.clear();
		BmobQuery<Shop> query = new BmobQuery<Shop>();
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		//筛选类型
		query.addWhereEqualTo("type",title);
		//执行查询方法
		query.findObjects(new FindListener<Shop>() {
			@Override
			public void done(List<Shop> object, BmobException e) {
				if(e==null){
					toast("查询成功：共"+object.size()+"条数据。");
					// 将本次查询的数据添加到bankCards中
					for (Shop shop : object) {
						shopList.add(shop);
					}
					// 通知Adapter数据更新
					shopListAdapter.refresh((ArrayList<Shop>) shopList);
					shopListAdapter.notifyDataSetChanged();
					// 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
					curPage++;
				}else{
					Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
				}
			}
		});
	}


	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				swipeLayout.setRefreshing(false);
				queryShopData();
			}
		}, 1000);
	};
	
}
