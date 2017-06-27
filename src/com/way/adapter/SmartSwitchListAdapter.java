package com.way.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.way.tabui.gokit.R;
import com.way.util.SwitchInfo;

import java.util.ArrayList;

public class SmartSwitchListAdapter extends BaseAdapter {

	private Context mContext;
	//	private ArrayList<LightInfo> mList;
	private ArrayList<SwitchInfo> mList;

	private DatabaseAdapter dbAdapter;
	protected static final int UPDATA = 99;
	protected static final int DELETE = 100;
	Handler handler = new Handler();

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public SmartSwitchListAdapter(Handler handler, Context mContext) {
		this.handler = handler;
		this.dbAdapter =new DatabaseAdapter(mContext);
		this.mContext = mContext;
		this.mList = new ArrayList<SwitchInfo>();
	}

	public SmartSwitchListAdapter(Context mContext, ArrayList<SwitchInfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
	}

	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}


	//MAC地址
	public SmartSwitchListAdapter setDate(String mac){
		mList=dbAdapter.findbybindgizSwitchInfo(mac);
		if(mList.size()!=0){
			this.notifyDataSetChanged();
			Log.i("smartoc", "setDate:-----> "+mList.size());
		}
		else
			return null;
		return this;
	}
	public SmartSwitchListAdapter updateList(SwitchInfo switchInfo){
		if(switchInfo!=null){
			dbAdapter.updateSwitchInfo(switchInfo);
			return this;
		}
		return null;
	}

	public ArrayList<SwitchInfo> getmList(){
		return mList;
	}

	public SmartSwitchListAdapter deleteDate(int id){
		dbAdapter.deleteSwitchInfo(id);
		return this;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SmartSwitchListAdapter.ViewHolder viewHolder =null;
		if(convertView==null){
			convertView = RelativeLayout.inflate(mContext, R.layout.listview_aircon_mes, null);
			viewHolder = new SmartSwitchListAdapter.ViewHolder();
			viewHolder.tvAirName=(TextView) convertView.findViewById(R.id.tvAirName);
			viewHolder.bt_update=(Button) convertView.findViewById(R.id.bt_update);
			viewHolder.bt_delete=(Button) convertView.findViewById(R.id.bt_delete);
			viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (SmartSwitchListAdapter.ViewHolder) convertView.getTag();
		}
		final int index;
		index=position;
		switch (mList.get(position).getType()) {
			case 1:
				viewHolder.imageView1.setImageResource(R.drawable.ic_one_switch);
				break;
			case 2:
				viewHolder.imageView1.setImageResource(R.drawable.ic_two_switch);
				break;
			case 3:
				viewHolder.imageView1.setImageResource(R.drawable.ic_three_switch);
				break;
			case 4:
				viewHolder.imageView1.setImageResource(R.drawable.ic_four_switch);
				break;
		}
		viewHolder.tvAirName.setText(mList.get(position).getName());
		viewHolder.bt_update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what =UPDATA;
				message.arg1 = mList.get(index).get_id();
				message.obj=mList.get(index);
				handler.sendMessage(message);
			}
		});
		viewHolder.bt_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
		private ImageView imageView1;
		private Button bt_update;
		private Button bt_delete;
	}

}
