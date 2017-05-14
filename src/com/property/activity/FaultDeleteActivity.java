package com.property.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.ui.BindView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.property.base.BaseActivity;
import com.property.utils.SharedpfTools;
import com.property.utils.UrlConnector;
import com.way.tabui.gokit.R;

public class FaultDeleteActivity extends BaseActivity {

	@BindView(id=R.id.iv_back, click=true)
	private ImageView ivBack;
	@BindView(id=R.id.tv_back, click=true)
	private TextView tvBack;
	@BindView(id=R.id.tv_delete_tijiao, click=true)
	private TextView tvTijiao;
	@BindView(id=R.id.et_delete_reason)
	private EditText etReason;
	
	private SharedpfTools sharedpfTools;
	private KJHttp http;
	private String id;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_fault_delete);
	}

	@Override
	public void initData() {
		super.initData();
		sharedpfTools = SharedpfTools.getInstance(this);
		http = new KJHttp();
		id = getIntent().getStringExtra("id");
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.iv_back:
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_delete_tijiao:
			sendpost();
			break;
		default:
			break;
		}
	}
	
	public void sendpost(){
		HttpParams params = new HttpParams();
		params.put("id", id);
		params.put("user_id", sharedpfTools.getUid());
		params.put("deleted_reason", etReason.getText().toString().trim());
		http.post(UrlConnector.FAULT_DELETE, params, false, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				Toast.makeText(getApplication(), "请求失败",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject object = new JSONObject(t);
					Toast.makeText(getApplication(),object.getString("msg"),Toast.LENGTH_SHORT).show();
					if(object.getInt("status")==1){
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		});
	}
}
