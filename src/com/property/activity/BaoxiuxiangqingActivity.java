package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.util.ab;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.property.base.BaseActivity;
import com.property.activity.BaoxiuDetailEntity.data.gallery;
import com.property.activity.BaoxiuDetailEntity.data.history;
import com.property.utils.ScreenUtil;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.property.view.NoScrollListView;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class BaoxiuxiangqingActivity extends BaseActivity {

	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_baoxiu_detail_title)
	private TextView tvTitle;
	@BindView(id = R.id.tv_baoxiu_detail_time)
	private TextView tvTime;
	@BindView(id = R.id.tv_baoxiu_detail_content)
	private TextView tvContent;
	@BindView(id = R.id.hsv_scroll)
	private HorizontalScrollView hsvScroll;
	@BindView(id = R.id.hot_content)
	private LinearLayout llHotContent;
	@BindView(id = R.id.ll_pingjia)
	private LinearLayout llPingjia;
	@BindView(id = R.id.tv_pingjia, click = true)
	private TextView tvPingjia;
	@BindView(id = R.id.lv_history)
	private NoScrollListView lvHistory;
	
	private String id;
	private AbHttpUtil http;
	private Gson gson;
	private BaoxiuDetailEntity baoxiuDetailEntity;
	private KJBitmap bitmap;
	private List<history> list;
	private BaoxiuHistoryAdapter adapter;
	
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	private int rating = 0;
	private String comment;
	private String uid;
	private SharedpfTools sharedpfTools;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_baoxiuxiangqing);
	}

	@Override
	public void initData() {
		super.initData();
		sharedpfTools = SharedpfTools.getInstance(this);
		uid = sharedpfTools.getUid();
		id = getIntent().getStringExtra("id");
		http = AbHttpUtil.getInstance(this);
		gson = new Gson();
		bitmap = new KJBitmap();
		list = new ArrayList<history>();
		adapter = new BaoxiuHistoryAdapter(getApplicationContext(), list);
		sendpost();
		Init();
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
		case R.id.tv_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	public void  sendpost(){
		AbRequestParams params = new AbRequestParams();
		params.put("id", id);
		http.post(UrlConnector.FAULT_DETAIL, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {
				Toast.makeText(getApplication(), "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int arg0, String arg1) {
				baoxiuDetailEntity = gson.fromJson(arg1, BaoxiuDetailEntity.class);
				tvTitle.setText(baoxiuDetailEntity.getData().getTitle());
				tvTime.setText("报修时间："+baoxiuDetailEntity.getData().getAdd_time());
				tvContent.setText(baoxiuDetailEntity.getData().getContent());
				initHotData(baoxiuDetailEntity.getData().getGallery(), llHotContent,hsvScroll);
				if(sharedpfTools.getRole().equals("1") && baoxiuDetailEntity.getData().getFault_status().equals("4")){
					llPingjia.setVisibility(View.VISIBLE);
					tvPingjia.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ll_popup.startAnimation(
									AnimationUtils.loadAnimation(getApplication(), R.anim.activity_translate_in));
							pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
						}
					});
				}
				List<history> list1 = baoxiuDetailEntity.getData().getHistory();
				if(list1 != null && list1.size()>0){
					list.addAll(list1);
				}
				lvHistory.setAdapter(adapter);				
			}
		});
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
							this).inflate(R.layout.item_published_grida,
							null);
					bitmap.display(columnTextView, image.get(i));
					columnTextView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(getApplication(), ChakandatuActivity.class);
							intent.putExtra("position", "1");
							intent.putExtra("ID", 0);
							intent.putStringArrayListExtra("image", image);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);							
						}
					});
					llGroup.addView(
							columnTextView,
							i,
							new LinearLayout.LayoutParams(ScreenUtil
									.getScreenWidth(this)/4-20, ScreenUtil
									.getScreenWidth(this)/4-20));
				}
			}
		}
	}
	
	public void Init() {
		parentView = getLayoutInflater().inflate(R.layout.activity_baoxiuxiangqing, null);
		pop = new PopupWindow(BaoxiuxiangqingActivity.this);
		View view = getLayoutInflater().inflate(R.layout.popupwindows_pingjia, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup1);

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
					Toast.makeText(getApplicationContext(), "请选择评分",Toast.LENGTH_SHORT).show();
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
		AbRequestParams params = new AbRequestParams();
		params.put("fault_id", id);
		params.put("uid", uid);
		params.put("comment", comment);
		params.put("rating", rating);
		http.post(UrlConnector.FAULT_ADD_COMMENT, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {
				Toast.makeText(getApplicationContext(), "请求失败",Toast.LENGTH_SHORT).show();				
			}
			
			@Override
			public void onSuccess(int arg0, String arg1) {
				try {
					JSONObject object = new JSONObject(arg1);
					Toast.makeText(getApplicationContext(),object.getString("msg"),Toast.LENGTH_SHORT).show();					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
