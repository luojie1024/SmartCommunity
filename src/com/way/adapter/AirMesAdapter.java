package com.way.adapter;

import java.util.ArrayList;

import com.way.adapter.AllmesAdapter.ViewHolder;
import com.way.tabui.gokit.R;
import com.way.util.AirMesinfo;
import com.way.util.Allmesinfo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AirMesAdapter extends BaseAdapter{
	private ArrayList<AirMesinfo> mList;
	private Context mContext;
	protected static final int UPDATA = 99;
	protected static final int DELETE = 100;
	
	Handler handler = new Handler();
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	public AirMesAdapter(ArrayList<AirMesinfo> mList, Context mContext) {
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
			convertView = RelativeLayout.inflate(mContext, R.layout.listview_aircon_mes, null);
			viewHolder = new ViewHolder();
			viewHolder.tvAirName=(TextView) convertView.findViewById(R.id.tvAirName);
			viewHolder.bt_update=(Button) convertView.findViewById(R.id.bt_update);
			viewHolder.bt_delete=(Button) convertView.findViewById(R.id.bt_delete);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final int index;
		index=position;
		viewHolder.tvAirName.setText(mList.get(position).getName());
		viewHolder.bt_update.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what =UPDATA;
				message.arg1 = mList.get(index).get_id();
				message.obj=mList.get(index);
				handler.sendMessage(message);
				
			}
		});
		viewHolder.bt_delete.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what =DELETE;
				message.arg1 = mList.get(index).get_id();
				handler.sendMessage(message);	
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		private TextView tvAirName;
		private Button bt_update;
		private Button bt_delete;
		
	}

}
