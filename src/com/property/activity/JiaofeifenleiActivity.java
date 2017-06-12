package com.property.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.google.gson.Gson;
import com.property.bean.JiaofeifenleiEntity;
import com.property.bean.JiaofeifenleiEntity.pay_genre;
import com.property.adapter.JiaofeifenleiAdapter;
import com.property.base.BaseActivity;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

public class JiaofeifenleiActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterLoadListener {

          @BindView(id = R.id.iv_back, click = true)
          private ImageView ivBack;
          @BindView(id = R.id.tv_back, click = true)
          private TextView tvBack;
          @BindView(id = R.id.abPullToRefreshView)
          private AbPullToRefreshView abPullToRefreshView;
          @BindView(id = R.id.lv_jiaofeifenlei)
          private ListView lvJiaofeifenlei;

          private AbHttpUtil http;
          private Gson gson;
          private JiaofeifenleiEntity jiaofeifenleiEntity;
          private List<pay_genre> list;
          private JiaofeifenleiAdapter adapter;
          private boolean loadmore = false;


          @Override
          public void setRootView() {
                    setContentView(R.layout.activity_jiaofei_fenlei);
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
                    http = AbHttpUtil.getInstance(this);
                    list = new ArrayList<pay_genre>();
                    adapter = new JiaofeifenleiAdapter(getApplication(), list);
                    applyData();
                    lvJiaofeifenlei.setOnItemClickListener(new OnItemClickListener() {

                              @Override
                              public void onItemClick(AdapterView<?> parent, View view,
                                                      int position, long id) {
                                        startActivity(new Intent(getApplication(), JiaofeiListActivity.class)
                                             .putExtra("type", list.get(position).getId()));
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

          public void applyData() {
                    String[][]data = {{"1", "水费", "http://121.40.20.207:3018/site/static/goods/source/201605131339503922.png"},
                         {"2", "电费", "http://121.40.20.207:3018/site/static/goods/source/201605131340055936.png"},
                         {"3", "房租费", "http://121.40.20.207:3018/site/static/goods/source/201605131340251146.png"},
                         {"4", "物业费", "http://121.40.20.207:3018/site/static/goods/source/201605131340363221.png"},
                         {"5", "停车费", "http://121.40.20.207:3018/site/static/goods/source/201605131340483826.png"}};
                    //加载数据

                    jiaofeifenleiEntity=new JiaofeifenleiEntity();
                    List<pay_genre> list1 = new ArrayList<pay_genre>();
                    for (int i = 0; i < 5; i++) {
                              JiaofeifenleiEntity.pay_genre pay_g=jiaofeifenleiEntity.new pay_genre();
                              pay_g.setId(data[i][0]);
                              pay_g.setName(data[i][1]);
                              pay_g.setImg(data[i][2]);
                              list1.add(pay_g);
                    }
                    list.clear();
                    if (list1 != null && list1.size() > 0) {
                              list.addAll(list1);
                    }
                    adapter.notifyDataSetChanged();
                    lvJiaofeifenlei.setAdapter(adapter);
                    if (!loadmore) {
                              abPullToRefreshView.onHeaderRefreshFinish();
                    } else {
                              abPullToRefreshView.onFooterLoadFinish();
                    }

                    //连接服务器取数据
//		http.get(UrlConnector.PAY_GENRE_LIST, new AbStringHttpResponseListener() {
//
//			@Override
//			public void onStart() {
//
//			}
//
//			@Override
//			public void onFinish() {
//
//			}
//
//			@Override
//			public void onFailure(int arg0, String arg1, Throwable arg2) {
//				if (!loadmore) {
//					abPullToRefreshView.onHeaderRefreshFinish();
//				} else {
//					abPullToRefreshView.onFooterLoadFinish();
//				}
//				Toast.makeText(JiaofeifenleiActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onSuccess(int arg0, String arg1) {
//				jiaofeifenleiEntity = gson.fromJson(arg1, JiaofeifenleiEntity.class);
//				List<pay_genre> list1 = jiaofeifenleiEntity.getPay_genre();
//				list.clear();
//				if(list1 != null && list1.size()>0){
//					list.addAll(list1);
//				}
//				adapter.notifyDataSetChanged();
//				lvJiaofeifenlei.setAdapter(adapter);
//				if (!loadmore) {
//					abPullToRefreshView.onHeaderRefreshFinish();
//				} else {
//					abPullToRefreshView.onFooterLoadFinish();
//				}
//			}
//		});
          }

          @Override
          public void onFooterLoad(AbPullToRefreshView arg0) {
                    loadmore = true;
                    applyData();
          }

          @Override
          public void onHeaderRefresh(AbPullToRefreshView arg0) {
                    loadmore = false;
                    applyData();
          }
}
