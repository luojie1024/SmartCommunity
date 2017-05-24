package com.way.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.way.main.MyClickListener;
import com.way.tabui.gokit.R;
import com.way.util.Gizinfo;

import java.util.ArrayList;

public class SmartOCAdapter extends BaseAdapter {
	private Context mContext;
//	private ArrayList<LightInfo> mList;
	private ArrayList<Gizinfo> mList;
	private MyClickListener mListener;
    private DatabaseAdapter dbAdapter;
	protected static final int OPEN = 1;
	protected static final int CLOSE = 0;
	Handler handler = new Handler();
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}

    public SmartOCAdapter(Handler handler, Context mContext) {
        this.handler = handler;
        this.dbAdapter =new DatabaseAdapter(mContext);
        this.mContext = mContext;
        this.mList = new ArrayList<Gizinfo>();
    }

    public  SmartOCAdapter(Context mContext, ArrayList<Gizinfo> mList) {
		this.mContext = mContext;
		this.mList = mList;
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
    public SmartOCAdapter setDate(String mac){
        mList=dbAdapter.findbybindgiz(mac);
        if(mList.size()!=0){
        this.notifyDataSetChanged();
            Log.i("smartoc", "setDate:-----> "+mList.size());
        }
        else
            return null;
        return this;
    }
    public ArrayList<Gizinfo> getmList(){
        return mList;
    }

    public SmartOCAdapter deleteDate(int id){
        dbAdapter.delete(id);
        return this;
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

	


}
