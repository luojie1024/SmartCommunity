package com.way.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.LinearGradient;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.way.main.MyClickListener;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;
import com.way.util.LightInfo;

public class SmartLightAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<LightInfo> mList;
	//private ArrayList<Gizinfo> mList;
	private MyClickListener mListener;

	public SmartLightAdapter(Context mContext, ArrayList<LightInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

//	public SmartLightAdapter(Context mContext, ArrayList<Gizinfo> mList) {
//		this.mContext = mContext;
//		this.mList = mList;
//	}
	
	public void setOnClickListener(MyClickListener mListener){
		this.mListener=mListener;
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {

			convertView = LinearLayout.inflate(mContext,
					R.layout.listview_smart, null);
			viewHolder = new ViewHolder();
			viewHolder.text_id = (TextView) convertView
					.findViewById(R.id.text_id);
			viewHolder.text_name = (TextView) convertView
					.findViewById(R.id.text_name);
			viewHolder.btn_tog =  (ToggleButton) convertView
					.findViewById(R.id.btn_tog);
			viewHolder.btn_tog.setOnClickListener(mOnClickListener);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text_id.setText(""+mList.get(position).getLight_id());
		viewHolder.text_name.setText(mList.get(position).getLight_name());
		viewHolder.btn_tog.setChecked(mList.get(position).isLight_state());
//		viewHolder.text_id.setText(""+mList.get(position).getId());
//		viewHolder.text_name.setText(mList.get(position).getName());
//		viewHolder.btn_tog.setChecked(false);
		viewHolder.btn_tog.setTag(position);
//		 viewHolder.btn_tog.setOnClickListener(new OnClickListener() {
//
//		 public void onClick(View v) {
//		 // TODO Auto-generated method stub
//		 if(((ToggleButton) v).isChecked())
//		 ((ToggleButton) v).setChecked(false);
//		 else
//		((ToggleButton) v).setChecked(true);
//		 
//		 }
//		 });
		return convertView;
	}

	class ViewHolder {
		private TextView text_id;
		private TextView text_name;
//		private TextView btn_tog;
		private ToggleButton btn_tog;
	}
 private View.OnClickListener mOnClickListener=new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mListener!=null){
				int position =(Integer) v.getTag();
				switch(v.getId()){
				case R.id.btn_tog:
					 mListener.onTogButton(SmartLightAdapter.this, v, position);
					break;
				}
			}
		}
	};
}
