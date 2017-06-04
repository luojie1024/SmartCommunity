package com.property.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.http.AbHttpUtil;
import com.google.gson.Gson;
import com.property.base.BaseActivity;
import com.property.bean.JiaofeiDetailEntity;
import com.way.tabui.gokit.R;

import org.kymjs.kjframe.ui.BindView;

import java.text.SimpleDateFormat;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.way.tabui.gokit.R.id.btn_loading;
import static org.kymjs.kjframe.ui.ViewInject.toast;

public class JiaofeiDetailActivity extends BaseActivity {

	@BindView(id = btn_loading, click = true)
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
	private String objectId;
	private String type = null;
	private int pay_status;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_jiaofeixiangqing);
	}

	@Override
	public void initData() {
		super.initData();
		http = AbHttpUtil.getInstance(this);
		gson = new Gson();
		objectId = getIntent().getStringExtra("objectId");
		type = getIntent().getStringExtra("type");
		pay_status=getIntent().getIntExtra("pay_status",1);
		Log.e("type", type + "");
		//c初始化jiaofeiDetailEntity
		jiaofeiDetailEntity=new JiaofeiDetailEntity();
		//已缴费模式，初始化按钮
		if (pay_status==2) {
			//禁止按钮点击 设置颜色
			bjiaofei.setClickable(false);
			bjiaofei.setText("缴费成功");
			bjiaofei.setBackgroundColor(Color.parseColor("#999999"));
		}


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

			case btn_loading:
				updatePay();
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
		//通过objectId查询数据
		BmobQuery<JiaofeiDetailEntity.pay_record> bmobQuery = new BmobQuery<JiaofeiDetailEntity.pay_record>();
		bmobQuery.getObject(objectId, new QueryListener<JiaofeiDetailEntity.pay_record>() {
			@Override
			public void done(JiaofeiDetailEntity.pay_record pay_record, BmobException e) {
				if(e==null){
					//查询成功
					jiaofeiDetailEntity.setStatus(1);
					jiaofeiDetailEntity.setMsg("");
					jiaofeiDetailEntity.setPay_record(pay_record);
					toast("查询成功!");
					updateUI();
				}else{
					jiaofeiDetailEntity.setStatus(0);
					Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
				}
			}
		});
	}


	//缴费逻辑
	public void updatePay(){
		JiaofeiDetailEntity.pay_record pay_re = jiaofeiDetailEntity.new pay_record();
		//获取当前系统时间
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		pay_re.setPay_time(date);
		pay_re.update(objectId, new UpdateListener() {
			@Override
			public void done(BmobException e) {
				if(e==null){
					toast("缴费成功！");
					//禁止按钮点击 设置颜色
					bjiaofei.setClickable(false);
					bjiaofei.setText("缴费成功");
					bjiaofei.setBackgroundColor(Color.parseColor("#999999"));
				}else{
					Log.i("bmob","服务器连接失败，请稍后重试！"+e.getMessage()+","+e.getErrorCode());
				}
			}
		});
	}

	//更新UI
	public void updateUI() {
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
	}
}
