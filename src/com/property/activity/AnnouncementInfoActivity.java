package com.property.activity;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.property.base.BaseActivity;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class AnnouncementInfoActivity extends BaseActivity {

	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_gonggao_detail_title)
	private TextView tvTitle;
	@BindView(id = R.id.tv_gonggao_detail_time)
	private TextView tvTime;
	@BindView(id = R.id.iv_gonggao_detail_img)
	private ImageView ivImg;
	@BindView(id = R.id.wv_gonggao_detail_content)
	private WebView wvContent;
	
	private KJHttp http;
	private Gson gson;
	private AnnouncementInfoEntity announcementInfoEntity;
	private KJBitmap bitmap;
	private String aid;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_gonggao_detail);
	}

	@Override
	public void initData() {
		super.initData();
		http = new KJHttp();
		gson = new Gson();
		bitmap = new KJBitmap();
		aid = getIntent().getStringExtra("aid");
		sendpost();
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
	
	public void sendpost(){
		HttpParams params = new HttpParams();
		params.put("aid", aid);
		http.post(UrlConnector.ANNOUNCEMENT_INFO, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);				
				Toast.makeText(AnnouncementInfoActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				announcementInfoEntity = gson.fromJson(t, AnnouncementInfoEntity.class);
				tvTitle.setText(announcementInfoEntity.getAnnouncement_info().getTitle());
				tvTime.setText(announcementInfoEntity.getAnnouncement_info().getUpdate_time());
				bitmap.display(ivImg, announcementInfoEntity.getAnnouncement_info().getImg());
				wvContent.loadData(announcementInfoEntity.getAnnouncement_info().getContent(), "text/html; charset=UTF-8", null);
			}
		});
	}
}
