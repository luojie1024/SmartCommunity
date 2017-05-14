package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJBitmap;

import com.property.bean.FaultListEntity.data;
import com.property.bean.FaultListEntity.data.gallery;
import com.property.utils.ScreenUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.way.tabui.gokit.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaoxiuguanliAdapter extends BaseAdapter {

	private List<data> list;
	private Context context;
	private KJBitmap bitmap;

	public BaoxiuguanliAdapter(Context context, List<data> list) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_item_baoxiuguanli, null);
			holder = new ViewHolder();
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvTitle.setText(list.get(position).getTitle());
		holder.tvTime.setText("报修时间：" + list.get(position).getAdd_time());
		holder.tvContent.setText(list.get(position).getContent());
		switch (Integer.valueOf(list.get(position).getStatus())) {
		case 0:
			holder.tvStatus.setText("待分配");
			holder.tvFenpei.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//TODO 跳转页面
//					context.startActivity(new Intent(context,
//							FaultWorkerActivity.class)
//							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//							.putExtra("id",list.get(position).getFault_id()));
				}
			});
			break;
		case 1:
			holder.tvStatus.setText("待维修");
			holder.llOperation.setVisibility(View.GONE);
			holder.tvWorker.setVisibility(View.VISIBLE);
			holder.tvWorker.setText("已分配给维修工人"
					+ list.get(position).getWorker_name());
			break;
		case 2:
			holder.tvStatus.setText("已接单");
			holder.llOperation.setVisibility(View.GONE);
			holder.tvWorker.setVisibility(View.VISIBLE);
			holder.tvWorker.setText(list.get(position).getWorker_name()+"已接单");
			break;
		case 3:
			holder.tvStatus.setText("维修中");
			holder.tvshanchu.setVisibility(View.GONE);
			holder.tvFenpei.setText("查看进度");
			holder.tvFenpei.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.startActivity(new Intent(context,BaoxiuxiangqingActivity.class)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
						.putExtra("id", list.get(position).getFault_id()));
				}
			});
			break;
		case 4:
			holder.tvStatus.setText("已完成");
			holder.llOperation.setVisibility(View.GONE);
			holder.tvWorker.setVisibility(View.VISIBLE);
			holder.tvWorker.setText("完成时间：" + list.get(position).getAdd_time());
			holder.tvWorker.setTextColor(Color.parseColor("#999999"));
			break;
		default:
			break;
		}
		initHotData(list.get(position).getGallery(), holder.llHotContent,
				holder.hsvScroll);
		holder.tvshanchu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO 跳转页面
//				context.startActivity(new Intent(context,
//						FaultDeleteActivity.class)
//						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//						.putExtra("id",list.get(position).getFault_id()));
			}
		});
		holder.llItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context,BaoxiuxiangqingActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
					.putExtra("id", list.get(position).getFault_id()));	
			}
		});
		return convertView;
	}

	class ViewHolder {
		@ViewInject(R.id.ll_baoxiuguanli_item)
		private LinearLayout llItem;
		@ViewInject(R.id.tv_baoxiu_title)
		private TextView tvTitle;
		@ViewInject(R.id.tv_baoxiu_status)
		private TextView tvStatus;
		@ViewInject(R.id.tv_baoxiu_time)
		private TextView tvTime;
		@ViewInject(R.id.tv_baoxiu_content)
		private TextView tvContent;
		@ViewInject(R.id.hsv_scroll)
		private HorizontalScrollView hsvScroll;
		@ViewInject(R.id.hot_content)
		private LinearLayout llHotContent;
		@ViewInject(R.id.tv_shanchudingdan)
		private TextView tvshanchu;
		@ViewInject(R.id.tv_fenpeigongren)
		private TextView tvFenpei;
		@ViewInject(R.id.tv_worker_name)
		private TextView tvWorker;
		@ViewInject(R.id.ll_operation)
		private LinearLayout llOperation;
	}

	private void initHotData(final List<gallery> gallery,
			final LinearLayout llGroup, final HorizontalScrollView hsvScroll) {
		llGroup.removeAllViews();
		if (gallery != null) {
			int count = gallery.size();
			if (count > 0) {
				final ArrayList<String> image = new ArrayList<String>();
				for (int i = 0; i < count; i++) {
					image.add(gallery.get(i).getImg_source());
				}
				for (int i = 0; i < count; i++) {
					ImageView columnTextView = (ImageView) LayoutInflater.from(
							context).inflate(R.layout.item_published_grida,
							null);
					bitmap.display(columnTextView, image.get(i));
					columnTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//TODO 跳转到查看状态
//							Intent intent = new Intent(context, ChakandatuActivity.class);
//							intent.putExtra("position", "1");
//							intent.putExtra("ID", 0);
//							intent.putStringArrayListExtra("image", image);
//							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							context.startActivity(intent);							
						}
					});
					llGroup.addView(
							columnTextView,
							i,
							new LinearLayout.LayoutParams(ScreenUtil
									.getScreenWidth(context)/4-20, ScreenUtil
									.getScreenWidth(context)/4-20));
				}
			}
		}
	}
}
