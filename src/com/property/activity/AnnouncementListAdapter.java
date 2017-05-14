package com.property.activity;

import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.property.activity.AnnouncementListEntity.list;
import com.way.tabui.gokit.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AnnouncementListAdapter extends BaseAdapter {

	private List<list> list;
	private Context context;
	private KJBitmap bitmap;
	
	public AnnouncementListAdapter(Context context, List<list> list) {
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
			convertView = LayoutInflater.from(context).inflate(R.layout.list_item_gonggao, null);
			holder = new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth()/5;
		int height = width/5*4;
		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.ivImg.getLayoutParams();
		linearParams.width = width;
		linearParams.height = height; 		  		  
		holder.ivImg.setLayoutParams(linearParams);
		bitmap.display(holder.ivImg, list.get(position).getImg());
		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvTime.setText(list.get(position).getUpdate_time());
		switch (Integer.parseInt(list.get(position).getTag())) {
		case 1:
			holder.ivTag.setImageResource(R.drawable.gonggao_zuixin);
			break;
		case 2:
			holder.ivTag.setImageResource(R.drawable.gonggao_zuire);
			break;
		case 3:
			holder.ivTag.setImageResource(R.drawable.gonggao_xinwen);
			break;
		default:
			break;
		}
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.iv_gonggao_img)
		private ImageView ivImg;
		@ViewInject(R.id.tv_gonggao_title)
		private TextView tvTitle;
		@ViewInject(R.id.tv_gonggao_time)
		private TextView tvTime;
		@ViewInject(R.id.iv_gonggao_tag)
		private ImageView ivTag;
	}
}
