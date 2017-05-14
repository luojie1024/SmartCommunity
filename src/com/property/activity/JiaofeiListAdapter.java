package com.property.activity;

import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.property.activity.JiaofeiListEntity.pay_record;
import com.way.tabui.gokit.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JiaofeiListAdapter extends BaseAdapter {

	private List<pay_record> list;
	private Context context;
	
	public JiaofeiListAdapter(Context context, List<pay_record> list) {
		this.list = list;
		this.context = context;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_jiaofei_list, null);
			holder = new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Log.e("pay_amount", list.get(position).getPay_amount());
		switch (Integer.parseInt(list.get(position).getType())) {
		case 1:
			holder.tvName.setText("水费");
			break;
		case 2:
			holder.tvName.setText("电费");
			break;
		case 5:
			holder.tvName.setText("房租费");
			break;
		case 6:
			holder.tvName.setText("物业费");
			break;
		case 7:
			holder.tvName.setText("停车费");
			break;
		case 8:
			holder.tvName.setText("其他费用");
			break;
		default:
			break;
		}
		holder.tvTime.setText(list.get(position).getPay_time());
		holder.tvMonth.setText(list.get(position).getMonth());
		holder.tvMoney.setText(list.get(position).getPay_amount());
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.tv_type_name)
		private TextView tvName;
		@ViewInject(R.id.tv_pay_time)
		private TextView tvTime;
		@ViewInject(R.id.tv_pay_month)
		private TextView tvMonth;
		@ViewInject(R.id.tv_pay_money)
		private TextView tvMoney;
	}


}
