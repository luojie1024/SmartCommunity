package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.property.activity.BaoxiushenqingActivity;
import com.property.activity.BaoxiuxiangqingActivity;
import com.property.activity.ChakandatuActivity;
import com.property.activity.FaultDeleteActivity;
import com.property.activity.FaultListEntity;
import com.property.activity.FaultListEntity.data;
import com.property.activity.FaultListEntity.data.gallery;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.property.base.BaseFragment;
import com.property.utils.ScreenUtil;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class YezhubaoxiuFragment extends BaseFragment implements
OnHeaderRefreshListener, OnFooterLoadListener{

	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_add, click = true)
	private TextView tvAdd;
	@BindView(id = R.id.mPullRefreshView)
	private AbPullToRefreshView abPullToRefreshView;
	@BindView(id = R.id.lv_yezhubaoxiu)
	private ListView lvYezhubaoxiu;

	private Gson gson;
	private KJHttp http;
	private boolean loadmore = false;
	private FaultListEntity faultListEntity;
	// 网络请求参数
	private int page = 1;
	private int status;
	private SharedpfTools sharedpfTools;
	private List<data> list;
	private YezhuBaoxiuAdapter adapter;
	
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private int rating = 0;
	private String comment;
	private String fault_id;
	private int type;
	private String uid;
		
	@Override
	public void registerBroadcast() {
		
	}

	@Override
	public void unRegisterBroadcast() {
		
	}

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		return inflater.inflate(R.layout.activity_yezhubaoxiu, null);
	}
	
	@Override
	public void initData() {
		super.initData();
		ivBack.setVisibility(View.GONE);
		tvBack.setVisibility(View.GONE);
		// 设置监听器
		abPullToRefreshView.setOnHeaderRefreshListener(this);
		abPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		abPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		abPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		sharedpfTools = SharedpfTools.getInstance(getActivity());
		uid = sharedpfTools.getUid();
		http = new KJHttp();
		gson = new Gson();
		list = new ArrayList<data>();
		adapter = new YezhuBaoxiuAdapter(getActivity(), list);
		sendpost();	
		Init();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {		
		case R.id.tv_add:
			startActivity(new Intent(getActivity(),
					BaoxiushenqingActivity.class));
			break;
		default:
			break;
		}
	}

	public void sendpost() {
		HttpParams params = new HttpParams();
		params.put("status", status);
		params.put("uid", uid);
		params.put("page", page);
		Log.e("uid", uid);
		Log.e("status", status + "");
		http.post(UrlConnector.FAULT_LIST, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				if (!loadmore) {
					abPullToRefreshView.onHeaderRefreshFinish();
				} else {
					abPullToRefreshView.onFooterLoadFinish();
				}
				Toast.makeText(getActivity(), "请求失败", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("onSuccess", t);
				faultListEntity = gson.fromJson(t, FaultListEntity.class);
				List<data> list1 = faultListEntity.getData();
				if (page == 1) {
					list.clear();
				}
				if (list1 != null && list1.size() > 0) {
					list.addAll(list1);
				}
				adapter.notifyDataSetChanged();
				lvYezhubaoxiu.setAdapter(adapter);
//				lvYezhubaoxiu.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						startActivity(new Intent(getApplicationContext(),BaoxiuxiangqingActivity.class)
//								.putExtra("id", list.get(position).getFault_id()));				
//					}
//				});	
				if (!loadmore) {
					abPullToRefreshView.onHeaderRefreshFinish();
				} else {
					abPullToRefreshView.onFooterLoadFinish();
				}
			}
		});
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		loadmore = true;
		page++;
		sendpost();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		loadmore = false;
		page = 1;
		sendpost();
	}

	public class YezhuBaoxiuAdapter extends BaseAdapter {

		private List<data> list;
		private Context context;
		private KJBitmap bitmap;
		
		public YezhuBaoxiuAdapter(Context context, List<data> list) {
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
						R.layout.list_item_yezhubaoxiu, null);
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
				holder.tvStatus.setText("待处理");
				holder.tvQuxiao.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						context.startActivity(new Intent(context,FaultDeleteActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
							.putExtra("id", list.get(position).getFault_id()));
					}
				});
				holder.tvTixing.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fault_id = list.get(position).getFault_id();
						type = 1;
						push();
					}
				});
				break;
			case 1:
				holder.tvStatus.setText("已处理");
				holder.tvQuxiao.setVisibility(View.GONE);
				holder.tvTixing.setText("催促物业");
				holder.tvTixing.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fault_id = list.get(position).getFault_id();
						type = 2;
						push();
					}
				});
				break;
			case 2:
				holder.tvStatus.setText("已处理");
				holder.tvQuxiao.setVisibility(View.GONE);
				holder.tvTixing.setText("催促物业");
				holder.tvTixing.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fault_id = list.get(position).getFault_id();
						type = 2;
						push();
					}
				});
				break;
			case 3:
				holder.tvStatus.setText("已处理");
				holder.tvQuxiao.setVisibility(View.GONE);
				holder.tvTixing.setText("催促物业");
				holder.tvTixing.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fault_id = list.get(position).getFault_id();
						type = 2;
						push();
					}
				});
				break;
			case 4:				
				holder.tvStatus.setText("已完成");
				holder.tvQuxiao.setVisibility(View.GONE);
				holder.tvTixing.setText("评价");				
				holder.tvTixing.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						fault_id = list.get(position).getFault_id();
						ll_popup.startAnimation(
								AnimationUtils.loadAnimation(getActivity(), R.anim.activity_translate_in));
						pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
					}
				});
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
			@ViewInject(R.id.ll_yezhubaoxiu_item)
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
			@ViewInject(R.id.tv_quxiaobaoxiu)
			private TextView tvQuxiao;
			@ViewInject(R.id.tv_tixingwuye)
			private TextView tvTixing;		
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
			
	}
	
	public void push(){
		HttpParams params = new HttpParams();
		params.put("fault_id", fault_id);
		params.put("uid", uid);
		params.put("type", type);
		http.post(UrlConnector.FAULT_PUSH, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getActivity(), "请求失败",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject object = new JSONObject(t);
					Toast.makeText(getActivity(),object.getString("msg"),Toast.LENGTH_SHORT).show();					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
	
	public void Init() {
		parentView = getActivity().getLayoutInflater().inflate(R.layout.activity_yezhubaoxiu, null);
		pop = new PopupWindow(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.popupwindows_pingjia, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout rlParent = (RelativeLayout) view.findViewById(R.id.parent);
		final ImageView ivFenshu1 = (ImageView) view.findViewById(R.id.iv_fenshu1);
		final ImageView ivFenshu2 = (ImageView) view.findViewById(R.id.iv_fenshu2);
		final ImageView ivFenshu3 = (ImageView) view.findViewById(R.id.iv_fenshu3);
		final ImageView ivFenshu4 = (ImageView) view.findViewById(R.id.iv_fenshu4);
		final ImageView ivFenshu5 = (ImageView) view.findViewById(R.id.iv_fenshu5);
		final EditText etPingjia = (EditText) view.findViewById(R.id.et_popupwindows_pingjia);
		TextView tvOk = (TextView) view.findViewById(R.id.tv_popupwindows_ok);
		TextView tvCancel = (TextView) view.findViewById(R.id.tv_popupwindows_cancel);
		rlParent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		ivFenshu1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivFenshu1.setImageResource(R.drawable.xieyi_yidu);
				ivFenshu2.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu3.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu4.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu5.setImageResource(R.drawable.xieyi_weidu);
				rating = 1;
			}
		});
		ivFenshu2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivFenshu1.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu2.setImageResource(R.drawable.xieyi_yidu);
				ivFenshu3.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu4.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu5.setImageResource(R.drawable.xieyi_weidu);
				rating = 2;
			}
		});
		ivFenshu3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivFenshu1.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu2.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu3.setImageResource(R.drawable.xieyi_yidu);
				ivFenshu4.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu5.setImageResource(R.drawable.xieyi_weidu);
				rating = 3;
			}
		});
		ivFenshu4.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivFenshu1.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu2.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu3.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu4.setImageResource(R.drawable.xieyi_yidu);
				ivFenshu5.setImageResource(R.drawable.xieyi_weidu);
				rating = 4;
			}
		});
		ivFenshu5.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ivFenshu1.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu2.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu3.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu4.setImageResource(R.drawable.xieyi_weidu);
				ivFenshu5.setImageResource(R.drawable.xieyi_yidu);
				rating = 5;
			}
		});
		tvOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				comment = etPingjia.getText().toString().trim();
				if(rating != 0){
					addComment();
					pop.dismiss();
					ll_popup.clearAnimation();
				}else{
					Toast.makeText(getActivity(), "请选择评分",Toast.LENGTH_SHORT).show();
				}				
			}
		});
		tvCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
	}
	
	public void addComment(){
		HttpParams params = new HttpParams();
		params.put("fault_id", fault_id);
		params.put("uid", uid);
		params.put("comment", comment);
		params.put("rating", rating);
		http.post(UrlConnector.FAULT_ADD_COMMENT, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getActivity(), "请求失败",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject object = new JSONObject(t);
					Toast.makeText(getActivity(),object.getString("msg"),Toast.LENGTH_SHORT).show();					
				} catch (JSONException e) {
					e.printStackTrace();
				}				
			}
		});
	}	
	
}
