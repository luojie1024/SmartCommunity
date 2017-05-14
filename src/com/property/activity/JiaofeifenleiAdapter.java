package com.property.activity;

import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.property.activity.JiaofeifenleiEntity.pay_genre;
import com.way.tabui.gokit.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JiaofeifenleiAdapter extends BaseAdapter {

	private List<pay_genre> list;
	private Context context;
	private KJBitmap bitmap;
	
	public JiaofeifenleiAdapter(Context context, List<pay_genre> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_jiaofeifenlei, null);
			holder = new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		bitmap.display(holder.ivImg, list.get(position).getImg());
		holder.tvName.setText(list.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.iv_jiaofeifenlei_image)
		private ImageView ivImg;
		@ViewInject(R.id.tv_jiaofeifenlei_name)
		private TextView tvName;
	}

}
