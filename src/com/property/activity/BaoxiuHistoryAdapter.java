package com.property.activity;

import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.property.activity.BaoxiuDetailEntity.data.history;
import com.way.tabui.gokit.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaoxiuHistoryAdapter extends BaseAdapter {

	private List<history> list;
	private Context context;
	private KJBitmap bitmap;
	
	public BaoxiuHistoryAdapter(Context context, List<history> list) {
		this.list = list;
		this.context = context;
		bitmap = new KJBitmap();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_history, null);
			holder = new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}		
		holder.tvTime.setText(list.get(position).getCreate_time());
		holder.tvContent.setText(list.get(position).getContent());
		return convertView;
	}

	class ViewHolder {	
		@ViewInject(R.id.tv_history_time)
		private TextView tvTime;
		@ViewInject(R.id.tv_history_content)
		private TextView tvContent;
	}

}
