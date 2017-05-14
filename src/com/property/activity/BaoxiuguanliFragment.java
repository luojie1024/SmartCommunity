package com.property.activity;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.property.base.BaseFragment;
import com.property.bean.FaultListEntity;
import com.property.bean.FaultListEntity.data;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class BaoxiuguanliFragment extends BaseFragment implements
		OnHeaderRefreshListener, OnFooterLoadListener {
	
	private Gson gson;
	private KJHttp kjHttp;
	private boolean loadmore = false;
	private FaultListEntity faultListEntity;
	// 网络请求参数
	private int page = 1;
	private int status;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView listview;
	private SharedpfTools sharedpfTools;
	private List<data> list;
	private BaoxiuguanliAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_baoxiuguanli,
				container, false);		
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) rootView
				.findViewById(R.id.mPullRefreshView);
		listview = (ListView) rootView.findViewById(R.id.lv_fault_list);
		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		sharedpfTools = SharedpfTools.getInstance(getActivity());
		if (getArguments() != null) {
			status = getArguments().getInt("status", 0);
		}
		kjHttp = new KJHttp();
		gson = new Gson();
		list = new ArrayList<data>();
		adapter = new BaoxiuguanliAdapter(getActivity(), list);
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		sendpost();
	}
	
	public void sendpost(){
		HttpParams params = new HttpParams();
		params.put("status", status);
		params.put("uid", sharedpfTools.getUid());
		params.put("page", page);
		Log.e("uid", sharedpfTools.getUid());
		Log.e("status", status+"");
		kjHttp.post(UrlConnector.FAULT_LIST, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				if (!loadmore) {
					mAbPullToRefreshView.onHeaderRefreshFinish();
				} else {
					mAbPullToRefreshView.onFooterLoadFinish();
				}
				Toast.makeText(getActivity(), "请求失败",
						Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Log.e("onSuccess", t);
				faultListEntity = gson.fromJson(t, FaultListEntity.class);
				List<data> list1 = faultListEntity.getData();
				if(page==1){
					list.clear();
				}
				if(list1 != null && list1.size()>0){
					list.addAll(list1);
				}
				adapter.notifyDataSetChanged();
				listview.setAdapter(adapter);
				if (!loadmore) {
					mAbPullToRefreshView.onHeaderRefreshFinish();
				} else {
					mAbPullToRefreshView.onFooterLoadFinish();
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

	@Override
	public void registerBroadcast() {
		
	}

	@Override
	public void unRegisterBroadcast() {
		
	}

	@Override
	protected View inflaterView(LayoutInflater inflater, ViewGroup container,
			Bundle bundle) {
		return null;
	}

}
