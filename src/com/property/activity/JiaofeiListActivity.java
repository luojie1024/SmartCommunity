package com.property.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
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
import com.property.activity.JiaofeiListEntity.pay_record;
import com.property.base.BaseActivity;
import com.property.utils.QueryAll;
import com.property.utils.SharedpfTools;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.ui.BindView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

          private AbHttpUtil http;
          private Gson gson;
          private JiaofeiListEntity jiaofeiListEntity;
          private List<pay_record> list;
          private JiaofeiListAdapter adapter;
          private boolean loadmore = false;
          private SharedpfTools sharedpfTools;
          private String type;
          private int pay_status = 1;
          private QueryAll queryAll;
          private Handler handler=new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                              super.handleMessage(msg);
                              switch (msg.arg1) {
                                        case 1:sendpost();//更新UI
                                                  break;
                              }

                    }
          };

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
                    // TODO: 2017/5/14
                    intidata();
//		            sendpost();
                    lvJiaofeiList.setOnItemClickListener(new OnItemClickListener() {

                              @Override
                              public void onItemClick(AdapterView<?> parent, View view,
                                                      int position, long id) {
                                        startActivity(new Intent(getApplication(), JiaofeiDetailActivity.class)
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
                                        pay_status = 1;
                                        //刷新数据，更新UI
//                                        sendpost();
                                        intidata();
                                        break;
                              case R.id.tv_jiaofei_title_right:
                                        tvLeft.setBackgroundResource(R.drawable.rounded_linearlayout_left_unpress);
                                        tvRight.setBackgroundResource(R.drawable.rounded_linearlayout_right_press);
                                        tvLeft.setTextColor(Color.WHITE);
                                        tvRight.setTextColor(Color.parseColor("#c0d355"));
                                        pay_status = 2;
                                        //刷新数据，更新UI
//                                        sendpost();
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
                    new Thread() {
                              @Override
                              public void run() {
                                        super.run();
                                        try {
                                                  // 1.注册驱动
                                                  Class.forName("com.mysql.jdbc.Driver");
                                                  // 2.获取连接
                                                  Connection conn = null;
                                                  //jdbc:mysql://192.168.31.210:3306/wysql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false
                                                  //conn = DriverManager.getConnection(QueryAll.mysqlUrl, QueryAll.mysqlRoot, QueryAll.mysqlpass);
                                                  conn = DriverManager.getConnection("jdbc:mysql://192.168.31.210:3306/wysql","root", "123456");
                                                  // 3.创建执行sql语句的对象
                                                  Statement stmt = null;
                                                  stmt = conn.createStatement();
                                                  // 4.书写一个sql语句
                                                  String sql;
                                                  //支付时间默认为0，
                                                  if (pay_status == 1) {
                                                            sql = "SELECT * FROM pay_record WHERE pay_time='0'";

                                                  } else {
                                                            sql = "SELECT * FROM pay_record WHERE LENGTH(pay_time)>1";
                                                  }
                                                  // 5.执行sql语句
                                                  ResultSet rs = null;
                                                  rs = stmt.executeQuery(sql);
                                                  // 6.对结果集进行处理
                                                  if (rs.next()) {
                                                            //查询得到结果
                                                            jiaofeiListEntity = new JiaofeiListEntity();
                                                            jiaofeiListEntity.setStatus(1);
                                                            jiaofeiListEntity.setMsg("");
                                                            //得到结果集(rs)的结构信息，比如字段数、字段名等
                                                            ResultSetMetaData md = rs.getMetaData();
                                                            //返回此 ResultSet 对象中的列数
                                                            int columnCount = md.getColumnCount();
                                                            //获取ResultSet 对象中的行数
                                                            rs.last();
                                                            int rowCount = rs.getRow();
                                                            rs.first();
                                                            System.out.println("行数：" + rowCount + "!列数：" + columnCount);
                                                            System.out.println(rs.toString());
                                                            //遍历结果集，封装到list
                                                            List<pay_record> list = new ArrayList<pay_record>();
                                                            //rs.next()下一行数据
                                                            for (int i = 0; i < rowCount; i++,rs.next()) {
                                                                      pay_record p_record = jiaofeiListEntity.new pay_record();
                                                                      //设置ID
                                                                      p_record.setId("" + rs.getInt(1));
                                                                      p_record.setType("" + rs.getInt(2));
                                                                      if (pay_status==1)
                                                                                p_record.setPay_time("未缴费！");
                                                                      else
                                                                                p_record.setPay_time(rs.getString(3));
                                                                      p_record.setPay_amount("" + rs.getFloat(4));
                                                                      p_record.setMonth(rs.getInt(5)+"月份");
                                                                      //设置每条数据
                                                                      list.add(p_record);
                                                            }
                                                            //设置整个结果数据
                                                            jiaofeiListEntity.setPay_record(list);
                                                            System.out.println("恭喜您，登录成功!");
                                                            System.out.println(sql);
                                                  } else {
                                                            //无数据设置为空
                                                            System.out.println("账号或密码错误!");
                                                  }
                                                  if (rs != null)
                                                            rs.close();
                                                  if (stmt != null)
                                                            stmt.close();
                                                  if (conn != null)
                                                            conn.close();
                                                  //查询完毕,通知UI更新
                                                  Message msg = handler.obtainMessage();
                                                  msg.arg1 = 1;
                                                  handler.sendMessage(msg);
                                        } catch (Exception e) {
                                                  System.out.println("连接数据库错误！");
                                                  e.printStackTrace();
                                        }
                              }
                    }.start();
          }

          public void sendpost() {

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
                    sendpost();
          }

          @Override
          public void onHeaderRefresh(AbPullToRefreshView arg0) {
                    loadmore = false;
                    sendpost();
          }
}
