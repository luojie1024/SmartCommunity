package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.property.base.BaseActivity;
import com.property.activity.JiaofeiListEntity.pay_record;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class JiaofeiListActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterLoadListener{

	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_jiaofei_title_left, click = true)
	private TextView tvLeft;
	@BindView(id = R.id.tv_jiaofei_title_right, click = true)
	private TextView tvRight;
	@BindView(id = R.id.mPullRefreshView)
	private AbPullToRefreshView abPullToRefreshView;
	@BindView(id = R.id.lv_jiaofei_list)
	private ListView lvJiaofeiList;
	
	private AbHttpUtil http;
	private Gson gson;
	private JiaofeiListEntity jiaofeiListEntity;
	private List<pay_record> list;
	private JiaofeiListAdapter adapter;
	private boolean loadmore = false;
	private SharedpfTools sharedpfTools;
	private String type;
	private int pay_status = 1;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_jiaofei_list);
	}

	@Override
	public void initData() {
		super.initData();
		// 设置监听器
		abPullToRefreshView.setOnHeaderRefreshListener(this);
		abPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		abPullToRefreshView.getHeaderView()
				.setHeaderProgressBarDrawable(getApplication().getResources().getDrawable(R.drawable.progress_circular));
		abPullToRefreshView.getFooterView()
				.setFooterProgressBarDrawable(getApplication().getResources().getDrawable(R.drawable.progress_circular));
		sharedpfTools = SharedpfTools.getInstance(this);
		type = getIntent().getStringExtra("type");
		http = AbHttpUtil.getInstance(this);
		gson = new Gson();
		list = new ArrayList<pay_record>();
		adapter = new JiaofeiListAdapter(getApplication(), list);
		sendpost();
		lvJiaofeiList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(getApplication(),JiaofeiDetailActivity.class)
					.putExtra("id", list.get(position).getId())
					.putExtra("type", type));
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
		case R.id.tv_jiaofei_title_left:
			tvLeft.setBackgroundResource(R.drawable.rounded_linearlayout_left_press);
			tvRight.setBackgroundResource(R.drawable.rounded_linearlayout_right_unpress);
			tvLeft.setTextColor(Color.parseColor("#c0d355"));
			tvRight.setTextColor(Color.WHITE);
			pay_status=1;
			sendpost();
			break;
		case R.id.tv_jiaofei_title_right:
			tvLeft.setBackgroundResource(R.drawable.rounded_linearlayout_left_unpress);
			tvRight.setBackgroundResource(R.drawable.rounded_linearlayout_right_press);
			tvLeft.setTextColor(Color.WHITE);
			tvRight.setTextColor(Color.parseColor("#c0d355"));
			pay_status=2;
			sendpost();
			break;
		default:
			break;
		}
	}
	
	public void sendpost(){
		AbRequestParams params = new AbRequestParams();
		//带入用户名信息访问
		params.put("uid", sharedpfTools.getUid());
		params.put("type", type);
		params.put("pay_status", pay_status);
		http.post(UrlConnector.PAY_RECORD_LIST, params, new AbStringHttpResponseListener() {
			
			@Override
			public void onStart() {
				
			}
			
			@Override
			public void onFinish() {
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1, Throwable arg2) {
				if (!loadmore) {
					abPullToRefreshView.onHeaderRefreshFinish();
				} else {
					abPullToRefreshView.onFooterLoadFinish();
				}
				Toast.makeText(JiaofeiListActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(int arg0, String arg1) {
				jiaofeiListEntity = gson.fromJson(arg1, JiaofeiListEntity.class);
				List<pay_record> list1 = jiaofeiListEntity.getPay_record();
				list.clear();
				if(list1 != null && list1.size()>0){
					list.addAll(list1);
				}
				adapter.notifyDataSetChanged();
				lvJiaofeiList.setAdapter(adapter);
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
		sendpost();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView arg0) {
		loadmore = false;
		sendpost();
	}
}
