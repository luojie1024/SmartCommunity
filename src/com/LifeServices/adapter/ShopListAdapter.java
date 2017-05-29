package com.LifeServices.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.LifeServices.bean.Shop;
import com.way.tabui.gokit.R;

import java.util.ArrayList;

/**
 * 适配器--适配某一分类下的店铺列表数据
 * 
 * @date 2014-4-29
 * @author Stone
 */
public class ShopListAdapter extends BaseAdapter {

	@SuppressWarnings("unused")
	private Context mContext;
	
	private LayoutInflater mInflater = null;
	private ArrayList<Shop> mShopList = null; // 所选分类下的所有店铺列表
	private String mType; // 商店的分类

	public ShopListAdapter(Context context, ArrayList<Shop> shopList,
						   String type) {
		mContext = context;
		mShopList = shopList;
		mType = type;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mShopList.size();
	}

	@Override
	public Object getItem(int position) {
		return mShopList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 刷新列表中的数据
	public void refresh(ArrayList<Shop> list) {
		mShopList = list;
		//将数字的类型编号转换为文字
//		exchangeType(mType);
		notifyDataSetChanged();
	}

//	/**
//	 * 根据当前的type类型, 转换成相应的文字
//	 * @date 2014-4-29
//	 * @param typeString
//	 */
//	private void exchangeType(String typeString) {
//
//		int type = Integer.parseInt(typeString);
//		int fatherType = type / 10; //父类型编号
//		int sonType = type % 10;	//子类型编号
//
//		Iterator<Shop> iterator = mShopList.iterator();
//		while (iterator.hasNext()) {
//			switch (fatherType) {
//			case 1:
//				iterator.next().setType(
//						TypeDef.typeDadList[fatherType-1] + "/"
//								+ TypeDef.typeSonList1[sonType-1]);
//				break;
//			case 2:
//				iterator.next().setType(
//						TypeDef.typeDadList[fatherType-1] + "/"
//								+ TypeDef.typeSonList2[sonType-1]);
//				break;
//			case 3:
//				iterator.next().setType(
//						TypeDef.typeDadList[fatherType-1] + "/"
//								+ TypeDef.typeSonList3[sonType-1]);
//				break;
//			case 4:
//				iterator.next().setType(
//						TypeDef.typeDadList[fatherType-1] + "/"
//								+ TypeDef.typeSonList4[sonType-1]);
//				break;
//			default:
//				break;
//			}
//
//		}
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShopHolder shopHodler;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.shop_all_list_item, null);
			shopHodler = new ShopHolder();
			shopHodler.tvShopName = (TextView) convertView
					.findViewById(R.id.tv_shop_name);
			shopHodler.tvShopType = (TextView) convertView
					.findViewById(R.id.tv_shop_type);
			shopHodler.tvShopLoc = (TextView) convertView
					.findViewById(R.id.tv_shop_loc);
			convertView.setTag(shopHodler);
		} else {
			shopHodler = (ShopHolder) convertView.getTag();
		}
		shopHodler.tvShopName.setText(mShopList.get(position).getName());
		// 商店的类型需要单独处理
		shopHodler.tvShopType.setText(mShopList.get(position).getInfo());
		shopHodler.tvShopLoc.setText(mShopList.get(position).getLocation());
		return convertView;
	}

}
