package com.property.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import com.property.bean.JiaofeiListEntity;
import com.property.bean.JiaofeiListEntity.pay_record;
import com.property.adapter.JiaofeiListAdapter;
import com.property.base.BaseActivity;
import com.property.utils.SharedpfTools;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.ui.BindView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static org.kymjs.kjframe.ui.ViewInject.toast;

public class JiaofeiListActivity extends BaseActivity implements OnHeaderRefreshListener, OnFooterLoadListener {

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

          //等待框
          private ProgressDialog pd;
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
                    //分配bean空间
                    jiaofeiListEntity=new JiaofeiListEntity();
                    // TODO: 2017/5/14
                    intidata();
//		            updatePay();
                    lvJiaofeiList.setOnItemClickListener(new OnItemClickListener() {

                              @Override
                              public void onItemClick(AdapterView<?> parent, View view,
                                                      int position, long id) {
                                        startActivity(new Intent(getApplication(), JiaofeiDetailActivity.class)
                                             .putExtra("objectId", list.get(position).getObjectId())
                                             .putExtra("pay_status", pay_status)
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
                                        tvLeft.setTextColor(Color.parseColor("#ff3995e3"));
                                        tvRight.setTextColor(Color.WHITE);
                                        pay_status = 1;
                                        //刷新数据，更新UI
                                        intidata();
                                        break;
                              case R.id.tv_jiaofei_title_right:
                                        tvLeft.setBackgroundResource(R.drawable.rounded_linearlayout_left_unpress);
                                        tvRight.setBackgroundResource(R.drawable.rounded_linearlayout_right_press);
                                        tvLeft.setTextColor(Color.WHITE);
                                        tvRight.setTextColor(Color.parseColor("#ff3995e3"));
                                        pay_status = 2;
                                        //刷新数据，更新UI
                                        initData();
                                        break;
                              default:
                                        break;
                    }
          }

          /**
           * 连接数据库，获取数据并生成JiaofeiListEntity,数据集合
           */
          public void intidata() {

                    BmobQuery<pay_record> bmobQuery = new BmobQuery<pay_record>();
                    //设置查询条件
                    bmobQuery.addWhereEqualTo("type",type);
                    if (pay_status == 1) {
                              bmobQuery.addWhereEqualTo("pay_time","0");
                    } else {
                              bmobQuery.addWhereNotEqualTo("pay_time","0");
                    }
                    bmobQuery.findObjects(new FindListener<pay_record>() {
                              @Override
                              public void done(List<pay_record> object, BmobException e) {
                                        if(e==null){
                                                  //查询成功
                                                  jiaofeiListEntity.setStatus(1);
                                                  jiaofeiListEntity.setMsg("");
                                                  jiaofeiListEntity.setPay_record(object);
                                                  toast("查询成功：共"+object.size()+"条数据。");
                                                  updateUI();
//
                                        }else{
                                                  jiaofeiListEntity.setStatus(0);
                                                  Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                              }
                    });

          }

          public void updateUI() {
                    //通过数据库获取数据
                    // TODO: 2017/5/14
                    List<pay_record> list1 = jiaofeiListEntity.getPay_record();
                    list.clear();
                    if (list1 != null && list1.size() > 0) {
                              list.addAll(list1);
                    }
                    adapter.notifyDataSetChanged();
                    lvJiaofeiList.setAdapter(adapter);
                    if (!loadmore) {
                              abPullToRefreshView.onHeaderRefreshFinish();
                    } else {
                              abPullToRefreshView.onFooterLoadFinish();
                    }

//
//		AbRequestParams params = new AbRequestParams();
//		//带入用户名信息访问
//		params.put("uid", sharedpfTools.getUid());
//		params.put("type", type);
//		params.put("pay_status", pay_status);
//		http.post(UrlConnector.PAY_RECORD_LIST, params, new AbStringHttpResponseListener() {
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
//				Toast.makeText(JiaofeiListActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
//			}
//
//			@Override
//			public void onSuccess(int arg0, String arg1) {
//				JiaofeiListActivity.this.jiaofeiListEntity = gson.fromJson(arg1, JiaofeiListEntity.class);
//				List<pay_record> list1 = JiaofeiListActivity.this.jiaofeiListEntity.getPay_record();
//				list.clear();
//				if(list1 != null && list1.size()>0){
//					list.addAll(list1);
//				}
//				adapter.notifyDataSetChanged();
//				lvJiaofeiList.setAdapter(adapter);
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
                    intidata();
          }

          @Override
          public void onHeaderRefresh(AbPullToRefreshView arg0) {
                    loadmore = false;
                    intidata();
          }
}
