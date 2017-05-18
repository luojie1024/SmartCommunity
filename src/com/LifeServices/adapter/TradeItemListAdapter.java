package com.LifeServices.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.LifeServices.bean.SecondTrade;
import com.way.tabui.gokit.R;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * 适配器--适配二手交易物品列表数据
 * @date 2014-9-15
 * @author Stone
 */
public class TradeItemListAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private ArrayList<SecondTrade> mTradeItemList = null; //物品列表
	private TradeItemHolder tradeItemHodler;

	public TradeItemListAdapter(Context context, ArrayList<SecondTrade> tradeItemList) {
		mContext = context;
		mTradeItemList = tradeItemList;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mTradeItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mTradeItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 刷新列表中的数据
	public void refresh(ArrayList<SecondTrade> list) {
		mTradeItemList = list;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.second_trade_all_list_item, null);
			tradeItemHodler = new TradeItemHolder();
			tradeItemHodler.imgTradeItem = (ImageView) convertView
					.findViewById(R.id.img_trade_item);
			tradeItemHodler.tvTradeItemName = (TextView) convertView
					.findViewById(R.id.tv_trade_item_name);
			tradeItemHodler.tvTradeItemType = (TextView) convertView
					.findViewById(R.id.tv_trade_item_type);
			tradeItemHodler.tvTradeItemPrice = (TextView) convertView
					.findViewById(R.id.tv_trade_item_price);
			tradeItemHodler.tvTradeItemOwner = (TextView) convertView
					.findViewById(R.id.tv_trade_item_owner);
			tradeItemHodler.tvTradeItemTime = (TextView) convertView
					.findViewById(R.id.tv_trade_item_time);
			convertView.setTag(tradeItemHodler);
		} else {
			tradeItemHodler = (TradeItemHolder) convertView.getTag();
		}
		//加载缩略图
		if(mTradeItemList.get(position).getPicTradeItem()!=null)
			mTradeItemList.get(position).getPicTradeItem().download(new DownloadFileListener() {
				@Override
				public void done(String s, BmobException e) {
					String fileName = s;
					Bitmap bm = BitmapFactory.decodeFile(fileName);
					tradeItemHodler.imgTradeItem.setImageBitmap(bm);
				}

				@Override
				public void onProgress(Integer integer, long l) {

				}
			});
		tradeItemHodler.tvTradeItemName.setText(mTradeItemList.get(position).getItem());
		tradeItemHodler.tvTradeItemType.setText(mTradeItemList.get(position).getType());
		tradeItemHodler.tvTradeItemPrice.setText("￥ " + mTradeItemList.get(position).getPrice());
		tradeItemHodler.tvTradeItemOwner.setText(mTradeItemList.get(position).getOwner());
		tradeItemHodler.tvTradeItemTime.setText(mTradeItemList.get(position).getCreatedAt());
		return convertView;
	}
	
}
