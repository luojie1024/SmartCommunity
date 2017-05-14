package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.property.base.BaseActivity;
import com.property.activity.AnnouncementListEntity.ad_top;
import com.property.activity.AnnouncementListEntity.list;
import com.property.utils.UrlConnector;
import com.property.view.NoScrollListView;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class AnnouncementListActivity extends BaseActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.abPullToRefreshView)
	private AbPullToRefreshView abPullToRefreshView;
	@BindView(id = R.id.abSlidingPlayView)
	private AbSlidingPlayView abSlidingPlayView;
	@BindView(id = R.id.noScrollListView)
	private NoScrollListView noScrollListView;

	private KJHttp http;
	private Gson gson;
	private int page_num = 1;
	private AnnouncementListEntity announcementListEntity;
	private List<list> list;
	private AnnouncementListAdapter adapter;
	private KJBitmap bitmap;
	private boolean loadmore = false;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_gonggao);
	}

	@Override
	public void initData() {
		super.initData();
		// 设置监听器
		abPullToRefreshView.setOnHeaderRefreshListener(this);
		abPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		abPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				getApplication().getResources().getDrawable(
						R.drawable.progress_circular));
		abPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				getApplication().getResources().getDrawable(
						R.drawable.progress_circular));
		http = new KJHttp();
		gson = new Gson();
		bitmap = new KJBitmap();
		list = new ArrayList<list>();
		adapter = new AnnouncementListAdapter(getApplication(), list);
		sendpost();
		noScrollListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(getApplication(),
						AnnouncementInfoActivity.class).putExtra("aid", list
						.get(position).getAid()));
			}
		});
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

	public void sendpost() {
		HttpParams params = new HttpParams();
		params.put("cat_id", 160);
		params.put("page_num", page_num);
		params.put("page_size", 20);
		http.post(UrlConnector.ANNOUNCEMENT_LIST, params, false,
				new HttpCallBack() {
					@Override
					public void onFailure(int errorNo, String strMsg) {
						super.onFailure(errorNo, strMsg);
						if (!loadmore) {
							abPullToRefreshView.onHeaderRefreshFinish();
						} else {
							abPullToRefreshView.onFooterLoadFinish();
						}
						Toast.makeText(AnnouncementListActivity.this, "请求失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						announcementListEntity = gson.fromJson(t,
								AnnouncementListEntity.class);
						List<list> list1 = announcementListEntity.getList();
						if (page_num == 1) {
							list.clear();
						}
						if (list1 != null && list1.size() > 0) {
							list.addAll(list1);
						}
						adapter.notifyDataSetChanged();
						noScrollListView.setAdapter(adapter);
						if (announcementListEntity.getAd_top() != null
								&& announcementListEntity.getAd_top().size() > 0) {
							initLoopPic(announcementListEntity.getAd_top());
						}
						if (!loadmore) {
							abPullToRefreshView.onHeaderRefreshFinish();
						} else {
							abPullToRefreshView.onFooterLoadFinish();
						}
					}
				});
	}

	/**
	 * 轮播图
	 * 
	 * @param list
	 */
	protected void initLoopPic(final List<ad_top> list) {
		abSlidingPlayView.removeAllViews();
		if (list.size() > 0) {
			for (ad_top galleryModel : list) {
				try {
					final View mPlayView = getLayoutInflater().inflate(
							R.layout.play_view_item, null);
					ImageView mPlayImage = (ImageView) mPlayView
							.findViewById(R.id.mPlayImage);
					bitmap.display(mPlayImage, galleryModel.getContent(), 1080,
							720);
					LinearLayout llLunbo = (LinearLayout) mPlayView
							.findViewById(R.id.ll_lunbo);
					llLunbo.setBackgroundResource(R.drawable.lunbo_background);
					ImageView ivTittle = (ImageView) mPlayView
							.findViewById(R.id.iv_lunbo_title);
					ivTittle.setImageResource(R.drawable.lunbo_title);
					TextView mPlayText = (TextView) mPlayView
							.findViewById(R.id.mPlayText);
					mPlayText.setText(galleryModel.getAd_name());
					abSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
					abSlidingPlayView.setNavPageResources(
							R.drawable.lunbo_white, R.drawable.lunbo_gray);
					abSlidingPlayView.addView(mPlayView);
				} catch (Exception e) {
				}
			}
			abSlidingPlayView.startPlay();
		}
		abSlidingPlayView
				.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {

					@Override
					public void onClick(int position) {

					}
				});
	}

	@Override
	public void onFooterLoad(AbPullToRefreshView arg0) {
		loadmore = true;
		page_num += 1;
		sendpost();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		loadmore = false;
		page_num = 1;
		sendpost();
	}
}
