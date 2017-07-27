package com.way.tabui.settingsmodule;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.way.tabui.commonmodule.GosBaseActivity;
import com.way.tabui.commonmodule.GosConstant;
import com.way.tabui.gokit.R;

public class SetBundMesActivity extends GosBaseActivity {

	private EditText ed_ProductKey, ed_AppId, ed_AppScrect,ed_ProductScrect;
	private Button bt_bdmes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_bund_mes);
		setActionBar(true, true, R.string.title_activity_set_bund_mes);
		 initview();
		 inintEvevt();
	}
	private void initview(){
		ed_ProductKey = (EditText) findViewById(R.id.ed_ProductKey);
		ed_AppId = (EditText) findViewById(R.id.ed_appid);
		ed_AppScrect = (EditText) findViewById(R.id.ed_appscrect);
		ed_ProductScrect=(EditText) findViewById(R.id.ed_ProductSecret);
		bt_bdmes = (Button) findViewById(R.id.bt_bdmes);
		//如果已经修改配置数据
		if (spf.getBoolean("ismodify", false)) {
			ed_AppId.setText(spf.getString("appid", "4d25e29be7e74a3aa18e2e7921cecb51"));
			ed_AppScrect.setText(spf.getString("appscrect", "84d1094f9dfe4911a961e5ef79b8e4f0"));
			String prroductkey = spf.getString("prroductkey", "2246c7de027244038dc8bae975453eb6");
			ed_ProductKey.setText(spf.getString("prroductkey", "2246c7de027244038dc8bae975453eb6"));
			String prroductscrect = spf.getString("prroductscrect", "ff1a9d62c35b4a4b9ca0c8eea0d120a2");
			ed_ProductScrect.setText(spf.getString("prroductscrect", "ff1a9d62c35b4a4b9ca0c8eea0d120a2"));
		} else {
			ed_ProductKey.setText(GosConstant.Product_Key);
			ed_AppId.setText(GosConstant.App_ID);
			ed_AppScrect.setText(GosConstant.App_Screct);
			ed_ProductScrect.setText(GosConstant.Product_Secret);
		}
	}
	private void inintEvevt(){
		bt_bdmes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//保存修改的数据
				SharedPreferences.Editor edit = spf.edit();
				edit.putBoolean("ismodify",true);
				edit.putString("prroductkey", ed_ProductKey.getText().toString());
				edit.putString("appid", ed_AppId.getText().toString());
				edit.putString("appscrect", ed_AppScrect.getText().toString());
				edit.putString("prroductscrect", ed_ProductScrect.getText().toString());
				edit.commit();


				Toast.makeText(getApplicationContext(),
						"存储信息完毕,重启后生效...", Toast.LENGTH_SHORT).show();

//				Intent reintent = getBaseContext().getPackageManager()
//						.getLaunchIntentForPackage(
//								getBaseContext().getPackageName());
//				PendingIntent restartIntent = PendingIntent.getActivity(
//						getApplicationContext(), 0, reintent,
//						PendingIntent.FLAG_ONE_SHOT);
//				AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//				mgr.set(AlarmManager.RTC, System.currentTimeMillis(),
//						restartIntent);
//				Intent intent = new Intent();
//				intent.setAction("com.way.util.exit_app");
//				sendBroadcast(intent);
			}
		});
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
