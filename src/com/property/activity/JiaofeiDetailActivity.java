package com.property.activity;

import org.kymjs.kjframe.ui.BindView;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.property.base.BaseActivity;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;
import com.google.gson.Gson;

public class JiaofeiDetailActivity extends BaseActivity {

	@BindView(id = R.id.iv_back, click=true)
	private ImageView ivBack;
	@BindView(id = R.id.tv_back, click=true)
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

	private AbHttpUtil http;
	private Gson gson;
	private JiaofeiDetailEntity jiaofeiDetailEntity;
	private String id;
	private String type=null;

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
		Log.e("type", type+"");
		switch (Integer.parseInt(type)) {
		case 1:
			tvType.setText("水费缴费");
			break;
		case 2:
			tvType.setText("电费缴费");
			break;
		case 5:
			tvType.setText("房租费缴费");
			break;
		case 6:
			tvType.setText("物业费缴费");
			break;
		case 7:
			tvType.setText("停车费缴费");
			break;
		case 8:
			tvType.setText("其他费用缴费");
			break;
		default:
			break;
		}
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

	public void sendpost() {
		AbRequestParams params = new AbRequestParams();
		params.put("id", id);
		params.put("type", type);
		http.post(UrlConnector.PAY_RECORD_DETAIL, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {

					}

					@Override
					public void onFinish() {

					}

					@Override
					public void onFailure(int arg0, String arg1, Throwable arg2) {
						Toast.makeText(getApplication(), "请求失败",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int arg0, String arg1) {
						jiaofeiDetailEntity = gson.fromJson(arg1,
								JiaofeiDetailEntity.class);
						tvAmount.setText(jiaofeiDetailEntity.getPay_record()
								.getPay_amount());
						tvPayee.setText(jiaofeiDetailEntity.getPay_record()
								.getPayee());
						if(type.equals("1") || type.equals("2")){
							llAccount.setVisibility(View.VISIBLE);
							tvAccount.setText(jiaofeiDetailEntity.getPay_record()
									.getAccount_name());
						}
						tvUserName.setText(jiaofeiDetailEntity.getPay_record()
								.getUsername());
						tvPayTime.setText(jiaofeiDetailEntity.getPay_record()
								.getPay_time());
					}
				});
	}
}
