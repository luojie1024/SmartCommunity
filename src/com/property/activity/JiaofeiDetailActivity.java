package com.property.activity;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.google.gson.Gson;
import com.property.base.BaseActivity;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.ui.BindView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class JiaofeiDetailActivity extends BaseActivity {

	@BindView(id = R.id.btn_loading, click = true)
	private Button bjiaofei;
	@BindView(id = R.id.iv_back, click = true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click = true)
	private TextView tvBack;
	@BindView(id = R.id.tv_jiaofei_type)
	private TextView tvType;
	@BindView(id = R.id.tv_pay_amount)
	private TextView tvAmount;
	@BindView(id = R.id.tv_payee)
	private TextView tvPayee;
	@BindView(id = R.id.ll_account_name)
	private LinearLayout llAccount;
	@BindView(id = R.id.tv_account_name)
	private TextView tvAccount;
	@BindView(id = R.id.tv_jiaofei_username)
	private TextView tvUserName;
	@BindView(id = R.id.tv_pay_time)
	private TextView tvPayTime;
	//等待框
	private ProgressDialog pd;
	private AbHttpUtil http;
	private Gson gson;
	private JiaofeiDetailEntity jiaofeiDetailEntity;
	private String id;
	private String type = null;

	//UI更新
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.arg1) {
				case 1:
					//关闭等待框
					pd.dismiss();
					sendpost();//更新UI
					break;
				case 2:
					//关闭等待框
					pd.dismiss();
					//设置为不可点击
					bjiaofei.setClickable(false);
					Toast.makeText(JiaofeiDetailActivity.this, "缴费成功！", Toast.LENGTH_SHORT).show();
					break;
			}

		}
	};

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_jiaofeixiangqing);
	}

	@Override
	public void initData() {
		super.initData();
		http = AbHttpUtil.getInstance(this);
		gson = new Gson();
		id = getIntent().getStringExtra("id");
		type = getIntent().getStringExtra("type");
		Log.e("type", type + "");
		switch (Integer.parseInt(type)) {
			case 1:
				tvType.setText("水费缴费");
				break;
			case 2:
				tvType.setText("电费缴费");
				break;
			case 3:
				tvType.setText("房租费缴费");
				break;
			case 4:
				tvType.setText("物业费缴费");
				break;
			case 5:
				tvType.setText("停车费缴费");
				break;
			case 6:
				tvType.setText("其他费用缴费");
				break;
			default:
				break;
		}
//                    sendpost();
		//从数据库获取数据
		intidata();
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.iv_back:
			case R.id.tv_back:
				finish();
				break;

			case R.id.btn_loading:
				updata();
				System.out.println("按钮点击");
				break;
			default:
				break;
		}
	}

	/**
	 * 连接数据库，获取数据并生成JiaofeiListEntity,数据集合
	 */
	public void intidata() {
		pd = ProgressDialog.show(JiaofeiDetailActivity.this,"正在获取数据", "加载中，请稍后……");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 1.注册驱动
					Class.forName("com.mysql.jdbc.Driver");
					// 2.获取连接
					Connection conn = null;
					conn = DriverManager.getConnection("jdbc:mysql://192.168.31.210:3306/wysql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false","root", "123456");
					// 3.创建执行sql语句的对象
					Statement stmt = null;
					stmt = conn.createStatement();
					// 4.书写一个sql语句
					String sql;
					//支付时间默认为0，
					sql = "SELECT * FROM pay_record WHERE id=" + id + ";";
					// 5.执行sql语句
					ResultSet rs = null;
					rs = stmt.executeQuery(sql);
					// 6.对结果集进行处理
					if (rs.next()) {
						//查询得到结果
						jiaofeiDetailEntity = new JiaofeiDetailEntity();
						jiaofeiDetailEntity.setStatus(1);
						jiaofeiDetailEntity.setMsg("");

						//rs.next()下一行数据
						JiaofeiDetailEntity.pay_record p_record = jiaofeiDetailEntity.new pay_record();
						//设置数据
						p_record.setId("" + rs.getInt(1));
						p_record.setPay_time(rs.getString(3));
						p_record.setPay_amount("" + rs.getFloat(4));
						p_record.setPayee(rs.getString(6));
						p_record.setAccount_name(rs.getString(7));
						p_record.setUsername(rs.getString(8));
						//设置整个结果数据
						jiaofeiDetailEntity.setPay_record(p_record);
						System.out.println("获取缴费详情数据成功!");
						System.out.println(sql);
					} else {
						//无数据设置为空
						System.out.println("获取缴费详情数据失败!");
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


	//缴费逻辑
	public void updata(){
		pd = ProgressDialog.show(JiaofeiDetailActivity.this,"正在提交数据", "加载中，请稍后……");
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					// 1.注册驱动
					Class.forName("com.mysql.jdbc.Driver");
					// 2.获取连接
					Connection conn = null;
					conn = DriverManager.getConnection("jdbc:mysql://192.168.31.210:3306/wysql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&autoReconnect=true&failOverReadOnly=false","root", "123456");
					// 3.创建执行sql语句的对象
					Statement stmt = null;
					stmt = conn.createStatement();
					//获取当前系统时间
					SimpleDateFormat sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String    date  = sDateFormat.format(new    java.util.Date());
					// 4.书写一个sql语句
					String sql;
					//支付时间默认为0，
					sql = "UPDATE pay_record SET pay_time='"+date+"' WHERE id="+id+";";
					// 5.执行sql语句
					stmt.execute(sql);
					//关闭不需要的资源
					if (stmt != null)
						stmt.close();
					if (conn != null)
						conn.close();
					//查询完毕,通知UI更新
					Message msg = handler.obtainMessage();
					msg.arg1 = 2;
					handler.sendMessage(msg);
				} catch (Exception e) {
					System.out.println("连接数据库错误！");
					e.printStackTrace();
				}
			}
		}.start();
	}

	//更新UI
	public void sendpost() {
		tvAmount.setText(jiaofeiDetailEntity.getPay_record()
		.getPay_amount());
		tvPayee.setText(jiaofeiDetailEntity.getPay_record()
		.getPayee());
		if (type.equals("1") || type.equals("2")) {
			llAccount.setVisibility(View.VISIBLE);
			tvAccount.setText(jiaofeiDetailEntity.getPay_record()
			.getAccount_name());
		}
		tvUserName.setText(jiaofeiDetailEntity.getPay_record()
		.getUsername());
		tvPayTime.setText(jiaofeiDetailEntity.getPay_record()
		.getPay_time());


//		AbRequestParams params = new AbRequestParams();
//		params.put("id", id);
//		params.put("type", type);
//		http.post(UrlConnector.PAY_RECORD_DETAIL, params,
//				new AbStringHttpResponseListener() {
//
//					@Override
//					public void onStart() {
//
//					}
//
//					@Override
//					public void onFinish() {
//
//					}
//
//					@Override
//					public void onFailure(int arg0, String arg1, Throwable arg2) {
//						Toast.makeText(getApplication(), "请求失败",
//								Toast.LENGTH_SHORT).show();
//					}
//
//					@Override
//					public void onSuccess(int arg0, String arg1) {
//						jiaofeiDetailEntity = gson.fromJson(arg1,
//								JiaofeiDetailEntity.class);
//						tvAmount.setText(jiaofeiDetailEntity.getPay_record()
//								.getPay_amount());
//						tvPayee.setText(jiaofeiDetailEntity.getPay_record()
//								.getPayee());
//						if(type.equals("1") || type.equals("2")){
//							llAccount.setVisibility(View.VISIBLE);
//							tvAccount.setText(jiaofeiDetailEntity.getPay_record()
//									.getAccount_name());
//						}
//						tvUserName.setText(jiaofeiDetailEntity.getPay_record()
//								.getUsername());
//						tvPayTime.setText(jiaofeiDetailEntity.getPay_record()
//								.getPay_time());
//					}
//				});
	}
}
