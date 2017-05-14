package com.way.adapter;

import java.util.ArrayList;

import com.way.tabui.gokit.R;
import com.way.util.Allmesinfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AllmesAdapter extends BaseAdapter {

	private ArrayList<Allmesinfo> mList;
	private Context mContext;
	
	public AllmesAdapter(Context mContext,ArrayList<Allmesinfo> mList) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder =null;
		if(convertView==null){
			convertView = RelativeLayout.inflate(mContext, R.layout.listview_all_mes, null);
			viewHolder = new ViewHolder();
			viewHolder.im_mes_ic =(ImageView) convertView.findViewById(R.id.im_mes_ic);
			viewHolder.tv_text1 =(TextView) convertView.findViewById(R.id.tv_text1);
			viewHolder.tv_text2 =(TextView) convertView.findViewById(R.id.tv_text2);
			viewHolder.tv_stuas =(TextView) convertView.findViewById(R.id.tv_stuas);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.im_mes_ic.setImageResource(mList.get(position).getId());
		viewHolder.tv_text1.setText(mList.get(position).getName());
		viewHolder.tv_text2.setText(mList.get(position).getDevicename());
		viewHolder.tv_stuas.setText(mList.get(position).getMes());
		
		
		return convertView;
	}

	class ViewHolder{
		private ImageView im_mes_ic;
		private TextView tv_text1;
		private TextView tv_text2;
		private TextView tv_stuas;
		
	}
}
