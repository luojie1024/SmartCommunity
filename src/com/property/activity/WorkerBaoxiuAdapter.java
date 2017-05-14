package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import com.property.activity.FaultListEntity.data;
import com.property.activity.FaultListEntity.data.gallery;
import com.property.utils.ScreenUtil;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WorkerBaoxiuAdapter extends BaseAdapter {

	private List<data> list;
	private Context context;
	private KJBitmap bitmap;
	private KJHttp http;
	private String id;
	private String uid;
	private int status;
	private SharedpfTools sharedpfTools;
	
	public WorkerBaoxiuAdapter(Context context, List<data> list) {
		this.list = list;
		this.context = context;
		bitmap = new KJBitmap();
		http = new KJHttp();
		sharedpfTools = SharedpfTools.getInstance(context);
		uid = sharedpfTools.getUid();
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
					R.layout.list_item_worker_baoxiu, null);
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
		case 1:
			holder.tvStatus.setText("待分配");
			holder.tvJiedan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					id = list.get(position).getFault_id();
					status = 1;
					changeProgress();
				}
			});
			break;
		case 2:
			holder.tvStatus.setText("待维修");
			holder.tvJiedan.setText("开始维修");
			holder.tvJiedan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					id = list.get(position).getFault_id();
					status = 2;
					changeProgress();
				}
			});
			break;
		case 3:
			holder.tvStatus.setText("维修中");
			holder.tvJiedan.setText("完成");
			holder.tvTianxiejindu.setVisibility(View.VISIBLE);
			holder.tvJiedan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					id = list.get(position).getFault_id();
					status = 4;
					changeProgress();
				}
			});
			holder.tvTianxiejindu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					context.startActivity(new Intent(context,FaultProgressActivity.class)
					.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
					.putExtra("id", list.get(position).getFault_id()));
				}
			});
			break;
		case 4:
			holder.tvStatus.setText("已完成");
			holder.tvJiedan.setVisibility(View.GONE);
			holder.tvWorkerTime.setVisibility(View.VISIBLE);
			holder.tvWorkerTime.setText("完成时间：" + list.get(position).getAdd_time());
			holder.tvWorkerTime.setTextColor(Color.parseColor("#999999"));
			break;
		default:
			break;
		}
		initHotData(list.get(position).getGallery(), holder.llHotContent,
				holder.hsvScroll);
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
		@ViewInject(R.id.tv_worker_jiedan)
		private TextView tvJiedan;
		@ViewInject(R.id.tv_worker_tianxiejindu)
		private TextView tvTianxiejindu;
		@ViewInject(R.id.tv_worker_time)
		private TextView tvWorkerTime;
		@ViewInject(R.id.ll_workerbaoxiu_item)
		private LinearLayout llItem;
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
							Intent intent = new Intent(context, ChakandatuActivity.class);
							intent.putExtra("position", "1");
							intent.putExtra("ID", 0);
							intent.putStringArrayListExtra("image", image);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);							
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
	
	public void changeProgress(){
		HttpParams params = new HttpParams();
		params.put("id", id);
		params.put("uid", uid);
		params.put("status", status);
		Log.e("11111", "id="+id+"&uid="+uid+"&status="+status);
		http.post(UrlConnector.FAULT_PROGRESS, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);				
				Toast.makeText(context, "请求失败",
						Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject object = new JSONObject(t);
					Toast.makeText(context, object.getString("msg"),
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
		});
	}
}
