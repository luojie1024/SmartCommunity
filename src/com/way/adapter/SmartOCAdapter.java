package com.way.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.way.main.MyClickListener;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;

public class SmartOCAdapter extends BaseAdapter {
	private Context mContext;
//	private ArrayList<LightInfo> mList;
	private ArrayList<Gizinfo> mList;
	private MyClickListener mListener;

	protected static final int OPEN = 1;
	protected static final int CLOSE = 0;
	Handler handler = new Handler();
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public  SmartOCAdapter(Context mContext, ArrayList<Gizinfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}
	
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
			viewHolder.btn_tog = (Switch) convertView
					.findViewById(R.id.btn_tog);
			viewHolder.btn_tog.setOnClickListener(mOnClickListener);
			final int index=position;
			viewHolder.btn_tog.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					if(isChecked){
						Message message = new Message();
						message.what = OPEN;
						message.arg1=index;
						handler.sendMessage(message);
					}else{
						Message message = new Message();
						message.what = CLOSE;
						message.arg1=index;
						handler.sendMessage(message);
					}
				}
			});
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text_id.setText(" "+mList.get(position).getId());
		viewHolder.text_name.setText(mList.get(position).getName());
		viewHolder.btn_tog.setChecked(false);
		viewHolder.btn_tog.setTag(position);
		return convertView;
	}

	class ViewHolder {
		private TextView text_id;
		private TextView text_name;
//		private TextView btn_tog;
		private Switch btn_tog;
	}
 private View.OnClickListener mOnClickListener=new View.OnClickListener() {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(mListener!=null){
				int position =(Integer) v.getTag();
				switch(v.getId()){
				case R.id.btn_tog:
					 mListener.onTogButton(SmartOCAdapter.this, v, position);
					break;
				}
			}
		}
	};
	


}
